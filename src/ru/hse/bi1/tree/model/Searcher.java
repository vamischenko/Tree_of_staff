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

	/**
	 * Ищет следующую запись, совпадающую по {@link Employee#specialEquals}, начиная с текущей позиции
	 * и проходя список по кругу.
	 */
	public TreePath findNext(Employee emp) {
		int n = content.size();
		if (n == 0) {
			return null;
		}
		for (int step = 0; step < n; step++) {
			int i = (pos + step) % n;
			if (emp.specialEquals(content.get(i).getUserObject())) {
				pos = (i + 1) % n;
				return new TreePath(content.get(i).getPath());
			}
		}
		return null;
	}
}
