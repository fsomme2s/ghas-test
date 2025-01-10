package de.conet.isd.skima.skimabackend.api.ui.skilltopics;

import de.conet.isd.skima.skimabackend.api.ui._common.Paths;
import de.conet.isd.skima.skimabackend.api.ui._common.security.CurrentUserInfo;
import de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.TrainingActivityDto;
import de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto.TrainingActivityDtoMapper;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.TrainingActivity;
import de.conet.isd.skima.skimabackend.service.skilltopics.SkillTopicRepo;
import de.conet.isd.skima.skimabackend.service.skilltopics.TrainingActivityCreatorService;
import de.conet.isd.skima.skimabackend.service.skilltopics.TrainingActivityRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.conet.isd.skima.skimabackend.api.ui._common.utils.ApiUtil.throwIfNotFound;
import static de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermissionConstants.MY_SKILL_TOPICS;

/**
 * Api around {@link TrainingActivity}
 */
@RestController
@RequestMapping(Paths.BASE_PATH + "skilltopics/{skillTopicId}/activities")
public class TrainingActivityApi {


    private final TrainingActivityRepository trainingActivityRepo;
    private final SkillTopicRepo skillTopicRepo;
    private final TrainingActivityDtoMapper dtoMapper;
    private final TrainingActivityCreatorService trainingActivityCreatorService;

    public TrainingActivityApi(TrainingActivityRepository trainingActivityRepo, SkillTopicRepo skillTopicRepo, TrainingActivityDtoMapper dtoMapper, TrainingActivityCreatorService trainingActivityCreatorService) {
        this.trainingActivityRepo = trainingActivityRepo;
        this.skillTopicRepo = skillTopicRepo;
        this.dtoMapper = dtoMapper;
        this.trainingActivityCreatorService = trainingActivityCreatorService;
    }

    @GetMapping
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('" + MY_SKILL_TOPICS + "')")
    public List<TrainingActivityDto> getActiviesOfSkillTopic(CurrentUserInfo currentUser, @PathVariable long skillTopicId) {
        // Check Ownership:
        throwIfNotFound(skillTopicRepo.existsByOwnerIdAndId(currentUser.getId(), skillTopicId));

        return trainingActivityRepo.findBySkillTopicId(skillTopicId).stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    /**
     * @return returns the created Activity
     */
    @PostMapping
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('" + MY_SKILL_TOPICS + "')")
    public TrainingActivityDto createActivity(
            @RequestBody TrainingActivityDto activityDto,
            @PathVariable long skillTopicId, CurrentUserInfo currentUser
    ) {
        // Check Ownership & Fetch Parent:
        SkillTopic skillTopic = throwIfNotFound(skillTopicRepo.findByOwnerIdAndId(currentUser.getId(), skillTopicId));

        TrainingActivity activity = dtoMapper.toEntity(activityDto, skillTopic);
        activity = trainingActivityCreatorService.create(activity);

        return dtoMapper.toDto(activity);
    }

    @DeleteMapping("{activityId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('" + MY_SKILL_TOPICS + "')")
    public void deleteActivy(CurrentUserInfo currentUser, @PathVariable long skillTopicId, @PathVariable long activityId) {
        // Check Ownership:
        TrainingActivity activity = throwIfNotFound(trainingActivityRepo.findByIdAndSkillTopicIdAndOwnerId(activityId, skillTopicId, currentUser.getId()));
        trainingActivityRepo.delete(activity);
    }
}
