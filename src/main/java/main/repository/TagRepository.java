package main.repository;

import java.util.List;
import main.model.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

  List<Tag> findAllByName(String name);

  List<Tag> findAll();
}
