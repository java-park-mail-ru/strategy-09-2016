package ru.mail.park.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class AuthorisationResponse {
    private final Boolean isAuthorized;
    private final String login;

    public AuthorisationResponse(Boolean status, String login) {
        this.isAuthorized = status;
        this.login = login;
    }
    @JsonProperty("isAuthorized")
    public Boolean getIsAuthorized() {
        return this.isAuthorized;
    }
    @JsonProperty("login")
    public String getLogin() {
        return this.login;
    }
}
