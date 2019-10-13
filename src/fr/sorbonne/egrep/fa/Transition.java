package fr.sorbonne.egrep.fa;

public class Transition  {

    private String rule;
    private State next;

    public Transition(String rule, State next) {
        this.rule = rule;
        this.next = next;
    }

    public State next() {
        return this.next;
    }


    public String getRule() {
        return rule;
    }

}
