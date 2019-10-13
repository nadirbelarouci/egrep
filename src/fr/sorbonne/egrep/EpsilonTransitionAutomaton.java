package fr.sorbonne.egrep;

import java.util.stream.IntStream;

public class EpsilonTransitionAutomaton {

    public Automaton func(RegExTree regExTree) {
        Automaton r;
        if (regExTree.root == RegEx.ALTERN) {
            r = altern(func(regExTree.subTrees.get(0)), func(regExTree.subTrees.get(1)));
        } else if (regExTree.root == RegEx.CONCAT) {
            r = concat(func(regExTree.subTrees.get(0)), func(regExTree.subTrees.get(1)));
        } else if (regExTree.root == RegEx.ETOILE) {
            r = etoile(func(regExTree.subTrees.get(0)));
        } else {
            r = leaf(regExTree.root);
        }
        return r;
    }

    public Automaton concat(Automaton r1, Automaton r2) {
        Automaton automaton = new Automaton(r1, r2, 0);

        IntStream.of(r1.getFinalStates()).forEach(f -> automaton.epsilons[f].add(r2.getInitialStates()[0] + r1.STATES_COUNT));

        IntStream.of(r1.getFinalStates()).forEach(f -> automaton.finalStatesMatrix[f] = false);

        IntStream.of(r2.getInitialStates()).forEach(ini -> automaton.initialStatesMatrix[r1.STATES_COUNT + ini] = false);

        return automaton;
    }

    public Automaton altern(Automaton r1, Automaton r2) {
        Automaton automaton = new Automaton(r1, r2, 2);
        int i = automaton.STATES_COUNT - 2;

        int[] finals = automaton.getFinalStates();
        int[] initials = automaton.getInitialStates();

        IntStream.of(finals)
                .forEach(f -> automaton.epsilons[f].add(i + 1));

        IntStream.of(initials)
                .forEach(ini -> automaton.epsilons[i].add(ini));

        IntStream.of(finals).forEach(f -> automaton.finalStatesMatrix[f] = false);
        IntStream.of(initials).forEach(ini -> automaton.initialStatesMatrix[ini] = false);

        automaton.initialStatesMatrix[i] = true;
        automaton.finalStatesMatrix[i + 1] = true;

        return automaton;
    }

    public Automaton etoile(Automaton r) {
        Automaton automaton = new Automaton(r, 2);

        int i = r.STATES_COUNT;

        int[] finals = automaton.getFinalStates();
        int[] initials = automaton.getInitialStates();

        IntStream.of(finals)
                .findFirst()
                .ifPresent(f -> IntStream.of(initials)
                        .findFirst()
                        .ifPresent(ini -> automaton.epsilons[f].add(ini)));

        IntStream.of(finals).forEach(f -> automaton.finalStatesMatrix[f] = false);
        IntStream.of(initials).forEach(ini -> automaton.initialStatesMatrix[ini] = false);

        automaton.epsilons[i].add(i + 1);

        IntStream.of(initials).forEach(ini -> automaton.epsilons[i].add(ini));
        IntStream.of(finals).forEach(f -> automaton.epsilons[f].add(i + 1));

        automaton.initialStatesMatrix[i] = true;
        automaton.finalStatesMatrix[i + 1] = true;

        return automaton;
    }

    public Automaton leaf(int c) {
        Automaton automaton = new Automaton(2);
        automaton.finalStatesMatrix[1] = true;
        automaton.initialStatesMatrix[0] = true;
        automaton.adjacencyMatrix[0][c] = 1;

        return automaton;
    }
}

