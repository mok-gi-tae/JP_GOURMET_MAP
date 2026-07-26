package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.user.User;
import com.gitae.jpgourmetmap.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // 테스트 할 기능
    // 유저 객체 새로 하나 만들어져서 DB 저장
    // 저장된 내용이 잘 조회가 되는가?
    @Test
    void 이메일로_유저_조회하기() {
        User user = new User(
                "gitae@example.com",
                "1234",
                "gita"
        );
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("gitae@example.com");

        assertThat(result).isPresent();

        User foundUser = result.get();
        assertThat(foundUser.getEmail()).isEqualTo("gitae@example.com");
        assertThat(foundUser.getNickname()).isEqualTo("gita");
        assertThat(foundUser.getPassword()).isEqualTo("1234");

    }

    @Test
    void 등록된_이메일_맞는지_확인() {
        User user = new User(
                "gitae@example.com",
                "1234",
                "gita"
        );
        userRepository.save(user);

        boolean result1 = userRepository.existsByEmail("gitae@example.com");
        boolean result2 = userRepository.existsByEmail("none@example.com");

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void 닉네임_존재하는지_확인(){
        User user = new User(
                "gitae@example.com",
                "1234",
                "gita"
        );
        userRepository.save(user);

        boolean result1 = userRepository.existsByNickname("gita");
        boolean result2 = userRepository.existsByNickname("none");

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

}
