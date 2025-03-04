package com.skku.sucpi.controller.auth;


import com.skku.sucpi.dto.user.SSOUserDto;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.service.auth.SSOService;
import com.skku.sucpi.service.user.UserService;
import com.skku.sucpi.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(
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
        SSOUserDto ssoUserDto = SSOUserDto
                .builder()
                .userName("신진건")
                .hakbun("2020310328")
                .role("student")
                .build();

        User user = userService.getOrCreateUser(ssoUserDto);
        log.info("유저 생성");

        /**
         * jwtService 사용
         * jwt 생성
         * role 구분
         */
        String accessToken = jwtUtil.generateAccessToken(user.getName(), user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getName());


        /**
         * Response
         * jwt, role, 필요한 유저 정보
         */
        addCookie(response, "accessToken", accessToken, 15 * 60);  // 15분
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("로그인 성공");
    }

    // test api
    @PostMapping("/login/student")
    public ResponseEntity<String> loginStudent(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        SSOUserDto ssoUserDto = SSOUserDto
                .builder()
                .userName("신진건")
                .hakbun("2020310328")
                .role("student")
                .build();

//        User user = userService.getOrCreateUser(ssoUserDto);
//
//        String accessToken = jwtUtil.generateAccessToken(user.getName(), user.getId(), user.getRole());
//        String refreshToken = jwtUtil.generateRefreshToken(user.getName());

        String accessToken = jwtUtil.generateAccessToken("신진건", 2020310328L, "student");
        String refreshToken = jwtUtil.generateRefreshToken("신진건");


        addCookie(response, "accessToken", accessToken, 15 * 60);  // 15분
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("student login success");
    }

    // test api
    @PostMapping("/login/admin")
    public ResponseEntity<String> loginAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        SSOUserDto ssoUserDto = SSOUserDto
                .builder()
                .userName("Admin")
                .hakbun("11111111")
                .role("admin")
                .build();

        User user = userService.getOrCreateUser(ssoUserDto);

        String accessToken = jwtUtil.generateAccessToken(user.getName(), user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getName());


        addCookie(response, "accessToken", accessToken, 15 * 60);  // 15분
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("admin login success");
    }

    // test api
    @PostMapping("/login/super-admin")
    public ResponseEntity<String> loginSuperAdmin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        SSOUserDto ssoUserDto = SSOUserDto
                .builder()
                .userName("SuperAdmin")
                .hakbun("00000000")
                .role("super-admin")
                .build();

        User user = userService.getOrCreateUser(ssoUserDto);

        String accessToken = jwtUtil.generateAccessToken(user.getName(), user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getName());


        addCookie(response, "accessToken", accessToken, 15 * 60);  // 15분
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("super-admin login success");
    }

    // 검증용 api
    @PostMapping("/student")
    @PreAuthorize("hasRole('student')")
    public String checkStudent() {
        return "학생입니다.";
    }

    // 검증용 api
    @PostMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String checkAdmin() {
        return "admin 입니다.";
    }

    // 검증용 api
    @PostMapping("/super-admin")
    @PreAuthorize("hasRole('super-admin')")
    public String checkSuperAdmin() {
        return "super-admin 입니다.";
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);  // JavaScript 접근 방지
        cookie.setSecure(true);  // HTTPS에서만 전송
        cookie.setPath("/");  // 모든 경로에서 사용 가능
        cookie.setMaxAge(maxAge);  // 쿠키 유효 기간 설정
        cookie.setAttribute("SameSite", "Strict"); // CSRF 방지

        response.addCookie(cookie);
    }

}
