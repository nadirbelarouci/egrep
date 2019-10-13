package fr.sorbonne.egrep;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class RadixTree {
    private Node root;

    public RadixTree() {
        root = new Node();
    }

    public static void main(String[] args) throws Exception {
        RadixTree radixTree = new RadixTree();
        radixTree.add(new Index("Text"));
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(radixTree.root));
    }

    public Record search(String value){
        return root.search(value,0);
    }

    public void add(Index index) {
        Map<Character, List<Record>> records = index.getRecords();
        root = new Node();
        root.setCurrent('\0');
        root.setParent(null);
        root.setRecord(Record.EMPTY);
        root.addChildren(records, 0);
    }
}
