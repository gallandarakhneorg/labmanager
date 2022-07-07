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

import fr.ciadlab.pubprovider.entities.Authorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AuthorshipRepository extends JpaRepository<Authorship, Integer> {

    void deleteByAutAutIdAndPubPubId(int autAutId, int pubPubId);

    Optional<Authorship> findDistinctByAutAutIdAndPubPubId(int autAutId, int pubPubId);

    Set<Authorship> findByPubPubId(int pubPubId);
}
