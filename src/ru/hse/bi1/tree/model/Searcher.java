package ru.hse.bi1.tree.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class Searcher {
	private List<DefaultMutableTreeNode> content = new ArrayList<>();
	int pos = 0;

	public Searcher() {
	}

	public void reset(DefaultMutableTreeNode root) {
		content.clear();
		addToModel(root);
		pos = 0;
	}

	private void addToModel(DefaultMutableTreeNode root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			if (((DefaultMutableTreeNode) root.getChildAt(i)).getUserObject() instanceof Employee) {
				content.add(((DefaultMutableTreeNode) root.getChildAt(i)));
			} else {
				addToModel((DefaultMutableTreeNode) root.getChildAt(i));
			}
		}
	}

	public TreePath findNext(Employee emp) {
		for (int i = pos; i < content.size(); i++) {
			if (emp.specialEquals(content.get(i).getUserObject())) {
				pos = i + 1;
				if (pos == content.size())
					pos = 0;
				return new TreePath(content.get(i).getPath());
			}
			if (i == content.size() - 1) {
				i = -1;
			}
			if (i == pos - 1) {
				break;
			}
		}
		return null;
	}
}
