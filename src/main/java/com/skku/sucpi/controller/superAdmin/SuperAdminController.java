package com.skku.sucpi.controller.superAdmin;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.activity.ActivityListRequestDto;
import com.skku.sucpi.dto.activity.ActivityRequestDto;
import com.skku.sucpi.dto.category.RatioRequestDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.dto.project.CreateProjectRequestDto;
import com.skku.sucpi.dto.project.ProjectActivityRulePatchDto;
import com.skku.sucpi.dto.project.UpdateProjectRequestDto;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.entity.Project;
import com.skku.sucpi.entity.ProjectActivityRule;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.project.ProjectService;
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
    private final ProjectService projectService;

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

    @PostMapping("/projects")
    @Operation(
            summary = "프로젝트 및 활동 규칙 생성",
            description = """
                    **설명**
                    - 새로운 프로젝트를 생성하고 연관된 활동별 사용자 정의 가중치(규칙)를 함께 저장하는 API
                    
                    **사용법**
                    - Method : POST
                    - Path : /api/super-admin/projects
                    
                    **Request Body**
                    - projectName (String, required)
                    - startDate (LocalDateTime, optional)
                    - endDate (LocalDateTime, optional)
                    - rules (List<RuleRequest>, optional) : 활동 ID 및 커스텀 가중치 리스트
                    """
    )
    public ApiResponse<Long> createProject(
            @Valid @RequestBody CreateProjectRequestDto dto,
            HttpServletRequest request
    ) {
        Long projectId = projectService.createProject(dto);
        return ApiResponse.success(projectId, request.getRequestURI());
    }

    @PatchMapping("projects/rules")
    @Operation(
            summary = "프로젝트 활동 규칙 수정",
            description = """
                    **설명**
                    - 특정 프로젝트에 설정된 기존 활동 가중치(규칙)를 초기화하고 새로운 값으로 갱신하는  API
                    
                    **사용법**
                    - Method : PATCH
                    - Path : /api/super-admin/projects/rules
                    
                    **Request Body**
                    - projectId (Long, required)
                    - rules (List<RuleRequest>, required) : 갱신할 활동 ID 및 가중치 리스트
                    """
    )
    public ApiResponse<Void> patchProjectRules(
            @Valid @RequestBody ProjectActivityRulePatchDto dto,
            HttpServletRequest request
            ) {
        projectService.patchProjectRules(dto);
        return ApiResponse.success(null, request.getRequestURI());
    }

    @PutMapping("/projects/{id}")
    @Operation(
            summary = "프로젝트 수정",
            description = """
                    **설명**
                    - 특정 프로젝트에 설정된 기존 활동 가중치(규칙)를 초기화하고 새로운 값으로 갱신하는  API
                    
                    **사용법**
                    - Method : PATCH
                    - Path : /api/super-admin/projects
                    
                    **Request Body**
                    - projectId (Long, required)
                    - Multiplier (Double, required) : 프로젝트 가중치
                    """
    )
    public ResponseEntity<Void> updateProject(
            @PathVariable("id") Long projectId,
            @RequestBody UpdateProjectRequestDto dto) {
        projectService.updateProject(projectId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/projects/{projectId}")
    @Operation(
            summary = "프로젝트 삭제",
            description = """
                    **설명**
                    - 프로젝트 및 해당 프로젝트와 연관된 모든 활동 규칙을 삭제하는 API
                    - 주의: 해당 프로젝트에 속한 팀이 존재할 경우 외래키 제약 조건 오류가 발생할 수 있습니다.
                    
                    **사용법**
                    - Method : DELETE
                    - Path : /api/super-admin/projects/{projectId}
                    """
    )
    public ApiResponse<Void> deleteProject(
            @PathVariable Long projectId,
            HttpServletRequest request
    ) {
        projectService.deleteProject(projectId);
        return ApiResponse.success(null, request.getRequestURI());
    }
}
