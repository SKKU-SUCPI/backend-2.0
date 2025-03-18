package com.skku.sucpi.service.activity;

import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.entity.Activity;
import com.skku.sucpi.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public List<ActivityDto.Response> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
                .map(ActivityDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

}
