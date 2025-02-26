package com.skku.sucpi.controller.auth;


import com.skku.sucpi.service.auth.SSOService;
import com.skku.sucpi.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final SSOService ssoService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        /**
         * ssoService 사용
         * pToken 없으면 SSO Login redirect
         * pToken 있으면 토큰 검증, User Info 가져오기
         */
//        response.sendRedirect("https://login.skku.edu" + "/?retUrl=12c46rw122r6dkenmr74");
//        return ResponseEntity.status(HttpStatus.FOUND).build();


        /**
         * userService 사용
         * 첫 로그인 : 유저 생성, User 엔티티 생성 (학번으로 일단 찾기)
         * 기존 유저 : User 엔티티 가져오기
         */
//        userService.getOrCreateUser(ssoUserDto);

        /**
         * jwtService 사용
         * jwt 생성
         * role 구분
         */

        /**
         * Response
         * jwt, role, 필요한 유저 정보
         */


        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

}
