package com.spring.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.UserDocumentation;

public interface UserDocumentationRepository extends JpaRepository<UserDocumentation, Integer> {

}

