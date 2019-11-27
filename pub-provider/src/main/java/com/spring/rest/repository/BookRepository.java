package com.spring.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}

