package org.coffeecrew.examples.enumeration;

public enum ProgrammingLanguage {

    /* ASM is hardware ;) */
    ASM("Assembler", -10),
    C("C", 0),
    CPP("C ++", 10),
    Java("Java", 20);

    public enum Adjacence {
        UNSET,
        LESS,
        EQUAL,
        MORE
    };

    private ProgrammingLanguage(String longName, int hardwareAdjacence) {
        this.longName = longName;
        this.hardwareAdjacence = hardwareAdjacence;
    }
    private final String longName;
    private final int hardwareAdjacence;

    public int hardwareAdjacence() {
        return hardwareAdjacence;
    }

    public String longName() {
        return this.longName;
    }

    public Adjacence isMoreAdjecent(ProgrammingLanguage than) {
        Adjacence toReturn = Adjacence.UNSET;
        if (this.hardwareAdjacence() < than.hardwareAdjacence()) {
            toReturn = Adjacence.LESS;
        } else if (this.hardwareAdjacence() == than.hardwareAdjacence()) {
            toReturn = Adjacence.EQUAL;
        } else if (this.hardwareAdjacence() > than.hardwareAdjacence()) {
            toReturn = Adjacence.MORE;
        }
        return toReturn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Name: ");
        sb.append(name())
          .append("\nLong name: ")
          .append(longName)
          .append("\nOrdinal: ")
          .append(ordinal())
          .append("\nHardware adjacency: ")
          .append(hardwareAdjacence)
          .append("\n\n");
        return sb.toString();
    }
}