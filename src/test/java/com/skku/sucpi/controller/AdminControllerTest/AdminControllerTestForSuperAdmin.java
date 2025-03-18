package com.skku.sucpi.controller.AdminControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminControllerTestForSuperAdmin {

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
        accessToken = jwtUtil.generateAccessToken("Test SuperAdmin", 1111111L, "super-admin");
    }

    @DisplayName("LQ, CQ, RQ 값 가져오기")
    @Test
    void getAllRatio() throws Exception {
        // given
        RatioResponseDto ratio = categoryService.getAllRatio();

        // when
        ResultActions result = mockMvc.perform(get("/admin/ratio")
                .header("Authorization", "Bearer " + accessToken));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cq").value(ratio.getCq()))
                .andExpect(jsonPath("$.data.lq").value(ratio.getLq()))
                .andExpect(jsonPath("$.data.rq").value(ratio.getRq()))
                .andExpect(jsonPath("$.path").value("/admin/ratio"));
    }
}
