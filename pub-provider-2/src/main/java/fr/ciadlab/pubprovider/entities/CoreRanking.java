package fr.ciadlab.pubprovider.entities;

public enum CoreRanking {
    A_PLUS_PLUS("A**"),
    A_PLUS("A*"),
    B("B"),
    C("C"),
    D("D");

    private String value;

    private CoreRanking(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}
