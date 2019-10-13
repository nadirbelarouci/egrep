package fr.sorbonne.egrep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class Node {
    private Node parent;
    private Record record;
    private char current;
    private Map<Character, Node> children = new HashMap<>();

    public Node() {
    }

    public Node(Node parent, Record record, char current, Map<Character, Node> children) {
        this.parent = parent;
        this.record = record;
        this.current = current;
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public char getCurrent() {
        return current;
    }

    public void setCurrent(char current) {
        this.current = current;
    }

    public void addChildren(Map<Character, List<Record>> records) {
        records.entrySet()
                .stream()
                .collect(groupingBy(Map.Entry::getValue,
                        mapping(Map.Entry::getValue, toList())));
    }

}
