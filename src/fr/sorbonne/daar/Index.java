package fr.sorbonne.daar;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
public class Index {
	static AtomicInteger i = new AtomicInteger();
	static Map<String, int[]> kmpTable = new ConcurrentHashMap<>();

	public static void main(String[] args) throws Exception {
		Comparator<Record> comparator = Comparator.comparing(record->record.word);
		comparator = comparator.thenComparing(record->record.line);
		comparator = comparator.thenComparing(record->record.colomn);

		List<String> records = Files.lines(Paths.get("Text"))
				.parallel()
				.flatMap(line -> processLine(line.toLowerCase()))
				.collect(groupingBy(record->record.word, 
						  mapping(Record::getPosition, joining(" "))))
				.entrySet().stream().map(e->e.getKey()+" "+e.getValue())
				.sorted()
				.collect(toList());
		
		System.out.println(records.subList(0, 20).stream().collect(joining("\n")));
		
		
		
	}

	static Stream<Record> processLine(String line) {
		int lineIndex = i.incrementAndGet();
		String[] words = line.split("[^a-zA-Z]");
		Stream<Record> records = Stream.of(words).parallel().filter(word -> !word.isEmpty())
				.map(word -> createRecord(line.toCharArray(), word.toLowerCase(),lineIndex));
		
		return records;
	}

	static Record createRecord(char[] line, String word,int i) {
		Record record = new Record();

		int[] kmp = kmpTable.computeIfAbsent(word, pattern -> KMP.kmpTable(word));
		record.word = word;
		record.line = i;
		record.colomn = KMP.match(word.toCharArray(), kmp, line);
		return record;
	}

	static class Record {
		String word;
		int line;
		int colomn;

		@Override
		public String toString() {
			return "Record [word=" + word + ", line=" + line + ", colomn=" + colomn + "]";
		}
		public String getPosition() {
			return "("+line+","+colomn+")";
		}

	}
}
