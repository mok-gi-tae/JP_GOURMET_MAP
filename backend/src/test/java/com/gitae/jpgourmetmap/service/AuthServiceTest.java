package com.gitae.jpgourmetmap.service;

import com.gitae.jpgourmetmap.config.jwt.JwtTokenProvider;
import com.gitae.jpgourmetmap.domain.user.AuthService;
import com.gitae.jpgourmetmap.domain.user.User;
import com.gitae.jpgourmetmap.domain.user.UserRepository;
import com.gitae.jpgourmetmap.domain.user.dto.LoginRequest;
import com.gitae.jpgourmetmap.domain.user.dto.LoginResponse;
import com.gitae.jpgourmetmap.domain.user.dto.SignUpRequest;
import com.gitae.jpgourmetmap.domain.user.dto.UserResponse;
import com.gitae.jpgourmetmap.exception.DuplicateEmailException;
import com.gitae.jpgourmetmap.exception.DuplicateNicknameException;
import com.gitae.jpgourmetmap.exception.InvalidCredentialsException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void 회원가입_성공() {
        SignUpRequest request = new SignUpRequest("gitae@example.com", "password1", "gita");
        when(userRepository.existsByEmail("gitae@example.com")).thenReturn(false);
        when(userRepository.existsByNickname("gita")).thenReturn(false);
        when(passwordEncoder.encode("password1")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = authService.signUp(request);

        assertThat(response.email()).isEqualTo("gitae@example.com");
        assertThat(response.nickname()).isEqualTo("gita");
    }

    @Test
    void 회원가입_이메일_중복이면_예외() {
        SignUpRequest request = new SignUpRequest("gitae@example.com", "password1", "gita");
        when(userRepository.existsByEmail("gitae@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(DuplicateEmailException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void 회원가입_닉네임_중복이면_예외() {
        SignUpRequest request = new SignUpRequest("gitae@example.com", "password1", "gita");
        when(userRepository.existsByEmail("gitae@example.com")).thenReturn(false);
        when(userRepository.existsByNickname("gita")).thenReturn(true);

        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(DuplicateNicknameException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void 로그인_성공() {
        User user = new User("gitae@example.com", "encoded-password", "gita");
        LoginRequest request = new LoginRequest("gitae@example.com", "password1");
        when(userRepository.findByEmail("gitae@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password1", "encoded-password")).thenReturn(true);
        when(jwtTokenProvider.createToken(any())).thenReturn("access-token");

        LoginResponse response = authService.signIn(request);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.user().email()).isEqualTo("gitae@example.com");
    }

    @Test
    void 로그인_이메일_없으면_예외() {
        LoginRequest request = new LoginRequest("none@example.com", "password1");
        when(userRepository.findByEmail("none@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.signIn(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void 로그인_비밀번호_불일치하면_예외() {
        User user = new User("gitae@example.com", "encoded-password", "gita");
        LoginRequest request = new LoginRequest("gitae@example.com", "wrong-password");
        when(userRepository.findByEmail("gitae@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.signIn(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
