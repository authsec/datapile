package org.coffeecrew.examples.enumeration;

public class Main {
    public static void main(String[] args) {
        for (ProgrammingLanguage pl: ProgrammingLanguage.values()) {
            printProgrammingLanguage(pl);
            printMoreAdjacent(ProgrammingLanguage.C, pl);
        }
    }

    public static void printProgrammingLanguage(ProgrammingLanguage pl) {
        System.out.println(pl);
    }

    public static void printMoreAdjacent(ProgrammingLanguage moreAdjacent,
                                         ProgrammingLanguage than) {
        ProgrammingLanguage.Adjacence adj = moreAdjacent.isMoreAdjecent(than);
        StringBuilder text = new StringBuilder();
        text.append(moreAdjacent.name())
                .append(" (")
                .append(moreAdjacent.longName())
                .append(")")
                .append(" is ");

        switch (adj) {
            case LESS:
                text.append(adj.name());
                break;
            case EQUAL:
                text.append(adj.name());
                break;
            case MORE:
                text.append(adj.name());
                break;
            default:
                text.append(adj.name());
        }

        text.append(" hardware adjacent than ")
                .append(than.name())
                .append(" (")
                .append(than.longName())
                .append(")");
        System.out.println(text.toString());
    }
}
