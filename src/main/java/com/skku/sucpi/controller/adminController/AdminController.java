package com.skku.sucpi.controller.adminController;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.submit.SubmitService;
import com.skku.sucpi.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final UserService userService;
    private final SubmitService submitService;

    @GetMapping("/ratio")
    public ResponseEntity<ApiResponse<RatioResponseDto>> getAllRatio(HttpServletRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI()));
    }

    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<List<ActivityDto.Response>>> getAllActivities(HttpServletRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(activityService.getAllActivities(), request.getRequestURI()));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<Page<StudentDto.basicInfo>>> getStudents(
            @RequestParam(required = false) String name,        // 검색 (이름)
            @RequestParam(required = false) String department,  // 필터 (학과)
            @RequestParam(required = false) String studentId,   // 필터 (학번)
            @RequestParam(required = false) Integer grade,      // 필터 (학년)
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ) {
        Page<StudentDto.basicInfo> result = userService.searchStudentsList(
                name,
                department,
                studentId,
                grade,
                pageable
        );

        return ResponseEntity.ok().body(ApiResponse.success(result, request.getRequestURI()));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitStateDto.Response>> updateSubmitState(
            @RequestBody SubmitStateDto.Request request,
            HttpServletRequest r
            ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.updateSubmitState(request), r.getRequestURI()));
    }

}
