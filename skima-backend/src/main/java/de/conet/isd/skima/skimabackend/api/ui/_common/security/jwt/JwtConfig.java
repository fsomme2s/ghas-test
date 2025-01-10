package de.conet.isd.skima.skimabackend.api.ui._common.security.jwt;

import de.conet.isd.skima.skimabackend.service._common.error.SkimaConfigException;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.security.Key;

/**
 * @param secretKey                   Secret password that is long enough to suffice {@link Keys#hmacShaKeyFor(byte[])}
 *                                    (tested on local with 108 characters)
 * @param tokenExpirationAfterMinutes
 */
@ConfigurationProperties(prefix = "skima-backend.jwt")
public record JwtConfig(
        String secretKey,
        Long tokenExpirationAfterMinutes
) {
    public SecretKey getKey() {
        if (secretKey == null) throw new SkimaConfigException("jwt.secretKey is null");
        return Keys.hmacShaKeyFor(secretKey().getBytes());
    }
}
