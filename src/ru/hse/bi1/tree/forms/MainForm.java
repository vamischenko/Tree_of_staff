package ru.hse.bi1.tree.forms;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ru.hse.bi1.tree.model.Employee;
import ru.hse.bi1.tree.model.EmployeeAdapter;
import ru.hse.bi1.tree.model.Model;
import ru.hse.bi1.tree.model.Searcher;

public class MainForm extends JFrame implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;

	private Model model = new Model();

	private JTree theTree;
	private JMenuItem save;
	private JMenuItem open;
	private JMenuItem exit;
	private JMenuItem insertButton;
	private JMenuItem deleteButton;
	private JMenuItem changeLookFeelButton;
	private JPanel panel = new JPanel();
	private AdditionalForm curView;

	private JTextField searchF;
	private JComboBox<String> searchCmb;
	private JButton searchNextBtn;
	private static MainForm instance;

	public Searcher seacher = new Searcher();

	private UIManager.LookAndFeelInfo installedLF[];

	private int current;

	public static MainForm getInstance() {
		return instance;
	}

	public MainForm() {
		instance = this;
		setTitle("Tree  example with model");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buildGUI();
		installedLF = UIManager.getInstalledLookAndFeels();
		current = 0;
		setVisible(true);
		try {
			UIManager.setLookAndFeel(installedLF[current].getClassName());
		} catch (Exception ex) {
			System.out.println("Exception 1");
		}
		addListeners();
	}

	protected void buildGUI() {
		theTree = new JTree(model);
		expandAll();
		theTree.addTreeSelectionListener(this);
		int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
		theTree.getSelectionModel().setSelectionMode(mode);

		panel.setLayout(new GridLayout(1, 2));
		panel.add(new JScrollPane(theTree));

		add(panel, "Center");

		open = new JMenuItem("Open");
		save = new JMenuItem("Save as");
		exit = new JMenuItem("Exit");

		insertButton = new JMenuItem("Insert Person");

		deleteButton = new JMenuItem("Delete Selected");

		changeLookFeelButton = new JMenuItem("Change Look & Feel");

		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");

		file.add(open);
		file.add(save);
		file.add(exit);
		menu.add(file);

		JMenu edit = new JMenu("Edit tree");

		JMenu pf = new JMenu("Preferences");

		edit.add(insertButton);
		edit.add(deleteButton);

		menu.add(edit);
		pf.add(changeLookFeelButton);
		menu.add(pf);
		setJMenuBar(menu);

		Box searchPanel = Box.createHorizontalBox();
		searchPanel.add(new JLabel("Поиск:  "));
		searchF = new JTextField();
		searchPanel.add(searchF);
		String items[] = { "By last name", "By name", "By father name", "By birthday", "By address",
				"By tabel number" };
		searchCmb = new JComboBox<>(new DefaultComboBoxModel<>(items));
		searchPanel.add(searchCmb);
		searchNextBtn = new JButton("Find next");
		searchPanel.add(searchNextBtn);
		add(searchPanel, "South");
	}

	public void addListeners() {
		final MainForm form = this;

		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.showOpenDialog(form);
				if (jfc.getSelectedFile() != null)
					model.readFile(jfc.getSelectedFile());
			}
		});

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.showSaveDialog(form);
				if (jfc.getSelectedFile() != null)
					model.writeFile(jfc.getSelectedFile());
			}
		});

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.runFinalization();
				System.exit(0);
			}
		});

		insertButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Employee entry = new EmployeeAdapter();
				TreePath path = model.insertPerson(entry);
				if (path != null) {
					theTree.scrollPathToVisible(path);
				}
				MainForm.getInstance().getModel().fireDataChange();
				theTree.setSelectionPath(path);
			}
		});

		searchF.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				searchNextBtn.doClick();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) theTree.getLastSelectedPathComponent();
				if (selectedNode != null && selectedNode.getParent() != null)
					model.deletePerson(selectedNode);
				if (curView != null)
					panel.remove(curView);
				form.revalidate();
			}
		});

		searchNextBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Employee entry;
				TreePath originalPath = theTree.getSelectionPath();
				switch (searchCmb.getSelectedItem().toString()) {
				case "By name":
					entry = new EmployeeAdapter("", searchF.getText(), "", null, "", "", "");
					break;

				case "By last name":
					entry = new EmployeeAdapter(searchF.getText(), "", "", null, "", "", "");
					break;
				case "By father name":
					entry = new EmployeeAdapter("", "", searchF.getText(), null, "", "", "");
					break;
				case "By birthday":
					entry = new EmployeeAdapter("", "", "", CalendarField.parse(searchF.getText()), "", "", "");
					break;
				case "By address":
					entry = new EmployeeAdapter("", "", "", null, "", searchF.getText(), "");
					break;

				case "By tabel number":
					entry = new EmployeeAdapter("", "", "", null, searchF.getText(), "", "");
					break;
				default:
					entry = null;
				}
				TreePath path = seacher.findNext(entry);
				if (path != null) {
					theTree.setSelectionPath(path);
					theTree.expandPath(path);
				} else {
					theTree.setSelectionPath(originalPath);
					theTree.expandPath(originalPath);
					JOptionPane.showMessageDialog(form, "Employee not found!");
				}
			}
		});

		changeLookFeelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				current++;
				if (current > installedLF.length - 1) {
					current = 0;
				}

				System.out.println("New Current Look&Feel:" + current);
				System.out.println("New Current Look&Feel Class:" + installedLF[current].getClassName());

				try {
					UIManager.setLookAndFeel(installedLF[current].getClassName());
					SwingUtilities.updateComponentTreeUI(form);
				} catch (Exception ex) {
					System.out.println("exception");
				}

			}
		});
	}

	public void valueChanged(TreeSelectionEvent event) {
		TreePath path = theTree.getSelectionPath();
		if (path == null)
			return;

		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (selectedNode != null && selectedNode.getUserObject() instanceof Employee) {
			if (curView != null)
				panel.remove(curView);
			curView = ((EmployeeAdapter) selectedNode.getUserObject()).getView();
			panel.add(curView);
			this.revalidate();
		} else {
			if (curView != null)
				panel.remove(curView);
			this.revalidate();
		}
	}

	public Model getModel() {
		return model;
	}

	public DefaultMutableTreeNode getSelectedNode() {
		return (DefaultMutableTreeNode) theTree.getLastSelectedPathComponent();
	}

	public void expandPath(TreePath path) {
		theTree.expandPath(path);
	}

	public void selectPath(TreePath path) {
		theTree.setSelectionPath(path);
	}

	public void expandAll() {
		for (int i = 0; i < theTree.getRowCount(); i++) {
			theTree.expandRow(i);
		}
	}
}
