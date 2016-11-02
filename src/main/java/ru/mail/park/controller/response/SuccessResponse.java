package ru.mail.park.controller.response;

public class SuccessResponse {
    private String response;

    public SuccessResponse(String response) {
        this.response = response;
    }

    @SuppressWarnings("unused")
    public String getResponse() {
        return response;
    }
}