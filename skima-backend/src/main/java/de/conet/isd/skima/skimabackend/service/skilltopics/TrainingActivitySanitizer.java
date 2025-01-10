package de.conet.isd.skima.skimabackend.service.skilltopics;

import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.TrainingActivity;
import org.springframework.stereotype.Service;

@Service
public class TrainingActivitySanitizer {
    public TrainingActivity sanitize(TrainingActivity activity) {
        if (activity.getMetric().isRequiresTaskLists()) {
            activity.setTargetAmount(0);
        } else {
            activity.getSubTasks().clear();
        }

        return activity;
    }
}
