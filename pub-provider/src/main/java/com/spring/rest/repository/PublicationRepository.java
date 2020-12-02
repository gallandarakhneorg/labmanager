package com.spring.rest.repository;

import com.spring.rest.entities.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {

    List<Publication> findDistinctByPubAutsAutAutOrgsResOrgResOrgId(int resOrgId);

    List<Publication> findDistinctByPubAutsAutAutId(int autId);

}