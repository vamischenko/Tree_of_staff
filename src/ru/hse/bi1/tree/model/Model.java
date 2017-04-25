package ru.hse.bi1.tree.model;

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

import ru.hse.bi1.tree.forms.AdditionalForm;
import ru.hse.bi1.tree.forms.MainForm;

public class Model extends DefaultTreeModel {

	private static final long serialVersionUID = 1L;

	public static String alphabet = new String("ABCDEFGIJKLMNOPRSTUVWXYZ");

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
				if (new_entry.getValue().substring(0, elem.getValue().toString().length())
						.equalsIgnoreCase(elem.getValue())) {
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
		MainForm.getInstance().seacher.reset((DefaultMutableTreeNode) getRoot());
	}

	public void readFile(File in) {
		final AdditionalForm form = null;
		FileInputStream fis = null;
		ObjectInputStream serial = null;
		try {
			fis = new FileInputStream(in);
		} catch (FileNotFoundException e) {
			if (fis == null) {
				JOptionPane.showMessageDialog(form, "File cannot be found");
				return;
			}
		}
		try {
			serial = new ObjectInputStream(fis);
		} catch (IOException e) {
			if (serial == null) {
				JOptionPane.showMessageDialog(form, "File cannot be open");
			}

			e.printStackTrace();
		}
		while (true) {
			try {
				Employee emp = (Employee) serial.readObject();
				insertPerson(new EmployeeAdapter(emp.fam, emp.name, emp.father, emp.birthday, emp.tab, emp.address,
						emp.path));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				break;
			}
		}
		try {
			serial.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fireDataChange();
		MainForm.getInstance().expandAll();
	}

	public void writeFile(File out) {
		final AdditionalForm form = null;
		FileOutputStream fos = null;
		ObjectOutputStream serial = null;
		try {
			fos = new FileOutputStream(out);
		} catch (FileNotFoundException e) {
			{
				JOptionPane.showMessageDialog(form, "File cannot be found");
				return;
			}
		}
		try {
			serial = new ObjectOutputStream(fos);
		} catch (IOException e) {
			if (serial == null) {
				JOptionPane.showMessageDialog(form, "File cannot be open");
			}
		}
		treeWriter(root, serial);
		try {
			serial.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			serial.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void treeWriter(final DefaultMutableTreeNode node, ObjectOutputStream out) {
		final AdditionalForm form = null;
		if (node.getUserObject() instanceof EmployeeAdapter) {
			try {
				out.writeObject(((EmployeeAdapter) node.getUserObject()).getData());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(form, "I cannot write");
			}

			return;
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) node.getChildAt(i);
			treeWriter(newNode, out);
		}
	}
}