/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

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