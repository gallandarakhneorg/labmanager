package com.spring.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.Authorship;

public interface AuthorshipRepository extends JpaRepository<Authorship, Integer> {
	void deleteByAutAutIdAndPubPubId(int autId, int pubId);

	Optional<Authorship> findDistinctByAutAutIdAndPubPubId(int autId, int pubId);
}
