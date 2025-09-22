package com.skku.sucpi.controller.common;


import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.service.activity.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
@Tag(name = "Common", description = "공통(Student, Admin, Super-Admin) API")
@SecurityRequirement(name = "bearerAuth")
public class CommonController {

    private final ActivityService activityService;

    @GetMapping("/activities")
    @Operation(
            summary = "모든 활동 조회",
            description = """
                    **설명**
                    - 모든 활동을 조회하는 API
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/activities
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    """
    )
    public ApiResponse<List<ActivityDto.Response>> getAllActivities(HttpServletRequest request) {
        return ApiResponse.success(activityService.getAllActivities(), request.getRequestURI());
    }
}
