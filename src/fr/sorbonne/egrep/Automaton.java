package fr.sorbonne.egrep;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Automaton {
    protected final int STATES_COUNT;
    protected final int MAX_COLUMNS = 256;

    protected final int[][] adjacencyMatrix;
    protected final HashSet<Integer>[] epsilons;
    protected final boolean[] initialStatesMatrix;
    protected final boolean[] finalStatesMatrix;

    public Automaton(int statesCount) {
        this.STATES_COUNT = statesCount;

        this.adjacencyMatrix = new int[STATES_COUNT][MAX_COLUMNS];
        this.initialStatesMatrix = new boolean[STATES_COUNT];
        this.finalStatesMatrix = new boolean[STATES_COUNT];

        this.epsilons = (HashSet<Integer>[]) new HashSet<?>[STATES_COUNT];

        for (int i = 0; i < STATES_COUNT; i++) {
            for (int j = 0; j < MAX_COLUMNS; j++) {
                adjacencyMatrix[i][j] = -1;
            }
            this.epsilons[i] = new HashSet<Integer>();
        }
    }

    public Automaton(Automaton original, int addedStates) {
        this(original.STATES_COUNT + addedStates);

        for (int i = 0; i < original.STATES_COUNT; i++) {
            this.adjacencyMatrix[i] = original.adjacencyMatrix[i];
            this.initialStatesMatrix[i] = original.initialStatesMatrix[i];
            this.finalStatesMatrix[i] = original.finalStatesMatrix[i];

            this.epsilons[i] = (HashSet<Integer>) original.epsilons[i].clone();
        }
    }

    public Automaton() {
        this(3);
    }

    public Automaton(Automaton original1, Automaton original2, int addedStates) {
        this(original1, original2.STATES_COUNT + addedStates);

        for (int i = 0; i < original2.STATES_COUNT; i++) {
            System.out.println("i = " + i);
            this.adjacencyMatrix[i + original1.STATES_COUNT] = IntStream.of(original2.adjacencyMatrix[i])
                    .map(state -> {
                        if (state != -1) return state + original1.STATES_COUNT;
                        else return -1;
                    }).toArray();

        }

        for (int i = 0; i < original1.STATES_COUNT; i++) {
            this.initialStatesMatrix[i] = original1.initialStatesMatrix[i];
            this.finalStatesMatrix[i] = original1.finalStatesMatrix[i];

            this.epsilons[i] = new HashSet<>(original1.epsilons[i]);
        }

        for (int i = 0; i < original2.STATES_COUNT; i++) {
            this.initialStatesMatrix[original1.STATES_COUNT + i] = original2.initialStatesMatrix[i];
            this.finalStatesMatrix[original1.STATES_COUNT + i] = original2.finalStatesMatrix[i];

            this.epsilons[original1.STATES_COUNT + i] = (HashSet<Integer>) original2.epsilons[i]
                    .stream()
                    .map(state -> state + original1.STATES_COUNT)
                    .collect(Collectors.toSet());
        }
    }

    public int[] getFinalStates() {
        return IntStream.range(0, finalStatesMatrix.length)
                .filter(i -> finalStatesMatrix[i])
                .toArray();
    }

    public int[] getInitialStates() {
        return IntStream.range(0, initialStatesMatrix.length)
                .filter(i -> initialStatesMatrix[i])
                .toArray();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder("Adjacency : ").append('\n').append(" ");

        String s = IntStream.rangeClosed('0', 'z')
                .mapToObj(Integer::toString)
                .map(s1 -> s1 + spaces(5 - s1.length()))
                .collect(Collectors.joining("  "));

        stringBuilder
                .append(s)
                .append("\n");
        for (int i = 0; i < STATES_COUNT; i++) {
            for (int j = '0'; j <= 'z'; j++) {
                stringBuilder.append(" ").append(adjacencyMatrix[i][j])
                        .append(spaces(6 - Integer.toString(adjacencyMatrix[i][j]).length()));
            }
            stringBuilder.append('\n');
        }
        stringBuilder.append("Initial states : ").append(Arrays.toString(initialStatesMatrix)).append('\n');
        stringBuilder.append("Final states : ").append(Arrays.toString(finalStatesMatrix)).append('\n');

        stringBuilder.append("Epsilons : ").append(Arrays.toString(epsilons));

        return stringBuilder.toString();
    }

    private String spaces(int size) {
        return IntStream.range(0, size).mapToObj(i -> " ").collect(Collectors.joining());
    }
}
