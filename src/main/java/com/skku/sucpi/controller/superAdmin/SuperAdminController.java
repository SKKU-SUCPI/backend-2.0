package com.skku.sucpi.controller.superAdmin;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.activity.ActivityListRequestDto;
import com.skku.sucpi.dto.activity.ActivityRequestDto;
import com.skku.sucpi.dto.category.RatioRequestDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/super-admin")
public class SuperAdminController {

    private final CategoryService categoryService;
    private final ActivityService activityService;

//    @PutMapping("/ratio")
//    public ResponseEntity<ApiResponse<RatioResponseDto>> changeRatio(@Valid @RequestBody RatioRequestDto ratioRequestDto, HttpServletRequest request) {
//        categoryService.changeRatio(ratioRequestDto);
//        return ResponseEntity.ok().body(ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI()));
//    }

    @PostMapping("/ratio")
    @Operation(
            summary = "LQ, RQ, CQ 비율 수정",
            description = """
                    **설명**
                    - LQ, RQ, CQ 비율을 수정하는 API
                    - 비율의 합은 100이어야 함

                    **사용법**
                    - Method : POST
                    - Path : /api/super-admin/ratio
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Request Body**
                    - lq (Double, required) : LQ 비율
                    - rq (Double, required) : RQ 비율
                    - cq (Double, required) : CQ 비율
                    """
    )
    public ResponseEntity<ApiResponse<RatioResponseDto>> changeRatio(
            @Valid @RequestBody RatioRequestDto ratioRequestDto,
            HttpServletRequest request
    ) {
        categoryService.changeRatio(ratioRequestDto);
        return ResponseEntity.ok().body(ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI()));
    }

    @PostMapping("/activity")
    @Operation(
            summary = "활동 생성",
            description = """
                    **설명**
                    - 새로운 활동을 생성하는 API
                    
                    **사용법**
                    - Method : POST
                    - Path : /api/super-admin/activity
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Request Body**
                    - categoryId (Long, required) : 카테고리의 고유 아이디 (1=LQ, 2=RQ, 3=CQ)
                    - activityClass (String, required) : 활동 분류
                    - activityDetail (String, required) : 활동 상세 내용
                    - activityWeight (Double, required) : 활동 가중치
                    """
    )
    public ApiResponse<ActivityDto.Response> createActivity(
            @RequestBody ActivityRequestDto activityRequestDto,
            HttpServletRequest request
    ) {
        return ApiResponse.success(activityService.createActivity(activityRequestDto), request.getRequestURI());
    }

    @PostMapping("/activity/patch")
    @Operation(
            summary = "활동 수정",
            description = """
                    **설명**
                    - 기존 활동을 수정하는 API
                    - categoryId는 Request Body에 포함되지 않음
                    
                    **사용법**
                    - Method : POST
                    - Path : /api/super-admin/activity/patch
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Request Body**
                    - activities (List, required) : 수정할 활동 리스트
                        - activityId (Long, required) : 활동의 고유 아이디
                        - activityClass (String, nullable) : 활동 분류
                        - activityDetail (String, nullable) : 활동 상세 내용
                        - activityWeight (Double, nullable) : 활동 점수
                    """
    )
    public ApiResponse<Void> updateActivity(
            @RequestBody ActivityListRequestDto activities,
            HttpServletRequest request
    ) {
        activityService.updateActivity(activities.getActivities());

        return ApiResponse.success(null, request.getRequestURI());
    }


    @PostMapping("/activity/delete/{activityId}")
    @Operation(
            summary = "활동 삭제",
            description = """
                    **설명**
                    - 기존 활동을 삭제하는 API
                    - 활동과 관련된 제출물도 함께 삭제됨
                    
                    **사용법**
                    - Method : POST
                    - Path : /api/super-admin/activity/delete/{activityId}
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **경로 변수**
                    - activityId (Long, required) : 삭제할 활동의 고유 아이디
                    """
    )
    public ApiResponse<Void> deleteActivity(
            @PathVariable Long activityId,
            HttpServletRequest request
    ) {
        activityService.deleteActivity(activityId);
        return ApiResponse.success(null, request.getRequestURI());
    }
}
