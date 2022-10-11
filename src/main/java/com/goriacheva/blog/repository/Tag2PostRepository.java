package com.goriacheva.blog.repository;

import com.goriacheva.blog.model.Tag2Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tag2PostRepository extends CrudRepository<Tag2Post, Integer> {


}
