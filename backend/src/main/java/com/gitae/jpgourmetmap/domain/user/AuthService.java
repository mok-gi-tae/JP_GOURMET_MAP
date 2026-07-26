package com.gitae.jpgourmetmap.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@EnableWebSecurity
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



}
