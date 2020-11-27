package com.spring.rest.repository;

import com.spring.rest.entities.UserDocumentation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDocumentationRepository extends JpaRepository<UserDocumentation, Integer> {

}

