package ru.hse.bi1.tree.model;

class DictionaryTopic extends DictionaryElem {
	private String theTopic;

	public DictionaryTopic(String topic) {
		theTopic = topic;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DictionaryTopic)) {
			return false;
		}
		return theTopic.equals(((DictionaryTopic) o).theTopic);
	}

	@Override
	public int hashCode() {
		return theTopic.hashCode();
	}

	public String getType() {
		return "Topic";
	}

	public String getValue() {
		return theTopic;
	}

	public String toString() {
		return theTopic;
	}

	@Override
	public boolean specialEquals(Object ob) {
		return equals(ob);
	}

}
