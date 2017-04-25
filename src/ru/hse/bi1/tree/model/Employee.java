package ru.hse.bi1.tree.model;

import java.io.Serializable;
import java.util.Date;

public class Employee extends DictionaryElem implements Serializable {

	private static final long serialVersionUID = -1268094734864998615L;
	String fam, name, father, tab, address, path;
	Date birthday;

	public Employee() {
		this.fam = "New";
		this.name = "";
		this.father = "";
		this.birthday = new Date(System.currentTimeMillis());
		this.tab = "";
		this.address = "";
		this.path = "";
	}

	public Employee(String fam, String name, String father, Date birthday, String tab, String address, String path) {
		this.fam = fam;
		this.name = name;
		this.father = father;
		this.birthday = birthday;
		this.tab = tab;
		this.address = address;
		this.path = path;
	}

	public String getType() {
		return "Entry";
	}

	public String getValue() {
		return toString();
	}

	public String toString() {
		return fam + " " + name + " " + father;
	}

	public boolean equals(Object ob) {
		if (!(ob instanceof Employee))
			return false;
		Employee o = (Employee) ob;
		return o.fam.equals(fam) && o.name.equals(name) && o.father.equals(father) && o.birthday.equals(birthday)
				&& o.tab.equals(tab) && o.address.equals(address);
	}

	@Override
	public boolean specialEquals(Object ob) {
		if (!(ob instanceof Employee))
			return false;
		Employee o = (Employee) ob;
		if (birthday != null && o.birthday != null)
			return o.fam.equalsIgnoreCase(fam) || o.name.equalsIgnoreCase(name) || o.father.equalsIgnoreCase(father)
					|| o.birthday.equals(birthday) || o.tab.equalsIgnoreCase(tab)
					|| o.address.equalsIgnoreCase(address);
		else
			return o.fam.equalsIgnoreCase(fam) || o.name.equalsIgnoreCase(name) || o.father.equalsIgnoreCase(father)
					|| o.tab.equalsIgnoreCase(tab) || o.address.equalsIgnoreCase(address);
	}

}