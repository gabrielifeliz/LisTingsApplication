package com.example.listings;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long>{
    Iterable<Post> findAllByOrderByPublicationDateDesc();
    Iterable<Post> findAllByTitleContainingIgnoreCase(String s);
}
