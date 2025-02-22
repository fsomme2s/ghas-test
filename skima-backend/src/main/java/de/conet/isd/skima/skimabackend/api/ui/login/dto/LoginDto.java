package de.conet.isd.skima.skimabackend.api.ui.login.dto;

/**
 * Send to backend for login
 */
public class LoginDto {
    private String username;
    private String secret;

    public LoginDto() {
    }

    public LoginDto(String username, String secret) {
        this.username = username;
        this.secret = secret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
