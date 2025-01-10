package de.conet.isd.skima.skimabackend.service.users;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<SkimaUser, Long> {
    /**
     * Fetches a single Benutzer with all collections required for authorization (Roles, Organizations, Departments,
     * ...)
     */
    @Query("select u from SkimaUser u where u.username=:username")
    SkimaUser findByUsernameFetchAuthorizationInfos(String username);

}
