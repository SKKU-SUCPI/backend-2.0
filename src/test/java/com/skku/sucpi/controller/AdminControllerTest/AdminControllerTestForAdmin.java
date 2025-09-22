package com.skku.sucpi.controller.AdminControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminControllerTestForAdmin {

//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private JWTUtil jwtUtil;
//    @Autowired
//    private CategoryService categoryService;
//    @Autowired
//    private ActivityService activityService;
//
//    private String accessToken;
//
//    @BeforeEach
//    void setAccessToken() {
//        accessToken = jwtUtil.generateAccessToken("Test Admin", 22222222L, "admin");
//    }
//
//    @DisplayName("LQ, CQ, RQ 값 가져오기")
//    @Test
//    void getAllRatio() throws Exception {
//        // given
//        RatioResponseDto ratio = categoryService.getAllRatio();
//
//        // when
//        ResultActions result = mockMvc.perform(get("/admin/ratio")
//                .header("Authorization", "Bearer " + accessToken));
//
//        // then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.cq").value(ratio.getCq()))
//                .andExpect(jsonPath("$.data.lq").value(ratio.getLq()))
//                .andExpect(jsonPath("$.data.rq").value(ratio.getRq()))
//                .andExpect(jsonPath("$.path").value("/admin/ratio"));
//    }
//
//    @DisplayName("모든 activity 조회하기")
//    @Test
//    void getAllActivities() throws Exception {
//        // given
//        List<ActivityDto.Response> activities = activityService.getAllActivities();
//
//        // when
//        ResultActions result = mockMvc.perform(get("/admin/activities")
//                .header("Authorization", "Bearer " + accessToken));
//
//        // then
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data[0].activityName").value(activities.getFirst().getActivityName()))
//                .andExpect(jsonPath("$.data[0].activityDetail").value(activities.getFirst().getActivityDetail()))
//                .andExpect(jsonPath("$.data", hasSize(57)))
//                .andExpect(jsonPath("$.path").value("/admin/activities"));
//    }
}
