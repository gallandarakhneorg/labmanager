package com.spring.rest.repository;

import com.spring.rest.entities.Author;
import com.spring.rest.entities.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Set<Author> findDistinctByAutPubsPubPubId(int pubId);

    Set<Author> findDistinctByAutOrgsResOrgResOrgId(int resOrgId);

    Optional<Author> findByAutFirstNameAndAutLastName(String autFirstName, String autlastName);

    Set<Author> findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(String resOrgName, MemberStatus status);
}
