package com.lth.identify_service.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_EXISTS(1001, "User already exists"),
    USER_NAME_INVALID(1002, "username has to be between 5 and 20 characters"),
    PASSWORD_INVALID(1003, "password has to be between 8 and 20 characters"),
    INVALID_MESSAGE_KEY(1004, "Invalid message key"),
    USER_NOT_FOUND(404, "User not found"),
    UNAUTHENTICATED(401, "Unauthenticated"),
    ;
    int code;
    String message;
}
