package de.conet.isd.skima.skimabackend.api.ui._common.security.jwt;

/**
 * Application Specific claims that our JWTs contain.
 */
public interface JwtClaims {
    /**
     * Id of the authenticated user
     */
    String USER_ID = "userId";

    /**
     * Roles that will be mapped to permissions. Note: never authorize directly against roles, but only
     * against derived permissions!
     */
    String ROLES = "roles";
}
