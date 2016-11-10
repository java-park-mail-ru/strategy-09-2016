package ru.mail.park.controller.response;

public final class SuccessResponse {
    final private String response;

    public SuccessResponse(String response) {
        this.response = response;
    }

    @SuppressWarnings("unused")
    public String getResponse() {
        return response;
    }
}