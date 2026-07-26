package com.gitae.jpgourmetmap.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 이메일 닉네임 을 중복체크
    @Transactional

    public void emailCheck (SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }

    @Transactional
    public void nicknameCheck (SignUpRequest signUpRequest) {
        if (userRepository.existsByNickname(signUpRequest.nickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    // 비밀번호 인코딩
    // user 테이블에 저장



}
