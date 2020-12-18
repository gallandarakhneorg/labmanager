package com.spring.rest.repository;

import com.spring.rest.entities.Authorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AuthorshipRepository extends JpaRepository<Authorship, Integer> {

    @Query("DELETE FROM Authorship a WHERE a.pk.aut.autId = ?1 and a.pk.pub.pubId = ?2")
    void deleteByAutAutIdAndPubPubId(int autId, int pubId);

    @Query("SELECT DISTINCT a FROM Authorship a WHERE a.pk.aut.autId = ?1 and a.pk.pub.pubId = ?2")
    Optional<Authorship> findDistinctByAutAutIdAndPubPubId(int autId, int pubId);

    @Query("SELECT DISTINCT a FROM Authorship a WHERE a.pk.pub.pubId = ?1")
    Set<Authorship> findByPubPubId(int pubId);
}
