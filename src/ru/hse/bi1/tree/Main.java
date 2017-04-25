package ru.hse.bi1.tree;

import javax.swing.SwingUtilities;

import ru.hse.bi1.tree.forms.MainForm;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainForm();
			}
		});
	}
}
