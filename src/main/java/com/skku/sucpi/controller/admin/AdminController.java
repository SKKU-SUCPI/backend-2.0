package com.skku.sucpi.controller.admin;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.activity.ActivityStatsDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.dto.comment.CommentUpdateDto;
import com.skku.sucpi.dto.score.ScoreAverageDto;
import com.skku.sucpi.dto.score.ScoreDepartmentAverageDto;
import com.skku.sucpi.dto.submit.SubmitCommentDto;
import com.skku.sucpi.dto.submit.SubmitCountDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.entity.FileStorage;
import com.skku.sucpi.service.activity.ActivityService;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.fileStorage.FileStorageService;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.service.submit.SubmitService;
import com.skku.sucpi.service.user.UserService;
import com.skku.sucpi.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.StringTokenizer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin API", description = "관리자 전용 기능을 제공하는 API")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final UserService userService;
    private final SubmitService submitService;
    private final FileStorageService fileStorageService;
    private final ScoreService scoreService;
    private final JWTUtil jwtUtil;


    @GetMapping("/ratio")
    @Operation(
            summary = "RQ, LQ, CQ 비율 조회",
            description = """
                    **설명**
                    - RQ, LQ, CQ 비율을 조회하는 API
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/ratio
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    """
    )
    public ApiResponse<RatioResponseDto> getAllRatio(HttpServletRequest request) {
        return ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI());
    }



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



    @GetMapping("/students")
    @Operation(
            summary = "학생 목록 조회",
            description = """
                    **설명**
                    - 학생 목록을 조회하는 API
                    - Pagination 적용
                    - 필터링 : 학생 이름, 학과, 학번
                    - 정렬 : 3Q 오름차순/내림차순 (다중 가능)
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/students?department={department}&page={page}&sort={sort}&sort={sort}
                
                    **헤더**
                    - Authorization: Bearer {accessToken}
                
                    **Query Parameter**
                    - name (String, not required) : 학생 이름
                    - department (String, not required) : 소프트웨어학과, 지능형소프트웨어학과, 글로벌융합학과
                    - studentId (String, not required) : 학번
                    - size (Integer, not required) : 한 페이지 당 개수 (default = 20)
                    - page (Integer, not required) : 페이지 번호 (default = 0, 첫 페이지 = 0)
                    - sort (String, not required) : lqScore,desc / lqScore,asc / rqScore,desc / rqScore,asc / cqScore,desc / cqScore,asc (default = id 오름차순)
                    """
    )
    public ApiResponse<PaginationDto<StudentDto.BasicInfo>> getStudents(
            @RequestParam(required = false) String name,        // 검색 (이름)
            @RequestParam(required = false) String department,  // 필터 (학과)
            @RequestParam(required = false) String studentId,   // 필터 (학번)
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request
    ) {
        PaginationDto<StudentDto.BasicInfo> result = userService.searchStudentsList(
                name,
                department,
                studentId,
                pageable
        );
        return ApiResponse.success(result, request.getRequestURI());
    }



    @GetMapping("/student/{id}")
    @Operation(
            summary = "학생 상세 정보 조회",
            description = """
                    **설명**
                    - 특정 학생의 상세 정보를 조회하는 API
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/student/{id}
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Path Variable**
                    - id (Long, required) : 학생의 고유 아이디
                    """
    )
    public ApiResponse<StudentDto.DetailInfo> getStudent(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        return ApiResponse.success(userService.searchStudentInfo(id), request.getRequestURI());
    }



    @GetMapping("/submits")
    @Operation(
            summary = "제출 내역 목록 조회",
            description = """
                **설명**
                - 모든 제출 내역을 조회하는 API
                - Pagination 적용
                - 필터링 : 승인 여부, 학생 이름
                - 정렬 : 제출날짜 오름차순/내림차순
            
                **사용법**
                - Method : GET
                - Path : /api/admin/submits?state={state}&page={page}&size={size}&sort=submitDate,desc&name={name}
            
                **Header**
                - Authorization: Bearer {accessToken}
            
                **Query Parameter**
                - name (String, not required) : 학생 이름
                - state (Integer, not required) : 0=미승인, 1=승인, 2=반려
                - size (Integer, not required) : 한 페이지 당 개수 (default = 20)
                - page (Integer, not required) : 페이지 번호 (default = 0, 첫 페이지 = 0)
                - sort (String, not required) : submitDate,desc(default) / submitDate,asc
                """
    )
    public ApiResponse<PaginationDto<SubmitDto.ListInfo>> getSubmits(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer state,
            @PageableDefault(size = 20, sort = "submitDate", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request
    ) {
        return ApiResponse.success(submitService.searchSubmitList(name, state, pageable), request.getRequestURI());
    }



    @GetMapping("/submit/{id}")
    @Operation(
            summary = "제출 내역 상세 조회",
            description = """
                    **설명**
                    - 특정 제출 내역의 상세 정보를 조회하는 API
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/submit/{id}
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Path Variable**
                    - id (Long, required) : 제출 내역의 고유 아이디
                    """
    )
    public ApiResponse<SubmitDto.DetailInfo> getSubmitDetailInfo(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        return ApiResponse.success(submitService.getSubmitDetailInfoById(id), request.getRequestURI());
    }



    @PostMapping("/submit/state")
    @Operation(
            summary = "제출 상태 변경",
            description = """
                    **설명**
                    - 제출 내역 상태를 변경하는 API

                    **사용법**
                    - Method : POST
                    - Path : /api/admin/submit/state

                    **헤더**
                    - Authorization: Bearer {accessToken}

                    **Request Body**
                    - id (Long, required) : 제출 내역의 고유 아이디
                    - state (Integer, required) : 0=미승인(대기), 1=승인, 2=반려
                    """
    )
    public ApiResponse<SubmitStateDto.Response> updateSubmitState(
            @RequestBody SubmitStateDto.Request request,
            HttpServletRequest r
            ) {
        return ApiResponse.success(submitService.updateSubmitState(request), r.getRequestURI());
    }

    @DeleteMapping("/submit/{id}")
    @Operation(
            summary = "제출 내역 삭제",
            description = """
                    **설명**
                    - 제출 내역을 삭제하는 API
                    
                    **사용법**
                    - Method : DELETE
                    - Path : /api/admin/submit/{id}
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Path Variable**
                    - id (Long, required) : 제출 내역의 고유 아이디
                    """
    )
    public ApiResponse<Void> deleteSubmit(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        submitService.deleteSubmitForAdmin(id);
        return ApiResponse.success(null, r.getRequestURI());
    }



    @PostMapping("/submit/comment")
    @Operation(
            summary = "제출 내역 댓글 작성",
            description = """
                    **설명**
                    - 제출 내역 상태를 변경하는 API

                    **사용법**
                    - Method : POST
                    - Path : /api/admin/submit/state

                    **헤더**
                    - Authorization: Bearer {accessToken}

                    **Request Body**
                    - id (Long, required) : 제출 내역의 고유 아이디
                    - comment (String, required) : 댓글 내용
                    """
    )
    public ApiResponse<SubmitCommentDto.Response> updateSubmitComment(
            @RequestBody SubmitCommentDto.Request request,
            HttpServletRequest r
    ) {
        String token = jwtUtil.parseJWT(r);
        Long userId = jwtUtil.getUserId(token);

        return ApiResponse.success(submitService.createSubmitComment(request, userId), r.getRequestURI());
    }



    @PatchMapping("/submit/comment")
    @Operation(
            summary = "제출 내역 댓글 수정",
            description = """
                    **설명**
                    - 제출 내역 댓글을 수정하는 API
                    
                    **사용법**
                    - Method : PATCH
                    - Path : /api/admin/submit/comment/{id}
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Request Body**
                    - id (Long, required) : 댓글의 고유 아이디
                    - content (String, required) : 댓글 내용
                    """
    )
    public ApiResponse<CommentUpdateDto.Response> updateSubmitComment(
            @RequestBody CommentUpdateDto.Request request,
            HttpServletRequest r
    ) {
        String token = jwtUtil.parseJWT(r);
        Long userId = jwtUtil.getUserId(token);

        return ApiResponse.success(submitService.updateSubmitComment(request, userId), r.getRequestURI());
    }



    @DeleteMapping("/submit/comment/{id}")
    @Operation(
            summary = "제출 내역 댓글 삭제",
            description = """
                    **설명**
                    - 제출 내역 댓글을 삭제하는 API
                    
                    **사용법**
                    - Method : DELETE
                    - Path : /api/admin/submit/comment/{id}
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Path Variable**
                    - id (Long, required) : 댓글의 고유 아이디
                    """
    )
    public ApiResponse<Void> deleteSubmitComment(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        String token = jwtUtil.parseJWT(r);
        Long userId = jwtUtil.getUserId(token);

        submitService.deleteSubmitComment(id, userId);

        return ApiResponse.success(null, r.getRequestURI());
    }



    @GetMapping("/files/{id}/download")
    @Operation(
            summary = "첨부 파일 다운로드",
            description = """
                    **설명**
                    - 특정 파일 다운로드
                    - 브라우저에서 다운로드
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/files/{id}/download
                    - Media Type : application/octet-stream
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Path Variable**
                    - id (Long, required) : 파일의 고유 아이디
                    """)
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



    @GetMapping("/3q-average")
    @Operation(
            summary = "전체 학생에 대한 3Q 평균",
            description = """
                    **설명**
                    - 전체 학생에 대한 3Q 평균을 조회하는 API
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/3q-average
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    """)
    public ApiResponse<ScoreAverageDto> get3QAverage(
            HttpServletRequest r
    ) {
        return ApiResponse.success(scoreService.get3QAverage(), r.getRequestURI());
    }



    @GetMapping("/3q-average/department")
    @Operation(
            summary = "학과별 3Q 평균",
            description = """
                    **설명**
                    - 학과별 3Q 평균을 조회하는 API
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/3q-average/department
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    """
    )
    public ApiResponse<ScoreDepartmentAverageDto> getDepartment3QAverage(
            HttpServletRequest r
    ) {
        return ApiResponse.success(scoreService.scoreDepartmentAverage(), r.getRequestURI());
    }



    @GetMapping("/submit/summary")
    @Operation(
            summary = "3Q의 총, 이번달, 저번달 활동 제출 내역 횟수",
            description = """
                    **설명**
                    - 3Q의 총, 이번달, 저번달 활동 제출 내역 횟수를 조회하는 API
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/submit/summary
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    """
    )
    public ApiResponse<SubmitCountDto.Response> countSubmissionsForThisAndLastMonth(
            HttpServletRequest r
    ) {
        return ApiResponse.success(submitService.countSubmissionsForThisAndLastMonth(), r.getRequestURI());
    }



    @GetMapping("submit-count/activity/{activityId}")
    @Operation(
            summary = "활동 별 제출 내역 횟수",
            description = """
                    **설명**
                    - 특정 활동에 대한 제출 내역 횟수를 조회하는 API
                    - 기간 설정 가능 (start, end)
                    
                    **사용법**
                    - Method : GET
                    - Path : /api/admin/submit-count/activity/{activityId}?start={start}&end={end}
                    
                    **헤더**
                    - Authorization: Bearer {accessToken}
                    
                    **Path Variable**
                    - activityId (Long, required) : 활동의 고유 아이디
                    
                    **Query Parameter**
                    - start (Instant, not required) : 조회 시작 날짜 (ISO 8601 포맷, 예: 2023-01-01T00:00:00Z), 미입력 시 2000-01-01로 설정
                    - end (Instant, not required) : 조회 종료 날짜 (ISO 8601 포맷, 예: 2023-12-31T23:59:59Z), 미입력 시 현재 날짜로 설정
                    """
    )
    public ApiResponse<ActivityStatsDto.SubmitCount> getSubmitCountByActivity(
            @PathVariable(value = "activityId") Long activityId,
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            Instant end,
            HttpServletRequest request
    ) throws Exception {
        ZoneId seoulZone = ZoneId.of("Asia/Seoul");

        LocalDate startDate = (start != null)
                ? start.atZone(seoulZone).toLocalDate()
                : LocalDate.of(2000, 1, 1);

        LocalDate endDate = (end != null)
                ? end.atZone(seoulZone).toLocalDate()
                : LocalDate.now(seoulZone);

        return ApiResponse.success(submitService.getSubmitCountByActivity(activityId, startDate, endDate), request.getRequestURI());
    };

}
