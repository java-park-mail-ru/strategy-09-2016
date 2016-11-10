package ru.mail.park.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.mail.park.model.UserProfile;

import java.util.List;

public final class RatingSuccessResponse{ //Вопрос - объект этого класса возвращает метод bestRating
    private final List<UserProfile> body; // то есть, этот класс тоже должен быть public?

    public RatingSuccessResponse(List<UserProfile> body) { this.body = body; } // хотя по смыслу он private

    @JsonProperty("body")
    private List<UserProfile> getBody() {
        return body;
    }
}