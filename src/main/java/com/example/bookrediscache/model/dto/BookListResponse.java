package com.example.bookrediscache.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookListResponse {

    private List<BookResponse> books;
}
