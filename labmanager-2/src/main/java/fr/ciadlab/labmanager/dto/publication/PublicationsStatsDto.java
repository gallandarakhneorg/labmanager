/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.dto.publication;

import java.util.Map;

import fr.ciadlab.labmanager.controller.view.publication.PublicationsStat;

/**
 * Data Transfer Object (DTO) for representing the statistics of a group of publications.
 */
public class PublicationsStatsDto {

    private PublicationsStat globalStats;
    private Map<Integer, PublicationsStat> yearlyStats;

    public PublicationsStatsDto(PublicationsStat globalStats, Map<Integer, PublicationsStat> yearlyStats) {
        this.globalStats = globalStats;
        this.yearlyStats = yearlyStats;
    }

    public PublicationsStat getGlobalStats() {
        return globalStats;
    }

    public  Map<Integer, PublicationsStat> getYearlyStats() {
        return yearlyStats;
    }
}
