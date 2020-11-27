package com.spring.rest.repository;

import com.spring.rest.entities.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface JournalRepository extends JpaRepository<Journal, Integer> {

    Set<Journal> findDistinctByJourPubsPubId(int pubId);

    Set<Journal> findDistinctByJourPubsPubAutsAutAutOrgsResOrgResOrgId(int orgId);

    Optional<Journal> findByJourName(String jourName);
}
