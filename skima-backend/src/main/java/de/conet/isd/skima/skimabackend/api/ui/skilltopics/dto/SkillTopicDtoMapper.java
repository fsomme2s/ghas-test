package de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto;

import de.conet.isd.skima.skimabackend.api.ui._common.fetcher.CommonFetcher;
import de.conet.isd.skima.skimabackend.api.ui._common.security.CurrentUserInfo;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SkillTopicDtoMapper {

    private final CommonFetcher commonFetcher;

    public SkillTopicDtoMapper(CommonFetcher commonFetcher) {
        this.commonFetcher = commonFetcher;
    }

    public SkillTopic toEntity(SkillTopicDto dto, CurrentUserInfo currentUser) {
        SkimaUser user = commonFetcher.fetch(currentUser);

        SkillTopic entity = new SkillTopic();
        entity.setOwner(user);
        entity.setTitle(dto.getTitle());
        entity.getCertifications().clear();
        entity.getCertifications().addAll(dto.getCertifications());
        return entity;
    }

    public SkillTopicDto toDto(SkillTopic skillTopic) {
        SkillTopicDto savedDto = new SkillTopicDto();
        savedDto.setId(skillTopic.getId());
        savedDto.setTitle(skillTopic.getTitle());
        savedDto.setCertifications(new ArrayList<>(skillTopic.getCertifications()));
        return savedDto;
    }
}
