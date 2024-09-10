package com.example.bookrediscache.repository;

import com.example.bookrediscache.model.Book;
import com.example.bookrediscache.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByCategory(Category category);

    Optional<Book> findByTitleAndAuthor(String title, String author);




}
