package com.skku.sucpi.controller.adminController;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final UserService userService;

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
            @RequestParam(defaultValue = "id") String sortBy,   // 정렬 기준 (기본: user id)
            @RequestParam(defaultValue = "asc") String direction, // 정렬 방향 (기본: asc)
            @RequestParam(defaultValue = "0") int page,         // 페이지 번호
            @RequestParam(defaultValue = "10") int size,         // 페이지 크기
            HttpServletRequest request
    ) {
        Page<StudentDto.basicInfo> result = userService.searchStudentsList(
                name,
                department,
                studentId,
                grade,
                sortBy,
                direction,
                page,
                size
        );

        return ResponseEntity.ok().body(ApiResponse.success(result, request.getRequestURI()));
    }

}
