package fr.sorbonne.egrep;

import java.util.List;
import java.util.Map;

public class RadixTree {
    private Node root;

    public RadixTree() {
        root = new Node();
    }

    public void add(Index index) {
        Map<Character, List<Record>> records = index.getRecords();
        root = new Node();
        root.setCurrent('\0');
        root.setParent(null);
        root.setRecord(Record.EMPTY);
        root.addChildren(records);
    }
}
