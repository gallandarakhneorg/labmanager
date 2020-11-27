package com.spring.rest.repository;

import com.spring.rest.entities.Authorship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorshipRepository extends JpaRepository<Authorship, Integer> {
    void deleteByAutAutIdAndPubPubId(int autId, int pubId);

    Optional<Authorship> findDistinctByAutAutIdAndPubPubId(int autId, int pubId);
}
