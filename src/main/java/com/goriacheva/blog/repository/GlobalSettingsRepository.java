package com.goriacheva.blog.repository;

import com.goriacheva.blog.model.GlobalSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {
  GlobalSettings findByCode(String code);

  @Override
  void deleteAll();
}
