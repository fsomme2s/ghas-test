package de.conet.isd.skima.skimabackend.service.skilltopics;

import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.TrainingActivity;
import de.conet.isd.skima.skimabackend.service._common.error.BusinessException;
import de.conet.isd.skima.skimabackend.service._common.error.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static de.conet.isd.skima.skimabackend.utils.PostgresUtils.isUniqueConstraintViolation;

@Service
public class TrainingActivityCreatorService {

    private final TrainingActivityRepository activityRepository;
    private final TrainingActivitySanitizer sanitizer;

    public TrainingActivityCreatorService(TrainingActivityRepository activityRepository, TrainingActivitySanitizer sanitizer) {
        this.activityRepository = activityRepository;
        this.sanitizer = sanitizer;
    }

    @Transactional
    public TrainingActivity create(TrainingActivity activity) {
        // todo validator.validate()
        sanitizer.sanitize(activity);
        return activityRepository.save(activity);
    }
}
