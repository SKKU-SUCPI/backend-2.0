package com.skku.sucpi.service;

import com.skku.sucpi.dto.user.SSOUserDto;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

//    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
//
//    @DisplayName("모든 유저 조회하기")
//    @Test
//    void getAllUser() {
//        // given
////        User user1 = User.builder()
////                .name("홍길동")
////                .role("student")
////                .build();
////        User user2 = User.builder()
////                .name("김철수")
////                .role("admin")
////                .build();
//
////        userRepository.save(user1);
////        userRepository.save(user2);
////        log.info(String.valueOf(user1.getCreatedAt()));
//
//        // when
//        List<User> users = userService.getAllUsers();
//
//        // then
//        assertThat(users.get(1).getName()).isEqualTo("최츀뱺");
//        assertThat(users.size()).isGreaterThan(400);
//        assertThat(users.get(0).getRole()).isEqualTo("student");
//    }
//
//    @DisplayName("로그인 - 유저 조회하기")
//    @Test
//    void getUser() throws Exception {
//        // given
//        User user1 = User.builder()
//                .name("홍길동")
//                .role("student")
//                .hakbun("12312312")
//                .build();
//
//        userRepository.save(user1);
//        SSOUserDto ssoUserDto = SSOUserDto.builder()
//                .userName("홍길동")
//                .role("student")
//                .hakbun("12312312")
//                .build();
//
//        // when
//        User user = userService.getOrCreateUser(ssoUserDto);
//
//        // then
//        assertThat(user.getName()).isEqualTo("홍길동");
//        assertThat(user.getRole()).isEqualTo("student");
//        assertThat(user.getHakbun()).isEqualTo("12312312");
//    }
//
//    @DisplayName("로그인 - 유저 조회하기")
//    @Test
//    void createUser() throws Exception {
//        // given
//        User user1 = User.builder()
//                .name("홍길동")
//                .role("student")
//                .hakbun("11111111")
//                .build();
//
//        userRepository.save(user1);
//        SSOUserDto ssoUserDto = SSOUserDto.builder()
//                .userName("김철수")
//                .role("student")
//                .hakbun("22222222")
//                .build();
//
//        // when
//        User user = userService.getOrCreateUser(ssoUserDto);
//
//        // then
//        assertThat(user1.getName()).isEqualTo("홍길동");
//        assertThat(user.getName()).isEqualTo("김철수");
//        assertThat(user.getRole()).isEqualTo("student");
//        assertThat(user.getHakbun()).isEqualTo("22222222");
//    }
}
