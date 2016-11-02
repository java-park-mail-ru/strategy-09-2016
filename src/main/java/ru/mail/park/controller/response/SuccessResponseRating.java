package ru.mail.park.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mail.park.model.UserProfile;

import java.util.List;

public final class SuccessResponseRating{
    final private List<UserProfile> body;

    public SuccessResponseRating(List<UserProfile> body) { this.body = body; }

    @JsonProperty("body")
    public List<UserProfile> getBody() {
        return body;
    }

}