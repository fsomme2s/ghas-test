package de.conet.isd.skima.skimabackend.service.skilltopics;

import br.com.fluentvalidator.context.ValidationResult;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import de.conet.isd.skima.skimabackend.service._common.error.BusinessException;
import de.conet.isd.skima.skimabackend.service._common.error.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static de.conet.isd.skima.skimabackend.utils.PostgresUtils.isUniqueConstraintViolation;

@Service
public class SkillTopicCreatorService {

    private final SkillTopicRepo skillTopicRepo;
    private final SkillTopicValidator skillTopicValidator;

    public SkillTopicCreatorService(SkillTopicRepo skillTopicRepo, SkillTopicValidator skillTopicValidator) {
        this.skillTopicRepo = skillTopicRepo;
        this.skillTopicValidator = skillTopicValidator;
    }

    @Transactional
    public SkillTopic create(SkillTopic skillTopic) {
        ValidationResult validationResult = skillTopicValidator.validate(skillTopic);
        if (!validationResult.isValid()) throw new ValidationException(validationResult);

        try {
            return skillTopicRepo.save(skillTopic);
        } catch (DataIntegrityViolationException e) {
            if (isUniqueConstraintViolation(e)) {
                throw new BusinessException(BusinessException.ERROR_CODE.ALREADY_EXISTS,
                        "SkillTopic with title %s already exists".formatted(skillTopic.getTitle()));
            }
            throw e;
        }
    }
}
