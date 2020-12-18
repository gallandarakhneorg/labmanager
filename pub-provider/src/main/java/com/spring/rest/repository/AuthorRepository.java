package com.spring.rest.repository;

import com.spring.rest.entities.Author;
import com.spring.rest.entities.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("SELECT DISTINCT a FROM Author a INNER JOIN a.autPubs p INNER JOIN p.pk.pub pb WHERE pb.pubId = ?1")
    Set<Author> findDistinctByAutPubsPubPubId(int pubId);

    Set<Author> findDistinctByAutOrgsResOrgResOrgId(int resOrgId);

    Set<Author> findByAutFirstNameAndAutLastName(String autFirstName, String autlastName);

    Set<Author> findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(String resOrgName, MemberStatus status);
}
