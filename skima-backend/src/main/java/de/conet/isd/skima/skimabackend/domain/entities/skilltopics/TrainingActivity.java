package de.conet.isd.skima.skimabackend.domain.entities.skilltopics;

import de.conet.isd.skima.skimabackend.domain.entities._common.AbstractEntity;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaUser;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * One To-Do for a concrete Measure to enhance a Skill, e.g. "Read Book X" or "Work through a list of web resources"
 * or "Complete Udemy Course Y"...
 */
@Entity
public class TrainingActivity extends AbstractEntity {

    private String title = "";

    @Column(length = 1000)
    private String description = "";

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
    @ElementCollection(fetch = FetchType.EAGER) // TODO LAZY (therefore: split ActivityDto into List and Detail Dto)
    @Column(length = 4000)
    private Set<String> subTasks = new LinkedHashSet<>();

    /**
     * Id of this.{@link #skillTopic}.{@link SkillTopic#getOwner() Owner}
     * Not supposed to be used in Java Model, but hold redundantly in Database for easier Data Querying.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private SkimaUser owner;

    @ManyToOne
    private SkillTopic skillTopic;

    protected SkimaUser getOwner() {
        return owner;
    }

    protected void setOwner(SkimaUser owner) {
        this.owner = owner;
    }

    public SkillTopic getSkillTopic() {
        return skillTopic;
    }

    public void setSkillTopic(SkillTopic skillTopic) {
        this.skillTopic = skillTopic;
        this.owner = this.skillTopic != null ? this.skillTopic.getOwner() : null;
    }

    //
    // Generated Getter/Setter
    //

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<String> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Set<String> subTasks) {
        this.subTasks = subTasks;
    }
}
