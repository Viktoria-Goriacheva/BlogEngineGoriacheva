package main.repository;

import java.util.List;
import java.util.Optional;
import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

  @Query(value = "select count(*),name from tags group by name order by count desc limit 1", nativeQuery = true)
  String findPopularTags();

  @Query(value = "SELECT name FROM tags WHERE name LIKE '%:pathtag%' group by name", nativeQuery = true)
  List<String> findAllByName(@Param("pathtag") String tagName);

  @Query(value = "select name from tags group by name", nativeQuery = true)
  List<String> findAllTags();

  @Query(value = "select count(*) from tags", nativeQuery = true)
  Integer findCountAllTags();

  @Query(value = "select count(*) from tags where name=:nameTag", nativeQuery = true)
  Integer findCountOneTags(String nameTag);

  Tag findFirstByName(String name);
  List<Tag> findTagsByNameIn(List<String> tags);
}
