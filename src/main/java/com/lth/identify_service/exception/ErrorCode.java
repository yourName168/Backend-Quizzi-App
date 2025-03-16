package com.lth.identify_service.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_EXISTS(1001, "User already exists", HttpStatus.CONFLICT),
    USER_NAME_INVALID(1002, "username has to be between 5 and 20 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "password has to be between 8 and 20 characters", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(1004, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "Unauthorized", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    ;
    int code;
    String message;
    HttpStatus statusCode;
}
