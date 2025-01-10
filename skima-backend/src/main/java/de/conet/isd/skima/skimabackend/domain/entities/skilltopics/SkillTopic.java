package de.conet.isd.skima.skimabackend.domain.entities.skilltopics;

import de.conet.isd.skima.skimabackend.domain.entities._common.AbstractEntity;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaUser;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Central entity of this App: the Skill, Subskill, or more general: "Topic", in which the user wants to get better.
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "skill_topic_title_per_owner_uk", columnNames = {"owner", "title"})})
public class SkillTopic extends AbstractEntity {

    @ManyToOne
    private SkimaUser owner;

    private String title = "";

    @ElementCollection
    private Set<String> certifications = new LinkedHashSet<>();

    public SkimaUser getOwner() {
        return owner;
    }

    public void setOwner(SkimaUser owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(Set<String> certifications) {
        this.certifications = certifications;
    }
}
