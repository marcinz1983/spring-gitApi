package com.gitapi.springgitapi.DTO;

import lombok.Getter;

@Getter
public final class ExceptionResponse {

    private int status;
    private String message;

    public ExceptionResponse(int responseStatus, String message) {
        this.status = responseStatus;
        this.message = message;
    }

}
