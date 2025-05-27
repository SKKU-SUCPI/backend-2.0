package com.skku.sucpi.controller.auth;


import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skku.sucpi.dto.user.SSOUserDto;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.auth.SSOService;
import com.skku.sucpi.service.user.UserService;
import com.skku.sucpi.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SSOService ssoService;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/login")
    @Operation(summary = "SSO 로그인 API", description = "추후 개발 예정입니다....")
    public ResponseEntity<String> login(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        /**
         * ssoService 사용
         * pToken 없으면 SSO Login redirect
         * pToken 있으면 토큰 검증, User Info 가져오기
         */
//        response.sendRedirect("https://login.skku.edu" + "/?retUrl=");
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
        String refreshToken = jwtUtil.generateRefreshToken(user.getName(), user.getId());


        /**
         * Response
         * jwt, role, 필요한 유저 정보
         */
        response.addHeader("Authorization", "Bearer " + accessToken);
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("로그인 성공");
    }

    @GetMapping("/logout")
    @Operation(summary = "로그아웃 API")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/reissue")
    @Operation(summary = "AccessToken 재발급 API", description = "쿠키에 RefreshToken 이 포함되어야 합니다.")
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (!jwtUtil.isValidToken(c.getValue())) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                if (c.getName().equals("refreshToken")) {
                    Long userId = jwtUtil.getUserId(c.getValue());
                    Optional<User> user = userRepository.findById(userId);

                    if (user.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    }

                    User u = user.get();
                    String accessToken = jwtUtil.generateAccessToken(u.getName(), u.getId(), u.getRole());

                    response.addHeader("Authorization", "Bearer " + accessToken);
                }
            }
        }

        return ResponseEntity.ok("Reissue Successfully");
    }

    // 테스트용 학생 로그인 API (윤붰뤴, 2020919319)
    @PostMapping("/login/student-test")
    @Operation(summary = "학생 테스트 로그인 API", description = "윤붰뤴 학생으로 로그인하여 JWT 토큰(Access, Refresh)을 발급합니다.")
    public ResponseEntity<String> loginStudentTest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 테스트용 학생 정보를 설정 (이미 DB에 존재하면 해당 정보 사용, 없으면 새로 생성)
        SSOUserDto ssoUserDto = SSOUserDto.builder()
                .userName("윤붰뤴")
                .hakbun("2020919319")
                .role("student")
                .build();
        User user = userService.getOrCreateUser(ssoUserDto);

        // JWT 토큰 생성 (AccessToken: 15분, RefreshToken: 7일)
        String accessToken = jwtUtil.generateAccessToken(user.getName(), user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getName(), user.getId());

        // 응답 헤더에 AccessToken 설정
        response.addHeader("Authorization", "Bearer " + accessToken);
        // Cookie에 RefreshToken 설정 (HTTP 전용, Secure 설정 등 적용)
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);

        return ResponseEntity.ok("학생 테스트 로그인 성공");
    }



    // test api
    @PostMapping("/login/student")
    @Operation(summary = "개발용 student 로그인 API", description = "AccessToken, RefreshToken 을 응답합니다.")
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

        User user = userService.getOrCreateUser(ssoUserDto);

        String accessToken = jwtUtil.generateAccessToken(user.getName(), user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getName(), user.getId());


        response.addHeader("Authorization", "Bearer " + accessToken);
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("student login success");
    }

    // test api
    @PostMapping("/login/admin")
    @Operation(summary = "개발용 admin 로그인 API", description = "AccessToken, RefreshToken 을 응답합니다.")
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
        String refreshToken = jwtUtil.generateRefreshToken(user.getName(), user.getId());


        response.addHeader("Authorization", "Bearer " + accessToken);
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("admin login success");
    }

    // test api
    @PostMapping("/login/super-admin")
    @Operation(summary = "개발용 super-admin 로그인 API", description = "AccessToken, RefreshToken 을 응답합니다.")
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
        String refreshToken = jwtUtil.generateRefreshToken(user.getName(), user.getId());


        response.addHeader("Authorization", "Bearer " + accessToken);
        addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);  // 7일


        return ResponseEntity.ok("super-admin login success");
    }

    // 검증용 api
    @PostMapping("/student")
    @Operation(summary = "개발용 student 로그인 확인 API", description = "성공 여부를 응답합니다.")
    @PreAuthorize("hasRole('student')")
    public String checkStudent() {
        return "학생입니다.";
    }

    // 검증용 api
    @PostMapping("/admin")
    @Operation(summary = "개발용 admin 로그인 확인 API", description = "성공 여부를 응답합니다.")
    @PreAuthorize("hasRole('admin')")
    public String checkAdmin() {
        return "admin 입니다.";
    }

    // 검증용 api
    @PostMapping("/super-admin")
    @Operation(summary = "개발용 super-admin 로그인 확인 API", description = "성공 여부를 응답합니다.")
    @PreAuthorize("hasRole('super-admin')")
    public String checkSuperAdmin() {
        return "super-admin 입니다.";
    }


    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);  // JavaScript 접근 방지
//        cookie.setSecure(true);  // HTTPS에서만 전송
        cookie.setPath("/");  // 모든 경로에서 사용 가능
        cookie.setMaxAge(maxAge);  // 쿠키 유효 기간 설정
        cookie.setAttribute("SameSite", "Strict"); // CSRF 방지

        response.addCookie(cookie);
    }


}
