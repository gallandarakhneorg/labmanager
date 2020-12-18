package com.spring.rest.repository;

import com.spring.rest.entities.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {

    @Query("SELECT DISTINCT p FROM Publication p INNER JOIN  p.pubAuts a INNER JOIN a.pk.aut au INNER JOIN au.autOrgs org  WHERE org.resOrg.resOrgId = ?1")
    List<Publication> findAllByResOrgId(int resOrgId);

    @Query("SELECT DISTINCT p FROM Publication p INNER JOIN  p.pubAuts a INNER JOIN a.pk.aut au WHERE au.autId = ?1")
    List<Publication> findAllByAutId(int autId);

    Optional<Publication> findByPubId(int pubId);

}