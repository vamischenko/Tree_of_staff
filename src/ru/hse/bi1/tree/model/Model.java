package ru.hse.bi1.tree.model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ru.hse.bi1.tree.forms.MainForm;

public class Model extends DefaultTreeModel {

	private static final long serialVersionUID = 1L;

	/** Префиксы тем для дерева: полный латинский алфавит A–Z. */
	public static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static DefaultMutableTreeNode root;

	public Model() {
		super(createRootNode());
		setAsksAllowsChildren(true);
	}

	public TreePath updatePerson(EmployeeAdapter entry) {
		TreePath path = null;
		DictionaryAnchor a = findByLastName((DefaultMutableTreeNode) getRoot(), entry);
		entry.update();
		DefaultMutableTreeNode new_node = (DefaultMutableTreeNode) a.entry;
		a = findByLastName((DefaultMutableTreeNode) getRoot(), entry);
		if (a.entry == null) {
			deletePerson(new_node);
			insertPerson(entry);
		}
		a = findByLastName((DefaultMutableTreeNode) getRoot(), entry);
		TreeNode[] nodes = getPathToRoot(a.entry);
		path = new TreePath(nodes);
		return path;
	}

	public void deletePerson(DefaultMutableTreeNode selectedNode) {
		if (selectedNode != getRoot()) {
			DictionaryElem elem = (DictionaryElem) selectedNode.getUserObject();
			if ("Entry".equals(elem.getType())) {
				DynamicTreeNode parent = null;
				if (selectedNode.getParent().getParent() != null
						&& selectedNode.getParent().getParent() instanceof DynamicTreeNode) {
					parent = (DynamicTreeNode) selectedNode.getParent().getParent();
				}
				removeNodeFromParent(selectedNode);
				if (parent != null) {
					parent.rebuild();
				}
			}
		}
	}

	public TreePath insertPerson(Employee data) {
		TreePath path = null;
		DictionaryAnchor anchor = new DictionaryAnchor();

		anchor = findByLastName((DefaultMutableTreeNode) getRoot(), data);

		if (anchor.entry == null) {
			// the proper topic has been found
			DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(data);
			new_node.setAllowsChildren(false);
			insertNodeInto(new_node, anchor.topic, anchor.topic.getChildCount());
			TreeNode[] nodes = getPathToRoot(new_node);
			path = new TreePath(nodes);
			if (anchor.topic instanceof DynamicTreeNode) {
				((DynamicTreeNode) anchor.topic).rebuild();
				nodes = getPathToRoot(findByLastName((DefaultMutableTreeNode) getRoot(), data).entry);
				path = new TreePath(nodes);
			}
		} else {
			TreeNode[] nodes = getPathToRoot(anchor.entry);
			path = new TreePath(nodes);
		}

		return path;
	}

	private DictionaryAnchor findByLastName(DefaultMutableTreeNode root, Employee new_entry) {
		DictionaryAnchor anchor = new DictionaryAnchor();

		@SuppressWarnings("rawtypes")
		Enumeration en = ((DefaultMutableTreeNode) root).children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();

			if (node.children().hasMoreElements()) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.children().nextElement();
				DictionaryAnchor a = findByLastName(node, new_entry);
				if (child.getUserObject() instanceof DictionaryTopic && a.topic != null) {
					return a;
				}
			}
			DictionaryElem elem = (DictionaryElem) node.getUserObject();
			if ("Topic".equals(elem.getType())) {
				String prefix = elem.getValue().toString();
				String val = new_entry.getValue();
				if (val.length() >= prefix.length() && val.substring(0, prefix.length()).equalsIgnoreCase(prefix)) {
					anchor.topic = node;
					break;
				}
			} else {
				break;
			}
		}

		if (anchor.topic != null) {
			en = anchor.topic.children();
			anchor.entry = null;

			while (en.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();

				DictionaryElem elem = (DictionaryElem) node.getUserObject();
				if ("Entry".equals(elem.getType())) {
					if (new_entry.equals(elem)) {
						anchor.entry = node;
						break;
					}
				}
			}

		}
		return anchor;
	}

	public static TreeNode createRootNode() {
		DefaultMutableTreeNode theRoot = new DefaultMutableTreeNode("Dictionary");
		root = theRoot;
		theRoot.setAllowsChildren(true);
		for (int i = 0; i < alphabet.length(); i++) {
			DictionaryElem nodeElem = new DictionaryTopic(alphabet.substring(i, i + 1));
			DefaultMutableTreeNode topic = new DynamicTreeNode(nodeElem);
			topic.setAllowsChildren(true);
			theRoot.add(topic);
		}
		return theRoot;
	}

	public void fireDataChange() {
		reload();
		MainForm.getInstance().searcher.reset((DefaultMutableTreeNode) getRoot());
	}

	public void readFile(File in) {
		try (FileInputStream fis = new FileInputStream(in); ObjectInputStream serial = new ObjectInputStream(fis)) {
			while (true) {
				try {
					Employee emp = (Employee) serial.readObject();
					insertPerson(new EmployeeAdapter(emp.fam, emp.name, emp.father, emp.birthday, emp.tab, emp.address,
							emp.path));
				} catch (EOFException e) {
					break;
				} catch (ClassNotFoundException e) {
					JOptionPane.showMessageDialog(MainForm.getInstance(), "Неверный формат файла: " + e.getMessage());
					return;
				}
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(MainForm.getInstance(), "Файл не найден");
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(MainForm.getInstance(), "Ошибка чтения: " + e.getMessage());
			return;
		}
		fireDataChange();
		MainForm.getInstance().expandAll();
	}

	public void writeFile(File out) {
		try (FileOutputStream fos = new FileOutputStream(out); ObjectOutputStream serial = new ObjectOutputStream(fos)) {
			treeWriter(root, serial);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(MainForm.getInstance(), "Не удалось создать файл");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(MainForm.getInstance(), "Ошибка записи: " + e.getMessage());
		}
	}

	public void treeWriter(final DefaultMutableTreeNode node, ObjectOutputStream out) throws IOException {
		if (node.getUserObject() instanceof EmployeeAdapter) {
			out.writeObject(((EmployeeAdapter) node.getUserObject()).getData());
			return;
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) node.getChildAt(i);
			treeWriter(newNode, out);
		}
	}
}