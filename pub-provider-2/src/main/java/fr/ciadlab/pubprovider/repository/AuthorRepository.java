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

import fr.ciadlab.pubprovider.entities.Author;
import fr.ciadlab.pubprovider.entities.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    List<Author> findByAutPubsPubPubIdOrderByAutPubsAutShipRank(int pubPubId);
    
    List<Author> findByAutIdIn(List<Integer> autId);

    Set<Author> findDistinctByAutOrgsResOrgResOrgId(int resOrgId);

    Set<Author> findByAutFirstNameAndAutLastName(String autFirstName, String autlastName);

    Set<Author> findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(String resOrgName, MemberStatus status);
}
