package ru.hse.bi1.tree.model;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ru.hse.bi1.tree.forms.MainForm;

public class DynamicTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

	private static int MAX = 5;

	private boolean isExtendOn = false;

	public DynamicTreeNode(Object userObject) {
		super(userObject);
	}

	@SuppressWarnings("unchecked")
	public void rebuild() {
		if (children.size() >= MAX && !isExtendOn) {
			isExtendOn = true;
			Vector<Object> copyChildren = (Vector<Object>) children.clone();
			removeAllChildren();
			for (int i = 0; i < Model.alphabet.length(); i++) {
				DictionaryElem nodeElem = new DictionaryTopic(this.toString() + Model.alphabet.substring(i, i + 1));
				DefaultMutableTreeNode topic = new DefaultMutableTreeNode(nodeElem);
				topic.setAllowsChildren(true);
				insert(topic, getChildCount());
			}
			for (Object o : copyChildren) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) o;
				MainForm.getInstance().getModel().insertPerson((Employee) child.getUserObject());
			}
			MainForm.getInstance().getModel().fireDataChange();
			return;
		}
		Vector<Object> copyChildren = new Vector<>();
		for (Object o : children) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) o;
			for (int i = 0; i < child.getChildCount(); i++) {
				copyChildren.add(child.getChildAt(i));
			}
		}
		if (copyChildren.size() < MAX && isExtendOn) {
			isExtendOn = false;
			removeAllChildren();
			for (Object o : copyChildren) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) o;
				MainForm.getInstance().getModel().insertPerson((Employee) child.getUserObject());
			}
			MainForm.getInstance().getModel().fireDataChange();
			MainForm.getInstance().expandPath(new TreePath(this.getPath()));
		}
	}

}
