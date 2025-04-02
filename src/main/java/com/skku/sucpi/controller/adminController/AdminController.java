package com.skku.sucpi.controller.adminController;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.entity.FileStorage;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.fileStorage.FileStorageService;
import com.skku.sucpi.service.submit.SubmitService;
import com.skku.sucpi.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.StringTokenizer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final UserService userService;
    private final SubmitService submitService;
    private final FileStorageService fileStorageService;

    @GetMapping("/ratio")
    public ResponseEntity<ApiResponse<RatioResponseDto>> getAllRatio(HttpServletRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI()));
    }

    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<List<ActivityDto.Response>>> getAllActivities(HttpServletRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(activityService.getAllActivities(), request.getRequestURI()));
    }

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<PaginationDto<StudentDto.BasicInfo>>> getStudents(
            @RequestParam(required = false) String name,        // 검색 (이름)
            @RequestParam(required = false) String department,  // 필터 (학과)
            @RequestParam(required = false) String studentId,   // 필터 (학번)
            @RequestParam(required = false) Integer grade,      // 필터 (학년)
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ) {
        PaginationDto<StudentDto.BasicInfo> result = userService.searchStudentsList(
                name,
                department,
                studentId,
                grade,
                pageable
        );

        return ResponseEntity.ok().body(ApiResponse.success(result, request.getRequestURI()));
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<ApiResponse<StudentDto.DetailInfo>> getStudent(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(userService.searchStudentInfo(id), r.getRequestURI()));
    }

    @GetMapping("/submits")
    public ResponseEntity<ApiResponse<PaginationDto<SubmitDto.ListInfo>>> getSubmits(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer state,
            @PageableDefault(size = 20) Pageable pageable,
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.searchSubmitList(name, state, pageable), r.getRequestURI()));
    }

    @GetMapping("/submit/{id}")
    public ResponseEntity<ApiResponse<SubmitDto.DetailInfo>> getSubmitDetailInfo(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.getSubmitDetailInfoById(id), r.getRequestURI()));
    }

    @PostMapping("/submit/state")
    public ResponseEntity<ApiResponse<SubmitStateDto.Response>> updateSubmitState(
            @RequestBody SubmitStateDto.Request request,
            HttpServletRequest r
            ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.updateSubmitState(request), r.getRequestURI()));
    }

    @GetMapping("/files/{id}/download")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        FileStorage file = fileStorageService.getFileStorageById(id);

        String fileName = new StringTokenizer(file.getFileName(), ".").nextToken();
        String fileType = file.getFileType();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "." + fileType + "\"")
                .body(file.getFileDate());
    }
}
