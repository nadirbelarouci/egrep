package fr.sorbonne.egrep.fa;

import java.util.HashMap;
import java.util.Map;

public class State {

    private Map<CharSequence, Transition> transitions;
    private boolean isFinal;

    public State() {
        this(false);
    }

    public State(boolean isFinal) {
        this.transitions = new HashMap<>();
        this.isFinal = isFinal;
    }

    public State transit(CharSequence c) {
        return transitions.get(c).next();
    }

    public boolean isFinal() {
        return this.isFinal;
    }


    public State with(Transition tr) {
        this.transitions.put(tr.getRule(), tr);
        return this;
    }


    @Override
    public String toString() {
        return "State : { isFinal : " + isFinal + ", transitions : " + transitions + " }";
    }
}
