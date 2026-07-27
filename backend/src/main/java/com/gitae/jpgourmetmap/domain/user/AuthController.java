package com.gitae.jpgourmetmap.domain.user;

import com.gitae.jpgourmetmap.domain.user.dto.LoginRequest;
import com.gitae.jpgourmetmap.domain.user.dto.SignUpRequest;
import com.gitae.jpgourmetmap.domain.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입 페이지
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        UserResponse response = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인 페이지
    @PostMapping("/login")
    public ResponseEntity<UserResponse> signIn(@Valid @RequestBody LoginRequest request) {
        UserResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

}
