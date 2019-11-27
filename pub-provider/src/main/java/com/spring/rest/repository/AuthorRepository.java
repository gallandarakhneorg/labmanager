package com.spring.rest.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
	Set<Author> findDistinctByAutPubsPubId(int pubId);

	Set<Author> findDistinctByAutOrgsResOrgResOrgId(int resOrgId);
}
