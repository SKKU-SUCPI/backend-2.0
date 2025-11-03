package com.skku.sucpi.service.activity;

import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.activity.ActivityRequestDto;
import com.skku.sucpi.entity.Activity;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.repository.ActivityRepository;
import com.skku.sucpi.repository.CategoryRepository;
import com.skku.sucpi.repository.ScoreRepository;
import com.skku.sucpi.repository.SubmitRepository;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.submit.SubmitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CategoryRepository categoryRepository;
    private final ScoreRepository scoreRepository;
    private final SubmitRepository submitRepository;

    private final SubmitService submitService;


    @Transactional(readOnly = true)
    public List<ActivityDto.Response> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(ActivityDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    public ActivityDto.Response createActivity(ActivityRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + requestDto.getCategoryId()));

        Activity activity = Activity.builder()
                .category(category)
                .activityClass(requestDto.getActivityClass())
                .detail(requestDto.getActivityDetail())
                .weight(requestDto.getActivityWeight())
                .domain(0L)
                .build();

        Activity savedActivity = activityRepository.save(activity);
        return ActivityDto.Response.fromEntity(savedActivity);
    }


    public void updateActivity(List<ActivityRequestDto> requestDto) {
        for (ActivityRequestDto dto : requestDto) {
            Activity activity = activityRepository.findById(dto.getActivityId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid activity ID: " + dto.getActivityId()));

            Category category = categoryRepository.findById(activity.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId()));

            // 활동 정보 업데이트
            activity.updateFromDto(dto, category);
        }

        // 모든 학생 점수 업데이트
        scoreRepository.updateAllScoresBasedOnSubmits();

        // 카테고리 테이블 통계 업데이트
        categoryRepository.calculateCategoryScores();
    }

    public void deleteActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid activity ID: " + activityId));

        // 관련 제출 삭제
        submitRepository.deleteAllByActivityId(activityId);

        // 활동 삭제
        activityRepository.delete(activity);

        // 모든 학생 점수 업데이트
        scoreRepository.updateAllScoresBasedOnSubmits();

        // 카테고리 테이블 통계 업데이트
        categoryRepository.calculateCategoryScores();
    }
}
