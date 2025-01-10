package de.conet.isd.skima.skimabackend.service.skilltopics;

import br.com.fluentvalidator.AbstractValidator;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

import static br.com.fluentvalidator.predicate.StringPredicate.stringEmptyOrNull;

@Service
public class SkillTopicValidator extends AbstractValidator<SkillTopic> {
    @Override
    public void rules() {
        ruleFor(SkillTopic::getTitle)
                .must(Predicate.not(stringEmptyOrNull()))
                .withMessage("Must not be empty")
                .withFieldName("title");
    }
}
