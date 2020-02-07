package com.spring.rest.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
	
	Set<Publication> findDistinctByPubAutsAutAutOrgsResOrgResOrgId(int resOrgId);

	Set<Publication> findDistinctByPubAutsAutAutId(int autId);
	
}