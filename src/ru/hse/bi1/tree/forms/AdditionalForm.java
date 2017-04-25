package ru.hse.bi1.tree.forms;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;
import ru.hse.bi1.tree.model.Employee;
import ru.hse.bi1.tree.model.EmployeeAdapter;

public class AdditionalForm extends JPanel {

	private static final long serialVersionUID = 1L;

	JTextField fam, name, father, birthday, address, path;

	PicturePanel picture;
	private JFormattedTextField tab;
	private JButton editButton;

	public AdditionalForm(String fam, String name, String father, Date birthday, String tab, String address,
			String path) {
		editButton = new JButton("Save");
		setLayout(new MigLayout("", "3[fill]5[fill,grow]3", "5[]3[]3[]3[]3[]3[]3[]3[]3[]10[]15[]5"));
		//
		this.fam = new JTextField(fam);
		this.name = new JTextField(name);
		this.father = new JTextField(father);
		this.birthday = new CalendarField(birthday);
		this.tab = new JFormattedTextField(tab);
		this.address = new JTextField(address);
		this.path = new JTextField(path);
		picture = new PicturePanel();
		//
		add(new JLabel("<html>Фамилия<font color=red>*"), "");
		add(this.fam, "pushx,growx,wrap");
		add(new JLabel("<html>Имя<font color=red>*"), "");
		add(this.name, "pushx,growx,wrap");
		add(new JLabel("<html>Отчество<font color=red>*"), "");
		add(this.father, "pushx,growx,wrap");
		add(new JLabel("<html>Дата Рождения<font color=red>*"), "");
		add(this.birthday, "pushx,growx,wrap");
		add(new JLabel("<html>Табельный номер<font color=red>**"), "");
		add(this.tab, "pushx,growx,wrap");
		add(new JLabel("<html>Адрес<font color=red>*"), "");
		add(this.address, "pushx,growx,wrap");
		add(new JLabel("Путь к фотографии"), "");
		add(this.path, "pushx,growx,wrap");
		add(new JLabel("<html><font color=red>*</font>- обязательны к заполнению"), "wrap");
		add(new JLabel("<html><font color=red>**</font>- 5 цифр"), "wrap");
		add(editButton, "span 2, w 150!,al center,wrap");
		add(picture, "span 2,al center,push,grow");

		try {
			this.tab.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("#####")));
		} catch (ParseException e) {
			// пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅ
			// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
			e.printStackTrace();
		}

		final AdditionalForm form = this;

		this.fam.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				form.name.requestFocus();
			}
		});
		this.name.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				form.father.requestFocus();
			}
		});
		this.father.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				form.birthday.requestFocus();
			}
		});
		this.birthday.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				form.tab.requestFocus();
			}
		});
		this.tab.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				form.address.requestFocus();
			}
		});
		this.address.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				form.path.requestFocus();
			}
		});
		this.path.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editButton.doClick();
			}
		});

		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
				// пїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
				// пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ
				if (form.fam.getText().isEmpty()) {
					JOptionPane.showMessageDialog(form, "Fam cannot be empty");
					return;
				}
				if (form.name.getText().isEmpty()) {
					JOptionPane.showMessageDialog(form, "Name cannot be empty");
					return;
				}
				if (form.father.getText().isEmpty()) {
					JOptionPane.showMessageDialog(form, "Father name cannot be empty");
					return;
				}
				if (form.birthday.getText().equals("..")) {
					JOptionPane.showMessageDialog(form, "Birthday cannot be empty");
					return;
				}
				if (form.tab.getText().isEmpty()) {
					JOptionPane.showMessageDialog(form, "Tab number cannot be empty");
					return;
				}
				if (form.address.getText().isEmpty()) {
					JOptionPane.showMessageDialog(form, "Address cannot be empty");
					return;
				}
				if (!Character.isLetter(form.fam.getText().charAt(0))) {
					form.fam.setText("");
					JOptionPane.showMessageDialog(form, "Fam must be started from letter");
					return;
				}
				if (!Character.isLetter(form.name.getText().charAt(0))) {
					form.name.setText("");
					JOptionPane.showMessageDialog(form, "Name must be started from letter");
					return;
				}
				if (!Character.isLetter(form.father.getText().charAt(0))) {
					form.father.setText("");
					JOptionPane.showMessageDialog(form, "Father must be started from letter");
					return;
				}

				TreePath newPath = null;
				DefaultMutableTreeNode selectedNode = MainForm.getInstance().getSelectedNode();
				if (selectedNode != null && selectedNode.getUserObject() instanceof Employee) {
					newPath = MainForm.getInstance().getModel()
							.updatePerson((EmployeeAdapter) selectedNode.getUserObject());
				}

				if (newPath != null) {
					MainForm.getInstance().getModel().fireDataChange();
					MainForm.getInstance().expandPath(newPath.getParentPath());
					MainForm.getInstance().selectPath(newPath);
				}
				picture.init();
			}
		});
	}

	public String getFamVal() {
		return fam.getText().trim();
	}

	public String getNameVal() {
		return name.getText().trim();
	}

	public String getFatherVal() {
		return father.getText().trim();
	}

	public Date getBirthdayVal() {
		return ((CalendarField) birthday).getDate();
	}

	public String getTabVal() {
		return tab.getText().trim();
	}

	public String getAddressVal() {
		return address.getText().trim();
	}

	public String getPathVal() {
		return path.getText().trim();
	}

	class PicturePanel extends JPanel {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;
		Image img;

		public PicturePanel() {
			init();
			this.addComponentListener(new ComponentListener() {

				@Override
				public void componentShown(ComponentEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void componentResized(ComponentEvent e) {
					init();

				}

				@Override
				public void componentMoved(ComponentEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void componentHidden(ComponentEvent e) {
					// TODO Auto-generated method stub

				}
			});
		}

		public void init() {

			Toolkit kit = Toolkit.getDefaultToolkit();
			if (!getPathVal().isEmpty())
				img = kit.getImage(getPathVal());
			else
				img = kit.getImage("default.jpg");
			if (!(getWidth() == 0 || getHeight() == 0))
				img = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			this.repaint();
		}

		public void paint(Graphics g) {
			g.drawImage(img, 0, 0, this);
		}

	}

}
