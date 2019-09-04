package com.ned.simpledatajpaspringboot.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//public interface BookRepository extends org.springframework.data.repository.Repository<Book, Long> {
public interface BookRepository extends JpaRepository<Book, Long> {
}
//public interface BookRepository extends org.springframework.data.repository.Repository<Book, Long> {}
