package fr.sorbonne.egrep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@JsonIgnoreProperties(value = {"parent"})

public class Node {
    private Node parent;
    private Record record;
    private char current;
    private Map<Character, Node> children = new HashMap<>();

    public Node() {
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

    public void addChildren(Map<Character, List<Record>> records, int length) {
        records.forEach((c, list) -> children.put(c, createNode(c, list, length + 1)));
    }

    public Node createNode(char c, List<Record> records, int length) {
        Node node = new Node();
        node.setCurrent(c);
        node.setParent(this);
        if (records.size() == 1) {
            node.setRecord(records.get(0));
        } else {
            Map<Character, List<Record>> children = records.stream()
                    .filter(record -> isValid(node, record, length))
                    .collect(groupingBy(record -> record.getWord().charAt(length)));
            node.addChildren(children, length);
        }
        return node;
    }

    private boolean isValid(Node node, Record record, int length) {
        if (record.getWord().length() == length) {
            node.setRecord(record);
            return false;
        }
        return true;
    }

    public Map<Character, Node> getChildren() {
        return children;
    }

    public void setChildren(Map<Character, Node> children) {
        this.children = children;
    }

    public List<Record> search(String value, int length) {
        if (current == value.charAt(length)) {
            return collect();
        } else {
            return children.get(value.charAt(length)).search(value, length + 1);
        }
    }

    private List<Record> collect() {
        List<Record> list = new ArrayList<>();
        if (record != null)
            list.add(record);

        list.addAll(children.values().stream().flatMap(node -> node.collect().stream()).collect(toList()));
        return list;
    }
}
