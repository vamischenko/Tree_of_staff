package ru.hse.bi1.tree.model;

abstract class DictionaryElem {
	public abstract String getType();

	public abstract String getValue();

	public abstract String toString();

	public abstract boolean specialEquals(Object ob);

}
