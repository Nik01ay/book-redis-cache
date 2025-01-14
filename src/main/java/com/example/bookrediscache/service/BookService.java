package com.example.bookrediscache.service;

import com.example.bookrediscache.model.Book;
import com.example.bookrediscache.model.Category;
import com.example.bookrediscache.repository.BookRepository;
import com.example.bookrediscache.util.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
//@CacheConfig()
public class BookService {

    private final BookRepository repository;
    private final CategoryService categoryService;

    @Cacheable("allByCategoryName")
    public List<Book> findAllByCategoryName(String categoryName){
        Category category = categoryService.findByName(categoryName)
                .orElseThrow((() ->
                new EntityNotFoundException(MessageFormat.format("Категория {0} не найдена", categoryName))));

        return repository.findAllByCategory(category);
    }


    @Cacheable(cacheNames = "oneByTitleAndAuthor", key = "#title + #author")
    public Book findByTitleAndAuthor(String title, String author){
        return repository.findByTitleAndAuthor(title, author).orElseThrow((() ->
                new EntityNotFoundException(MessageFormat.format("Книга {0} автора {1} не найдена", title, author))));
    }


    public Book findById(Long id){
        return repository.findById(id).orElseThrow((() ->
                new EntityNotFoundException(MessageFormat.format("Книга с ID {0} не найдена", id))));
    }



    @CacheEvict(value = {"allByCategoryName", "oneByTitleAndAuthor", "allBook"}, allEntries = true)
    public Book create(Book book){
        Book createdBook = new Book();

        if(repository.findByTitleAndAuthor(book.getTitle(), book.getAuthor()).isPresent()){
            book.setId(findByTitleAndAuthor(book.getTitle(), book.getAuthor()).getId());
            return update(book);
        }
        BeanUtils.nonNullFieldsCopy(book, createdBook);

        return repository.save(createdBook);
    }

    @CacheEvict(value = {"allByCategoryName", "oneByTitleAndAuthor", "allBook"}, allEntries = true)
    public Book update(Book book){
        Book updatedBook = findById(book.getId());

        BeanUtils.nonNullFieldsCopy(book, updatedBook);

        return repository.save(updatedBook);
    }

    @CacheEvict(value = {"allByCategoryName", "oneByTitleAndAuthor", "allBook"}, allEntries = true)
    public void deleteById(Long id){

        repository.delete(findById(id));
    }

    @Cacheable("allBook")
    public List<Book> findAll() {
        return  repository.findAll();
    }
}