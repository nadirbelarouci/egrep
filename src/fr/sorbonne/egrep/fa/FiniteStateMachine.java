package fr.sorbonne.egrep.fa;

public class FiniteStateMachine {

    private State current;

    public FiniteStateMachine(State initial) {
        this.current = initial;
    }

    public FiniteStateMachine switchState(CharSequence c) {
        return new FiniteStateMachine(this.current.transit(c));
    }

    public boolean canStop() {
        return this.current.isFinal();
    }
}
