package com.example.bookrediscache.controller;

import com.example.bookrediscache.mapper.BookMapper;
import com.example.bookrediscache.model.dto.BookListResponse;
import com.example.bookrediscache.model.dto.BookResponse;
import com.example.bookrediscache.model.dto.ErrorResponse;
import com.example.bookrediscache.model.dto.UpsertBookRequest;
import com.example.bookrediscache.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestController
@RequestMapping("/bookshelf")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService service;
    private final BookMapper mapper;

    @GetMapping
    public ResponseEntity<BookListResponse> findAll(){       ;

        return ResponseEntity.ok(
                mapper.bookListToListResponse(
                        service.findAll()));
    }

    @GetMapping("/{title}/{author}")
    public ResponseEntity<BookResponse> findByTitleAndAuthor(@PathVariable String title, @PathVariable String author){

        return ResponseEntity.ok(
                mapper.bookToResponse(
                        service.findByTitleAndAuthor(title, author)));
    }
    @GetMapping("/{categoryName}")
    public ResponseEntity<BookListResponse> findAllByCategoryName(@PathVariable String categoryName){       ;

        return ResponseEntity.ok(
                mapper.bookListToListResponse(
                service.findAllByCategoryName(categoryName)));
    }




    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody UpsertBookRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(
                mapper.bookToResponse(
                        service.create(
                                mapper.requestToBook(request))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody UpsertBookRequest request){



        return ResponseEntity.ok(
                mapper.bookToResponse(
                        service.update(mapper.requestToBook(id, request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handler(EntityNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getLocalizedMessage()));
    }
}
