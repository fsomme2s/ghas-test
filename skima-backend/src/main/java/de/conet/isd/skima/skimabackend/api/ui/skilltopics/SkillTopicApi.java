package de.conet.isd.skima.skimabackend.api.ui.skilltopics;

import de.conet.isd.skima.skimabackend.api.ui._common.Paths;
import de.conet.isd.skima.skimabackend.api.ui._common.fetcher.CommonFetcher;
import de.conet.isd.skima.skimabackend.api.ui._common.security.CurrentUserInfo;
import de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.SkillTopicDto;
import de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.SkillTopicDtoMapper;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import de.conet.isd.skima.skimabackend.service.skilltopics.SkillTopicCreatorService;
import de.conet.isd.skima.skimabackend.service.skilltopics.SkillTopicRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.conet.isd.skima.skimabackend.api.ui._common.utils.ApiUtil.throwIfNotFound;
import static de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermissionConstants.MY_SKILL_TOPICS;

/**
 * API around {@link SkillTopic}
 */
@RestController
@RequestMapping(Paths.BASE_PATH + "skilltopics")
public class SkillTopicApi {

    private final SkillTopicRepo skillTopicRepo;
    private final CommonFetcher commonFetcher;
    private final SkillTopicDtoMapper dtoMapper;
    private final SkillTopicCreatorService skillTopicCreatorService;

    public SkillTopicApi(SkillTopicRepo skillTopicRepo, CommonFetcher commonFetcher, SkillTopicDtoMapper dtoMapper, SkillTopicCreatorService skillTopicCreatorService) {
        this.skillTopicRepo = skillTopicRepo;
        this.commonFetcher = commonFetcher;
        this.dtoMapper = dtoMapper;
        this.skillTopicCreatorService = skillTopicCreatorService;
    }

    @GetMapping
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('" + MY_SKILL_TOPICS + "')")
    public List<SkillTopicDto> getSkillTopics(CurrentUserInfo currentUser) {
        return skillTopicRepo.findByOwnerFetchDetails(currentUser.getId()).stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    @GetMapping("{skillTopicId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('" + MY_SKILL_TOPICS + "')")
    public SkillTopicDto getSkillTopicDetails(CurrentUserInfo currentUser, @PathVariable long skillTopicId) {
        SkillTopic skillTopic = throwIfNotFound(skillTopicRepo.findByOwnerIdAndId(currentUser.getId(), skillTopicId));
        return dtoMapper.toDto(skillTopic);
    }

    @PostMapping
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('" + MY_SKILL_TOPICS + "')")
    public SkillTopicDto createSkillTopic(@RequestBody SkillTopicDto dto, CurrentUserInfo currentUser) {
        SkillTopic skillTopic = dtoMapper.toEntity(dto, currentUser);

        skillTopic = skillTopicCreatorService.create(skillTopic);

        return dtoMapper.toDto(skillTopic);
    }



}
