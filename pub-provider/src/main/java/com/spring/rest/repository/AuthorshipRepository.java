package com.spring.rest.repository;

import com.spring.rest.entities.Authorship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AuthorshipRepository extends JpaRepository<Authorship, Integer> {
    void deleteByAutAutIdAndPubPubId(int autId, int pubId);

    Optional<Authorship> findDistinctByAutAutIdAndPubPubId(int autId, int pubId);

    Set<Authorship> findByPubPubId(int pubId);
}
