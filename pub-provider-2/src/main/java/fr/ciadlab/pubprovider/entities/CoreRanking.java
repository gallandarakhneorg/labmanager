package fr.ciadlab.pubprovider.entities;

public enum CoreRanking {
    A_PLUS_PLUS {
        public String toString() {
            return "A**";
        }
    },
    A_PLUS {
        public String toString() {
            return "A*";
        }
    },
    A,
    B,
    C,
    D
}