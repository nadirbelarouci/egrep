package fr.sorbonne.daar;

import java.util.HashMap;
import java.util.Map;

public class Node {
	private Node parent;
	private String data;
	private Map<String, Node> children = new HashMap<>();

	public Node(Node parent, String data, Map<String, Node> children) {
		super();
		this.parent = parent;
		this.data = data;
		this.children = children;
	}

	public Node() {
		super();
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Map<String, Node> getChildren() {
		return children;
	}

	public void setChildren(Map<String, Node> children) {
		this.children = children;
	}

	public void addChild(String s, Node node) {
		children.put(s, node);
	}
}
