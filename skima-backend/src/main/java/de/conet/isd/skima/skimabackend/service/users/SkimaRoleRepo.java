package de.conet.isd.skima.skimabackend.service.users;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkimaRoleRepo extends JpaRepository<SkimaRole, Long> {
  @Query("select r from SkimaRole r")
  List<SkimaRole> findAllBy();
}
