package de.conet.isd.skima.skimabackend.domain.entities.users;

import de.conet.isd.skima.skimabackend.domain.entities._common.AbstractEntity;
import jakarta.persistence.Entity;

@Entity
public class SkimaUser extends AbstractEntity {

    private String username;
    private String firstname;
    private String lastname;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
