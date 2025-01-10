package de.conet.isd.skima.skimabackend.service.skilltopics;

import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface SkillTopicRepo extends JpaRepository<SkillTopic, Long> {

  boolean existsByOwnerIdAndId(long ownerId, long id);

  @Query("select s from SkillTopic s left join fetch s.certifications where s.owner.id = :ownerId")
  List<SkillTopic> findByOwnerFetchDetails(long ownerId);

  @EntityGraph(attributePaths = {"certifications"})
  SkillTopic findByOwnerIdAndId(long ownerId, long id);

}
