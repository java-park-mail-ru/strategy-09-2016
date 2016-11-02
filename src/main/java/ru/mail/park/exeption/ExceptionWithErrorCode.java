package ru.mail.park.exeption;

public class ExceptionWithErrorCode extends Exception {

    private String errorCode;

    public ExceptionWithErrorCode(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return super.getMessage();
    }

}
