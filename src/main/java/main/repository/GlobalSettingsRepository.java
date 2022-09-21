package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {
  GlobalSettings findByCode(String code);

  @Override
  void deleteAll();
}
