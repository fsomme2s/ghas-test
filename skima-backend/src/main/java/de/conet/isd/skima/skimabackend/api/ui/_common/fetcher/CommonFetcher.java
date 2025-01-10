package de.conet.isd.skima.skimabackend.api.ui._common.fetcher;

import de.conet.isd.skima.skimabackend.api.ui._common.security.CurrentUserInfo;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaUser;
import de.conet.isd.skima.skimabackend.service.users.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

/**
 * Used for often fetched objects that are used across several sub-domains / apis
 */
@Component
@RequestScope
public class CommonFetcher {
    private final Logger log = LoggerFactory.getLogger(CommonFetcher.class);

    private final UserRepo userRepo;

    public CommonFetcher(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public SkimaUser fetch(CurrentUserInfo currentUser) {
        return userRepo.findById(currentUser.getId()).orElseThrow(() -> {
            log.error("Currently Authenticated User {} not found in Database!", currentUser);
            return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        });
    }
}
