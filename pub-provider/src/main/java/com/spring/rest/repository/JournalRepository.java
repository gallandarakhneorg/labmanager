package com.spring.rest.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.Journal;

public interface JournalRepository extends JpaRepository<Journal, Integer> {

	Set<Journal> findDistinctByJourPubsPubId(int pubId);
}
