package com.skku.sucpi.controller.AdminControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.entity.*;
import com.skku.sucpi.repository.ActivityRepository;
import com.skku.sucpi.repository.ScoreRepository;
import com.skku.sucpi.repository.SubmitRepository;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AdminControllerTestForAdmin {

    private static final Logger log = LoggerFactory.getLogger(AdminControllerTestForAdmin.class);
    private String accessToken;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private SubmitRepository submitRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    @BeforeEach
    void setAccessToken() {
        accessToken = jwtUtil.generateAccessToken("Test Admin", 22222222L, "admin");
    }

    @DisplayName("LQ, CQ, RQ 값 가져오기")
    @Test
    void getAllRatio() throws Exception {
        // given
        RatioResponseDto ratio = categoryService.getAllRatio();

        // when
        ResultActions result = mockMvc.perform(get("/api/admin/ratio")
                .header("Authorization", "Bearer " + accessToken));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cq").value(ratio.getCq()))
                .andExpect(jsonPath("$.data.lq").value(ratio.getLq()))
                .andExpect(jsonPath("$.data.rq").value(ratio.getRq()))
                .andExpect(jsonPath("$.path").value("/api/admin/ratio"));
    }

    @DisplayName("모든 activity 조회하기")
    @Test
    void getAllActivities() throws Exception {
        // given
        List<ActivityDto.Response> activities = activityService.getAllActivities();

        // when
        ResultActions result = mockMvc.perform(get("/api/admin/activities")
                .header("Authorization", "Bearer " + accessToken));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].activityDetail").value(activities.getFirst().getActivityDetail()))
                .andExpect(jsonPath("$.data", hasSize(activities.size())))
                .andExpect(jsonPath("$.path").value("/api/admin/activities"));
    }

    @DisplayName("[Admin] 학생 활동 내역 대기, 반려를 승인하기")
    @Test
    void approveActivity_Admin() throws Exception {

        // given
        // 건진신 학생
        User student = userRepository.findByHakbun("12221222").orElseThrow();
        log.info("student.getName() = {}", student.getName());

        Score beforeScore = scoreRepository.findByUserId(student.getId()).orElseThrow();
        Double beforeLq = beforeScore.getLqScore();
        Double beforeRq = beforeScore.getRqScore();
        Double beforeCq = beforeScore.getCqScore();
        log.info("Before Score - LQ: {}, RQ: {}, CQ: {}", beforeLq, beforeRq, beforeCq);

        List<Category> beforeCategories = categoryService.getAllCategory();
        Double beforeLqSum = 0D;
        Double beforeRqSum = 0D;
        Double beforeCqSum = 0D;
        Double beforeLqSquareSum = 0D;
        Double beforeRqSquareSum = 0D;
        Double beforeCqSquareSum = 0D;
        for (Category category : beforeCategories) {
            switch (category.getName()) {
                case "LQ" -> {
                    beforeLqSum = category.getSumY();
                    beforeLqSquareSum = category.getSquareSumY();
                }
                case "RQ" -> {
                    beforeRqSum = category.getSumY();
                    beforeRqSquareSum = category.getSquareSumY();
                }
                case "CQ" -> {
                    beforeCqSum = category.getSumY();
                    beforeCqSquareSum = category.getSquareSumY();
                }
            }
        }

        // 임시 제출물 생성
        Activity activity = activityRepository.findAll().getFirst();
        Submit submit = Submit.builder()
                .user(student)
                .activity(activity)
                .title("테스트 코드 - 제목")
                .content("테스트 코드 - 내용")
                .state(0)
                .build();
        Submit saved = submitRepository.save(submit);
        log.info("Saved Submit ID: {}", saved.getId());

        // when
        SubmitStateDto.Request submitStateDto = new SubmitStateDto.Request();
        submitStateDto.setId(saved.getId());
        submitStateDto.setState(1);

        ResultActions result = mockMvc.perform(post("/api/admin/submit/state")
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(submitStateDto))
                .contentType("application/json")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.state").value(1))
                .andExpect(jsonPath("$.path").value("/api/admin/submit/state"));

        // 학생 점수
        Score afterScore = scoreRepository.findByUserId(student.getId()).orElseThrow();

        switch (activity.getCategory().getName()) {
            case "LQ" -> {
                beforeLq += activity.getWeight();
                beforeLqSum += activity.getWeight();
                beforeLqSquareSum += activity.getWeight() * activity.getWeight();
            }
            case "RQ" -> {
                beforeRq += activity.getWeight();
                beforeRqSum += activity.getWeight();
                beforeRqSquareSum += activity.getWeight() * activity.getWeight();
            }
            case "CQ" -> {
                beforeCq += activity.getWeight();
                beforeCqSum += activity.getWeight();
                beforeCqSquareSum += activity.getWeight() * activity.getWeight();
            }
        }
        log.info("After Score - LQ: {}, RQ: {}, CQ: {}", afterScore.getLqScore(), afterScore.getRqScore(), afterScore.getCqScore());

        assertThat(afterScore.getLqScore()).isEqualTo(beforeLq);
        assertThat(afterScore.getRqScore()).isEqualTo(beforeRq);
        assertThat(afterScore.getCqScore()).isEqualTo(beforeCq);

        // 통계 점수
        List<Category> afterCategories = categoryService.getAllCategory();
        Double afterLqSum = 0D;
        Double afterRqSum = 0D;
        Double afterCqSum = 0D;
        Double afterLqSquareSum = 0D;
        Double afterRqSquareSum = 0D;
        Double afterCqSquareSum = 0D;
        for (Category category : afterCategories) {
            switch (category.getName()) {
                case "LQ" -> {
                    afterLqSum = category.getSumY();
                    afterLqSquareSum = category.getSquareSumY();
                }
                case "RQ" -> {
                    afterRqSum = category.getSumY();
                    afterRqSquareSum = category.getSquareSumY();
                }
                case "CQ" -> {
                    afterCqSum = category.getSumY();
                    afterCqSquareSum = category.getSquareSumY();
                }
            }
        }

        assertThat(afterLqSum).isEqualTo(beforeLqSum);
        assertThat(afterRqSum).isEqualTo(beforeRqSum);
        assertThat(afterCqSum).isEqualTo(beforeCqSum);
        assertThat(afterLqSquareSum).isEqualTo(beforeLqSquareSum);
        assertThat(afterRqSquareSum).isEqualTo(beforeRqSquareSum);
        assertThat(afterCqSquareSum).isEqualTo(beforeCqSquareSum);

    }


    @DisplayName("[Admin] 학생 활동 내역 승인을 대기, 반려하기")
    @Test
    void refuseActivity_Admin() throws Exception {
        // given
        // 건진신 학생
        User student = userRepository.findByHakbun("12221222").orElseThrow();

        // 임시 제출물 생성
        Activity activity = activityRepository.findAll().getFirst();
        Submit submit = Submit.builder()
                .user(student)
                .activity(activity)
                .title("테스트 코드 - 제목")
                .content("테스트 코드 - 내용")
                .state(0)
                .build();
        Submit saved = submitRepository.save(submit);
        SubmitStateDto.Request submitStateDto = new SubmitStateDto.Request();
        submitStateDto.setId(saved.getId());
        submitStateDto.setState(1);

        ResultActions r = mockMvc.perform(post("/api/admin/submit/state")
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(submitStateDto))
                .contentType("application/json")
        );

        Score beforeScore = scoreRepository.findByUserId(student.getId()).orElseThrow();
        Double beforeLq = beforeScore.getLqScore();
        Double beforeRq = beforeScore.getRqScore();
        Double beforeCq = beforeScore.getCqScore();

        List<Category> beforeCategories = categoryService.getAllCategory();
        Double beforeLqSum = 0D;
        Double beforeRqSum = 0D;
        Double beforeCqSum = 0D;
        Double beforeLqSquareSum = 0D;
        Double beforeRqSquareSum = 0D;
        Double beforeCqSquareSum = 0D;
        for (Category category : beforeCategories) {
            switch (category.getName()) {
                case "LQ" -> {
                    beforeLqSum = category.getSumY();
                    beforeLqSquareSum = category.getSquareSumY();
                }
                case "RQ" -> {
                    beforeRqSum = category.getSumY();
                    beforeRqSquareSum = category.getSquareSumY();
                }
                case "CQ" -> {
                    beforeCqSum = category.getSumY();
                    beforeCqSquareSum = category.getSquareSumY();
                }
            }
        }



        // when
        SubmitStateDto.Request submitStateDto2 = new SubmitStateDto.Request();
        submitStateDto2.setId(saved.getId());
        submitStateDto2.setState(2); // 승인 -> 반려
        ResultActions result = mockMvc.perform(post("/api/admin/submit/state")
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(submitStateDto2))
                .contentType("application/json")
        );


        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(saved.getId()))
                .andExpect(jsonPath("$.data.state").value(2))
                .andExpect(jsonPath("$.path").value("/api/admin/submit/state"));

        Score afterScore = scoreRepository.findByUserId(student.getId()).orElseThrow();

        switch (activity.getCategory().getName()) {
            case "LQ" -> {
                beforeLq -= activity.getWeight();
                beforeLqSum -= activity.getWeight();
                beforeLqSquareSum -= activity.getWeight() * activity.getWeight();
            }
            case "RQ" -> {
                beforeRq -= activity.getWeight();
                beforeRqSum -= activity.getWeight();
                beforeRqSquareSum -= activity.getWeight() * activity.getWeight();
            }
            case "CQ" -> {
                beforeCq -= activity.getWeight();
                beforeCqSum -= activity.getWeight();
                beforeCqSquareSum -= activity.getWeight() * activity.getWeight();
            }
        }

        assertThat(afterScore.getLqScore()).isEqualTo(beforeLq);
        assertThat(afterScore.getRqScore()).isEqualTo(beforeRq);
        assertThat(afterScore.getCqScore()).isEqualTo(beforeCq);

        // 통계 점수
        List<Category> afterCategories = categoryService.getAllCategory();
        Double afterLqSum = 0D;
        Double afterRqSum = 0D;
        Double afterCqSum = 0D;
        Double afterLqSquareSum = 0D;
        Double afterRqSquareSum = 0D;
        Double afterCqSquareSum = 0D;
        for (Category category : afterCategories) {
            switch (category.getName()) {
                case "LQ" -> {
                    afterLqSum = category.getSumY();
                    afterLqSquareSum = category.getSquareSumY();
                }
                case "RQ" -> {
                    afterRqSum = category.getSumY();
                    afterRqSquareSum = category.getSquareSumY();
                }
                case "CQ" -> {
                    afterCqSum = category.getSumY();
                    afterCqSquareSum = category.getSquareSumY();
                }
            }
        }

        assertThat(afterLqSum).isEqualTo(beforeLqSum);
        assertThat(afterRqSum).isEqualTo(beforeRqSum);
        assertThat(afterCqSum).isEqualTo(beforeCqSum);
        assertThat(afterLqSquareSum).isEqualTo(beforeLqSquareSum);
        assertThat(afterRqSquareSum).isEqualTo(beforeRqSquareSum);
        assertThat(afterCqSquareSum).isEqualTo(beforeCqSquareSum);
    }

}
