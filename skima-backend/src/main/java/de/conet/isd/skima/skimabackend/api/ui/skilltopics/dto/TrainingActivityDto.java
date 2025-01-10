package de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto;

import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.ActivityMetric;
import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.TrainingActivity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Dto for {@link TrainingActivity}
 */
public class TrainingActivityDto {

    @Schema(nullable = true)
    private Long id;

    private String title = "";

    private ActivityMetric metric;

    /**
     * Only used if this.{@link #metric} does not {@link ActivityMetric#isRequiresTaskLists() require a task lists};
     * otherwise {@link #subTasks} is used.
     */
    private double targetAmount;

    /**
     * Only used if this.{@link #metric} does {@link ActivityMetric#isRequiresTaskLists() require a task lists};
     * otherwise {@link #targetAmount} is used.
     */
    private List<String> subTasks = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ActivityMetric getMetric() {
        return metric;
    }

    public void setMetric(ActivityMetric metric) {
        this.metric = metric;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public List<String> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<String> subTasks) {
        this.subTasks = subTasks;
    }
}
