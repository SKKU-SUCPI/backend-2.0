package com.skku.sucpi.controller.AdminControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminControllerTestForAdmin {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private CategoryService categoryService;

    private String accessToken;

    @BeforeEach
    void setAccessToken() {
        accessToken = jwtUtil.generateAccessToken("Test Admin", 22222222L, "admin");
    }


}
