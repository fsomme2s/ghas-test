package de.conet.isd.skima.skimabackend.service.skilltopics;

import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.TrainingActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingActivityRepository extends JpaRepository<TrainingActivity, Long> {

  List<TrainingActivity> findBySkillTopicId(long skillTopicId);

  TrainingActivity findByIdAndSkillTopicIdAndOwnerId(long activityId, long skillTopicId, long ownerId);

}
