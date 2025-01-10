package de.conet.isd.skima.skimabackend.api.ui.login;

import de.conet.isd.skima.skimabackend.api.ui._common.Paths;
import de.conet.isd.skima.skimabackend.api.ui._common.fetcher.CommonFetcher;
import de.conet.isd.skima.skimabackend.api.ui._common.security.CurrentUserInfo;
import de.conet.isd.skima.skimabackend.api.ui._common.security.jwt.JwtLoginTokenFactory;
import de.conet.isd.skima.skimabackend.api.ui.login.dto.LoginDto;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = Paths.BASE_PATH)
public class LoginApi {

    private final JwtLoginTokenFactory tokenFactory;
    private final CommonFetcher commonFetcher;

    public LoginApi(JwtLoginTokenFactory tokenFactory, CommonFetcher commonFetcher) {
        this.tokenFactory = tokenFactory;
        this.commonFetcher = commonFetcher;
    }

    /**
     * @param loginDto
     * @return Returns the Login Token
     */
    @PostMapping(path = "login")
    // Security: Whitelisted Endpoint
    public String login(@RequestBody LoginDto loginDto) {
        return tokenFactory.attemptLogin(loginDto.getUsername(), loginDto.getSecret());
    }

    @PostMapping(path = "refresh-token")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public String refreshToken(CurrentUserInfo currentUser) {
        // TODO: this must check AuthProvider for a refresh, or a user could stay "logged in" via app token forever,
        //  even when he looses roles or gets deactivated in the primary authentication system.
        //  probably we can skip the whole refreshToken flow for the app token and make it a responsibility of
        //  the Authentication adapter, who can then login() after each refresh?
        SkimaUser user = commonFetcher.fetch(currentUser);
        Set<String> roles = currentUser.getRoles();
        return tokenFactory.createToken(user, roles);
    }

}
