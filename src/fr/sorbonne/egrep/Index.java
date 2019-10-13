package fr.sorbonne.egrep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Index {
    private static Map<String, int[]> kmpTable = new ConcurrentHashMap<>();
    private String fileName;
    private AtomicInteger currentLine = new AtomicInteger();
    private Map<Character, List<Record>> records;

    public Index(String fileName) throws IOException {
        this.fileName = fileName;
        process();
    }

    private void process() throws IOException {
        records = Files.lines(Paths.get(fileName))
                .flatMap(line -> processLine(line.toLowerCase()))
                .collect(groupingBy(record -> record.getWord().charAt(0)));
    }

    private Stream<Record> processLine(String line) {
        int lineIndex = currentLine.incrementAndGet();
        String[] words = line.split("[^a-zA-Z]");
        return Stream.of(words)
                .parallel()
                .filter(word -> !word.isEmpty())
                .map(word -> createRecord(line.toCharArray(), word.toLowerCase(), lineIndex));

    }

    private Record createRecord(char[] line, String word, int i) {
        int[] table = kmpTable.computeIfAbsent(word, pattern -> KMP.kmpTable(word));
        return new Record(word, i, KMP.match(word.toCharArray(), table, line));
    }

    public void save() throws IOException {
        List<String> lines = this.records.entrySet().stream().map(e -> e.getKey() + " " + e.getValue())
                .sorted()
                .collect(toList());
        Path path = Paths.get(fileName + ".index");
        if (!Files.exists(path))
            Files.createFile(path);

        Files.write(path, lines);
    }

    public Map<Character, List<Record>> getRecords() {
        return records;
    }
}
