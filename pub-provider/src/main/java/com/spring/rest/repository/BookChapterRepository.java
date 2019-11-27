package com.spring.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.BookChapter;

public interface BookChapterRepository extends JpaRepository<BookChapter, Integer> {

}

