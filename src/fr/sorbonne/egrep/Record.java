package fr.sorbonne.egrep;

public class Record {
    public static Record EMPTY = new Record("", 0, 0);
    private String word;
    private int line;
    private int column;

    public Record(String word, int line, int column) {
        this.word = word;
        this.line = line;
        this.column = column;
    }

    public String getWord() {
        return word;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }


    @Override
    public String toString() {
        return "Record [word=" + word + ", line=" + line + ", column=" + column + "]";
    }

    public String getPosition() {
        return "(" + line + "," + column + ")";
    }

}
