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

package fr.ciadlab.pubprovider.repository;

import fr.ciadlab.pubprovider.entities.ResearchOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ResearchOrganizationRepository extends JpaRepository<ResearchOrganization, Integer> {

    Set<ResearchOrganization> findDistinctByOrgAutsAutAutId(int autId);

    Set<ResearchOrganization> findDistinctByOrgSupResOrgId(int resOrgId);

    Set<ResearchOrganization> findDistinctByOrgSubsResOrgId(int resOrgId);


}