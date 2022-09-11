package com.gitapi.springgitapi.exceptions;

import com.gitapi.springgitapi.DTO.ExceptionResponse;
import com.gitapi.springgitapi.controller.GitHubRepositoriesController;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
@Getter
@Setter
@ToString
public class ApplicationExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GitHubRepositoriesController.class);

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(Exception exception) {
        logger.warn("User not found exception has occurred", exception);
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "GitHub user with this user name does not exist"
                )
        );
    }


    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class
            , org.springframework.web.bind.MissingServletRequestParameterException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(Exception exception) {
        logger.warn("Bad request exception has occurred", exception);
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Wrong query format, query requires a valid parameter"
                )
        );
    }
}