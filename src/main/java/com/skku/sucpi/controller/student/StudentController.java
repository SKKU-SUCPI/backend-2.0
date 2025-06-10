package com.skku.sucpi.controller.student;

import java.util.List;
import java.util.Optional;

import com.skku.sucpi.dto.score.MonthlyScoreDto;
import com.skku.sucpi.dto.score.StudentScoreAverageDto;
import com.skku.sucpi.dto.score.StudentScoreDto;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.service.score.ScoreSubmitService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.submit.SubmitCreateRequestDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.service.submit.SubmitService;
import com.skku.sucpi.service.user.UserService;
import com.skku.sucpi.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "Student API", description = "학생 전용 기능을 제공하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final SubmitService submitService;
    private final ScoreService scoreService;
    private final ScoreSubmitService scoreSubmitService;

    @Operation(
        summary = "내 프로필 조회",
        description = """
        **사용법**  
        GET /api/student/me  
        **헤더**  
        Authorization: Bearer {accessToken}  
        
        **응답 예시**  
        ```json
        {
          "success": true,
          "data": {
            "id": 123,
            "name": "홍길동",
            "studentId": "2020310000",
            "department": "소프트웨어학과",
            "grade": 3,
            "lq": 12.0,
            "rq": 8.0,
            "cq": 5.0,
            "tLq": 15.2,
            "tRq": 10.1,
            "tCq": 7.3,
            "totalScore": 25.0
          },
          "path": "/api/student/me"
        }
        ```
        """
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필 조회 성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StudentDto.BasicInfo.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('student')")
    public ApiResponse<StudentDto.BasicInfo> getMyProfile(
            HttpServletRequest request
    ) {
        String token = Optional.ofNullable(request.getHeader("Authorization"))
            .filter(h -> h.startsWith("Bearer "))
            .map(h -> h.substring(7))
            .orElseThrow(() -> new IllegalArgumentException("인증 토큰이 없습니다."));
        Long userId = jwtUtil.getUserId(token);

        StudentDto.DetailInfo detail = userService.searchStudentInfo(userId);
        return ApiResponse.success(detail.getBasicInfo(), request.getRequestURI());
    }

    @Operation(
        summary = "내 제출 내역 조회",
        description = """
        **사용법**  
        GET /api/student/submits?state={state}&page={page}&size={size}&sort=submitDate,desc  
        **헤더**  
        Authorization: Bearer {accessToken}  
        
        **쿼리 파라미터**  
        - state (선택) : 0=미승인, 1=승인, 2=거부  
        
        **응답 예시**  
        ```json
        {
          "success": true,
          "data": {
            "content": [
              {
                "id": 456,
                "submitDate": "2025-05-12T14:00:00",
                "state": 1,
                "approvedDate": "2025-05-13T10:00:00",
                "content": "과제 제출 내용",
                "activityId": 10,
                "activityClass": "education",
                "activityName": "campus",
                "activityDetail": "교내외 교육 활동",
                "activityWeight": 0.2,
                "activityDomain": 0,
                "categoryId": 1,
                "categoryName": "LQ",
                "categoryRatio": 33.3
              }
            ],
            "page": 0,
            "size": 20,
            "totalElements": 1,
            "totalPage": 1
          },
          "path": "/api/student/submits"
        }
        ```
        """
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "제출 내역 조회 성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PaginationDto.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping("/submits")
    @PreAuthorize("hasRole('student')")
    public ApiResponse<PaginationDto<SubmitDto.BasicInfo>> getMySubmits(
        @Parameter(in = ParameterIn.QUERY, description = "제출 상태 (0:미승인, 1:승인, 2:거부)", example = "1")
        @RequestParam(required = false) Integer state,

        @Parameter(hidden = true)
        @PageableDefault(size = 20, sort = "submitDate", direction = Sort.Direction.DESC)
        Pageable pageable,

        HttpServletRequest request
    ) {
        String token = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7))
                .orElseThrow(() -> new IllegalArgumentException("인증 토큰이 없습니다."));
        Long userId = jwtUtil.getUserId(token);

        PaginationDto<SubmitDto.BasicInfo> result =
            submitService.getMySubmits(userId, state, pageable);

        return ApiResponse.success(result, request.getRequestURI());
    }

    @Operation(summary = "제출 내역 상세 조회")
    @GetMapping("/submit/{id}")
    public ResponseEntity<ApiResponse<SubmitDto.DetailInfo>> getSubmitDetailInfo(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        String token = parseJWT(r);
        Long userId = jwtUtil.getUserId(token);
        submitService.checkSubmitOwnedByStudent(userId, id);

        return ResponseEntity.ok().body(ApiResponse.success(submitService.getSubmitDetailInfoById(id), r.getRequestURI()));
    }

    @Operation(
        summary = "내 제출 내역 삭제",
        description = """
        **사용법**  
        DELETE /api/student/submits/{id}  
        **헤더**  
        Authorization: Bearer {accessToken}  
        """
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "승인된 제출 삭제 시도 또는 잘못된 요청")
    })
    @DeleteMapping("/submits/{id}")
    @PreAuthorize("hasRole('student')")
    public ApiResponse<Void> deleteMySubmit(
        @Parameter(in = ParameterIn.PATH, description = "삭제할 제출 ID", example = "123")
        @PathVariable("id") Long submitId,

        HttpServletRequest request
    ) {
        String token = Optional.ofNullable(request.getHeader("Authorization"))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7))
                .orElseThrow(() -> new IllegalArgumentException("인증 토큰이 없습니다."));
        Long userId = jwtUtil.getUserId(token);

        submitService.deleteSubmit(userId, submitId);
        return ApiResponse.success(null, request.getRequestURI());
    }

    /**
     * 학생 제출 API
     * - activityId, content 는 필수, files 는 선택(다중 첨부 가능)
     */
    @Operation(summary = "학생 활동 제출 (multipart/form-data)",
               description = "activityId, content 필수. files는 선택(다중 첨부 가능)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="200", description="제출 성공",
                     content=@Content(mediaType="application/json")),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="400", description="잘못된 요청"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="401", description="인증 실패"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode="403", description="권한 없음")
    })
    @PostMapping(value="/submits",
                 consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('student')")
    public ApiResponse<SubmitDto.BasicInfo> createSubmit(
        @Parameter(description="activityId, content JSON", required=true,
                   in=ParameterIn.DEFAULT)
        @RequestPart("requestDto") SubmitCreateRequestDto dto,

        @Parameter(description="첨부파일들 (선택)", in=ParameterIn.DEFAULT,
                   content=@Content(mediaType="application/octet-stream"))
        @RequestPart(value="files", required=false)
        List<MultipartFile> files,

        HttpServletRequest request
    ) throws Exception {
        // JWT에서 userId 추출
        String token = Optional.ofNullable(request.getHeader("Authorization"))
            .filter(h -> h.startsWith("Bearer "))
            .map(h -> h.substring(7))
            .orElseThrow(() -> new IllegalArgumentException("인증 토큰이 없습니다."));
        Long userId = jwtUtil.getUserId(token);

        // 서비스 호출
        SubmitDto.BasicInfo result = submitService.createSubmit(userId, dto, files);
        return ApiResponse.success(result, request.getRequestURI());
    }

    @Operation(summary = "학생 활동 제출 (raw binary)",
               description = "미리 생성된 submitId에 binary file 저장")
    @PostMapping(value="/submits/{submitId}/file",
                 consumes=MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasRole('student')")
    public void uploadBinaryFile(
        @PathVariable Long submitId,
        @RequestHeader("file-name") String name,
        @RequestHeader("file-type") String type,
        @RequestBody byte[] data
    ) {
        submitService.saveFileBinary(submitId, name, type, data);
    }

    @Operation(summary = "학생 본인의 3Q 지표 요약")
    @GetMapping("/3q-info")
    public ApiResponse<StudentScoreDto.Response> getStudent3QInfo(HttpServletRequest r) {
        String token = parseJWT(r);
        Long userId = jwtUtil.getUserId(token);

        StudentScoreDto.Response result = scoreService.getStudent3QInfo(userId);
        return ApiResponse.success(result, r.getRequestURI());
    }

    @Operation(summary = "학생 본인의 점수, 학과 평균, 전체 평균")
    @GetMapping("/3q-averages")
    public ApiResponse<StudentScoreAverageDto> getStudent3QWithAverages(HttpServletRequest r) {
        String token = parseJWT(r);
        Long userId = jwtUtil.getUserId(token);

        StudentScoreAverageDto result = scoreService.getStudent3QWithAverages(userId);
        return ApiResponse.success(result, r.getRequestURI());
    }

    @Operation(summary = "학생 본인의 월별 3Q 변화")
    @GetMapping("/3q-change/month")
    public ApiResponse<List<MonthlyScoreDto>> getStudentMonthlyScoreDto(HttpServletRequest r) {
        String token = parseJWT(r);
        Long userId = jwtUtil.getUserId(token);

        List<MonthlyScoreDto> result = scoreSubmitService.getStudentMonthlyScoreDto(userId);
        return ApiResponse.success(result, r.getRequestURI());
    }

    private String parseJWT(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new IllegalArgumentException("인증 토큰이 없습니다.");
        }
    }
}