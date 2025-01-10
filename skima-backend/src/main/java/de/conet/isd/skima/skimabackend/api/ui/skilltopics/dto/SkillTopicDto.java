package de.conet.isd.skima.skimabackend.api.ui.skilltopics.dto;

import de.conet.isd.skima.skimabackend.domain.entities.skilltopics.SkillTopic;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ElementCollection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Dto for {@link SkillTopic}
 */
public class SkillTopicDto {

    @Schema(nullable = true)
    private Long id;

    private String title = "";

    private List<String> certifications = new ArrayList<>();

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

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }
}
