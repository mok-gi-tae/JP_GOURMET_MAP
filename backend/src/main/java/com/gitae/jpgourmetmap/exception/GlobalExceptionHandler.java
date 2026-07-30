package com.gitae.jpgourmetmap.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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
    // 맛집 없음 예외처리
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRestaurantNotFound (RestaurantNotFoundException e) {
        ErrorResponse response = ErrorResponse.of("RESTAURANT_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    // 리뷰 없음 예외처리
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReviewNotFound (ReviewNotFoundException e) {
        ErrorResponse response = ErrorResponse.of("REVIEW_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    // 리뷰 중복 작성 예외처리
    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateReview (DuplicateReviewException e) {
        ErrorResponse response = ErrorResponse.of("DUPLICATE_REVIEW", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    // 리뷰 소유권 예외처리
    @ExceptionHandler(ReviewAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleReviewAccessDenied (ReviewAccessDeniedException e) {
        ErrorResponse response = ErrorResponse.of("REVIEW_ACCESS_DENIED", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    // 회원가입 형식 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation (MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("잘못된 요청입니다.");
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
