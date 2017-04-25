package ru.hse.bi1.tree.model;

import java.util.Date;

import ru.hse.bi1.tree.forms.AdditionalForm;

public class EmployeeAdapter extends Employee {

	private static final long serialVersionUID = 8574279049831135128L;
	private AdditionalForm view;

	public EmployeeAdapter() {
		super();
	}

	public EmployeeAdapter(String fam, String name, String father, Date birthday, String tab, String address,
			String path) {
		super(fam, name, father, birthday, tab, address, path);
	}

	public AdditionalForm getView() {
		return view = new AdditionalForm(fam, name, father, birthday, tab, address, path);
	}

	public void update() {
		fam = view.getFamVal();
		name = view.getNameVal();
		father = view.getFatherVal();
		birthday = view.getBirthdayVal();
		tab = view.getTabVal();
		address = view.getAddressVal();
		path = view.getPathVal();
	}

	public Employee getData() {
		return new Employee(fam, name, father, birthday, tab, address, path);
	}

}
