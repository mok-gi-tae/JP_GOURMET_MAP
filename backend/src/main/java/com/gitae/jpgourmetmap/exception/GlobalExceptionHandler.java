package com.gitae.jpgourmetmap.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이메일 중복 예외처리
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail (DuplicateEmailException e) {
        ErrorResponse response = ErrorResponse.of("DUPLICATE_EMAIL", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    // 닉네임 중복 예외처리
    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateNickname (DuplicateNicknameException e) {
        ErrorResponse response = ErrorResponse.of("DUPLICATE_NICKNAME", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    // 로그인 실패 예외처리
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials (InvalidCredentialsException e) {
        ErrorResponse response = ErrorResponse.of("INVALID_CREDENTIALS", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    // 회원가입 형식 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation (MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(ErrorResponse.of("ARGUMENT_NOT_VALID", message));
    }
    // 예외처리 못한것들 나머지
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException (Exception e) {
        log.error("Unhandled exception", e);
        ErrorResponse response = ErrorResponse.of("INTERNAL_ERROR", "오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
