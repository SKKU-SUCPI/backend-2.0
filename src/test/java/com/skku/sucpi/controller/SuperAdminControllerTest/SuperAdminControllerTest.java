package com.skku.sucpi.controller.SuperAdminControllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.sucpi.dto.category.RatioRequestDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SuperAdminControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    private String accessToken;
//
//    @BeforeEach
//    void setAccessToken() {
//        accessToken = jwtUtil.generateAccessToken("Test SuperAdmin", 111111L, "super-admin");
//    }
//
//
//    @DisplayName("[성공] LQ, CQ, RQ 값 변경")
//    @Test
//    void changeRatio() throws Exception {
//        // given
//        RatioRequestDto requestDto = new RatioRequestDto();
//        requestDto.setCq(10.0D);
//        requestDto.setLq(20.0D);
//        requestDto.setRq(70.0D);
//
//        // when
//        ResultActions result = mockMvc.perform(post("/super-admin/ratio")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json(requestDto)));
//
//        // then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.cq").value(10.0D))
//                .andExpect(jsonPath("$.data.lq").value(20.0D))
//                .andExpect(jsonPath("$.data.rq").value(70.0D))
//                .andExpect(jsonPath("$.path").value("/super-admin/ratio"));
//
//    }
//
//    @DisplayName("[에러] LQ, CQ, RQ 합이 0이 아닐 때")
//    @Test
//    void changeRatioFail() throws Exception {
//        // given
//        RatioRequestDto requestDto = new RatioRequestDto();
//        requestDto.setCq(10.0D);
//        requestDto.setLq(20.0D);
//        requestDto.setRq(30.0D);
//
//        // when
//        ResultActions result = mockMvc.perform(post("/super-admin/ratio")
//                .header("Authorization", "Bearer " + accessToken)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json(requestDto)));
//
//        // then
//        result.andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.success").value(false));
//
//    }
//
//    private String json(Object object) throws JsonProcessingException {
//        return objectMapper.writeValueAsString(object);
//    }
}
