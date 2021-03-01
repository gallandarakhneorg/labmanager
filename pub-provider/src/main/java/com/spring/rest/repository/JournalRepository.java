package com.spring.rest.repository;

import com.spring.rest.entities.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface JournalRepository extends JpaRepository<Journal, Integer> {

    Set<Journal> findDistinctByJourPubsPubId(int pubId);

    @Query("SELECT DISTINCT j " +
            "FROM Journal j " +
            "INNER JOIN j.jourPubs p " +
            "INNER JOIN p.pubAuts au " +
            "INNER JOIN au.pk.aut a " +
            "INNER JOIN a.autOrgs org " +
            "WHERE org.resOrg.resOrgId = ?1")
    Set<Journal> findDistinctByJourPubsPubAutsAutAutOrgsResOrgResOrgId(int orgId);

    Optional<Journal> findByJourName(String jourName);
}
