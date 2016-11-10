package ru.mail.park.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.mail.park.exeption.ExceptionWithErrorCode;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(ExceptionWithErrorCode.class)
    private ResponseEntity handleExeptionWithErrorCode(ExceptionWithErrorCode exceptionWithErrorCode){
        if(exceptionWithErrorCode.getErrorCode().equals("R03")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BadResponse(exceptionWithErrorCode.getErrorMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadResponse(exceptionWithErrorCode.getErrorMessage()));
        }
    }
/*
    @ExceptionHandler(Exception.class)
    private ResponseEntity handleAllExeption(){
        return ResponseEntity.ok(new BadResponse("ooops, something go wrong way"));
    }
*/
    private static final class BadResponse {
        private final String errorMessage;

        private BadResponse(String message) {
            this.errorMessage = message;
        }

        @JsonProperty("errorCode")
        private String getErrorMessage() {
            return errorMessage;
        }
    }
}

