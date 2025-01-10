package de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto;

import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.TrainingActivity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TrainingActivityDtoMapper {

    public TrainingActivity toEntity(TrainingActivityDto dto, SkillTopic parent) {
        TrainingActivity entity = new TrainingActivity();

        entity.setTitle(dto.getTitle());
        entity.setSkillTopic(parent);
        entity.setMetric(dto.getMetric());

        entity.getSubTasks().clear();
        entity.getSubTasks().addAll(dto.getSubTasks());

        entity.setTargetAmount(dto.getTargetAmount());

        return entity;
    }

    public TrainingActivityDto toDto(TrainingActivity activity) {
        TrainingActivityDto dto = new TrainingActivityDto();
        dto.setId(activity.getId());
        dto.setTitle(activity.getTitle());
        dto.setMetric(activity.getMetric());
        dto.setTargetAmount(activity.getTargetAmount());
        dto.setSubTasks(new ArrayList<>(activity.getSubTasks()));
        return dto;
    }
}
