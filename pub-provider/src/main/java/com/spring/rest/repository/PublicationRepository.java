package com.spring.rest.repository;

import com.spring.rest.entities.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {

    Set<Publication> findDistinctByPubAutsAutAutOrgsResOrgResOrgId(int resOrgId);

    Set<Publication> findDistinctByPubAutsAutAutId(int autId);

}