package com.sharmel.minesweeper.exception;

import com.sharmel.minesweeper.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(NoSuchElementException exception) {
        return new ApiError(
                "GAME_NOT_FOUND",
                exception.getMessage(),
                Instant.now()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequest(IllegalArgumentException exception){
        return new ApiError(
                "INVALID_REQUEST",
                exception.getMessage(),
                Instant.now()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflict(IllegalStateException exception) {
        return new ApiError(
                "INVALID_GAME_STATE",
                exception.getMessage(),
                Instant.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError validation(MethodArgumentNotValidException exception) {
        return new ApiError(
                "VALIDATION_ERROR",
                exception.getMessage(),
                Instant.now()
        );
    }
}
