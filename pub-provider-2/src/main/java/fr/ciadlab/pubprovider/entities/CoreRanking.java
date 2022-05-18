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
    D;

    public static CoreRanking getCoreRankingFromString(String stringCoreRanking) {
        switch (stringCoreRanking) {
            case "A**":
                return A_PLUS_PLUS;
            case "A*":
                return A_PLUS;
            default:
                return valueOf(stringCoreRanking);
        }
    }
}