package com.gitae.jpgourmetmap.domain.user;

import com.gitae.jpgourmetmap.domain.user.dto.LoginRequest;
import com.gitae.jpgourmetmap.domain.user.dto.SignUpRequest;
import com.gitae.jpgourmetmap.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public UserResponse signUp(SignUpRequest request) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        // 닉네임 중복 체크
        if (userRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        // 회원가입 시 비밀번호 인코딩
        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname()
        );
        return UserResponse.from(userRepository.save(user));
    }

    // 로그인
    @Transactional
    public UserResponse signIn(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return UserResponse.from(user);
    }

}
