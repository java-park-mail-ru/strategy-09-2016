package ru.mail.park.exeption;

/**
 * Created by victor on 26.10.16.
 */
public class CustomExeption extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public CustomExeption(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
