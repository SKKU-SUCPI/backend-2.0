package com.skku.sucpi.controller.student;

import java.util.List;
import java.util.StringTokenizer;

import com.skku.sucpi.dto.score.MonthlyScoreDto;
import com.skku.sucpi.dto.score.StudentScoreAverageDto;
import com.skku.sucpi.dto.score.StudentScoreDto;
import com.skku.sucpi.service.fileStorage.FileStorageService;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.service.score.ScoreSubmitService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.skku.sucpi.entity.FileStorage;
import com.skku.sucpi.service.submit.SubmitService;
import com.skku.sucpi.service.user.UserService;
import com.skku.sucpi.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "Student API", description = "학생 전용 기능을 제공하는 API")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final SubmitService submitService;
    private final ScoreService scoreService;
    private final ScoreSubmitService scoreSubmitService;
    private final FileStorageService fileStorageService;



    @GetMapping("/me")
    @Operation(
            summary = "내 프로필 조회",
            description = """
                **사용법**
                - Method : GET
                - Path : /api/student/me
                
                **헤더**
                - Authorization: Bearer {accessToken}
                """
    )
    public ApiResponse<StudentDto.BasicInfo> getMyProfile(
            HttpServletRequest request
    ) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        StudentDto.DetailInfo detail = userService.searchStudentInfo(userId);
        return ApiResponse.success(detail.getBasicInfo(), request.getRequestURI());
    }



    @GetMapping("/submits")
    @Operation(
            summary = "내 제출 내역 목록 조회",
            description = """
                **설명**
                - 학생의 제출 내역을 조회하는 API
                - Pagination 적용
                - 필터링 : 승인 여부
                - 정렬 : 제출날짜 오름차순/내림차순
                
                **사용법**
                - Method : GET
                - Path : /api/student/submits?state={state}&page={page}&size={size}&sort=submitDate,desc
                
                **헤더**
                - Authorization: Bearer {accessToken}
                
                **Query Parameter**
                - state (not required) : 0=미승인, 1=승인, 2=반려
                - sort (not required) : submitDate,desc(default) / submitDate,asc
                - size (not required) : 한 페이지 당 개수 (Integer, default = 20)
                - page (not required) : 페이지 번호 (Integer, default = 0, 첫 페이지 = 0)
                ```
                """
    )
    public ApiResponse<PaginationDto<SubmitDto.BasicInfo>> getMySubmits(
        @RequestParam(required = false) Integer state,
        @PageableDefault(size = 20, sort = "submitDate", direction = Sort.Direction.DESC) Pageable pageable,
        HttpServletRequest request
    ) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        PaginationDto<SubmitDto.BasicInfo> result = submitService.getMySubmits(userId, state, pageable);
        return ApiResponse.success(result, request.getRequestURI());
    }



    @GetMapping("/submit/{id}")
    @Operation(
            summary = "내 제출 내역 상세 조회",
            description = """
                **설명**
                - 특정 제출 내역의 상세 정보를 조회하는 API
                
                **사용법**
                - Method : GET
                - Path : /api/student/submit/{id}
                
                **헤더**
                - Authorization: Bearer {accessToken}
                
                **Path Variable**
                - id : 제출 내역 ID
                """
    )
    public ApiResponse<SubmitDto.DetailInfo> getSubmitDetailInfo(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);
        submitService.checkSubmitOwnedByStudent(userId, id);

        return ApiResponse.success(submitService.getSubmitDetailInfoById(id), request.getRequestURI());
    }



    @GetMapping("/files/{id}/download")
    @Operation(
            summary = "내 제출 첨부파일 다운로드",
            description = """
                **설명**
                - 학생이 본인의 제출 첨부파일을 다운로드하는 API
                - 브라우저에서 다운로드 됨
                
                **사용법**
                - Method : GET
                - Path : /api/student/files/{id}/download
                
                **헤더**
                - Authorization: Bearer {accessToken}
                """
    )
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        FileStorage file = fileStorageService.getFileStorageById(id);
        submitService.checkSubmitOwnedByStudent(userId, file.getSubmit().getId());

        String fileName = new StringTokenizer(file.getFileName(), ".").nextToken();
        String fileType = file.getFileType();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "." + fileType + "\"")
                .body(file.getFileDate());
    }



    @DeleteMapping("/submits/{id}")
    @Operation(
            summary = "내 제출 내역 삭제",
            description = """
                **설명**
                - 학생이 본인의 제출 내역을 삭제하는 API
                - 제출 내역과 첨부된 파일 모두 삭제됨
                - 승인된 제출 내역은 삭제할 수 없음
                
                **사용법**
                - Method : DELETE
                - Path : /api/student/submits/{id}
                
                **헤더**
                - Authorization: Bearer {accessToken}
                """
    )
    public ApiResponse<Void> deleteMySubmit(
        @PathVariable("id") Long submitId,
        HttpServletRequest request
    ) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        submitService.deleteSubmit(userId, submitId);
        return ApiResponse.success(null, request.getRequestURI());
    }



    @PostMapping(value="/submits")
    @Operation(
            summary = "내 활동 제출",
            description = """
                **설명**
                - 학생이 활동을 제출하는 API
                - 첨부파일 저장 API 별도 호출
                
                **사용법**
                - Method : POST
                - Path : /api/student/submits
                - Body : JSON
                
                **헤더**
                - Authorization: Bearer {accessToken}
                
                **Request Body**
                - activityId: Long (필수: 활동 ID)
                - title: String (필수: 활동 제목)
                - content: String (필수: 활동 내용)
                """
    )
    public ApiResponse<SubmitDto.BasicInfo> createSubmit(
            @RequestBody SubmitCreateRequestDto dto,
            HttpServletRequest request
    ) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        return ApiResponse.success(submitService.createSubmit(userId, dto), request.getRequestURI());
    }



    @PostMapping(value="/submits/{id}/file", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "내 활동 첨부파일 제출",
            description = """
                **설명**
                - 학생이 본인의 활동 제출에 첨부파일을 업로드하는 API
                - 기존 첨부파일이 있을 경우 모두 삭제 후 새로 업로드
                - 제출 상태가 '반려'인 경우, 첨부파일 업로드 시 제출 상태가 '미승인'으로 변경됨
                
                **사용법**
                - Method : POST
                - Path : /api/student/submits/{id}/file
                - Content-Type : multipart/form-data
                - Form Data : files (여러 개 가능)
                
                **헤더**
                - Authorization: Bearer {accessToken}
                
                **Path Variable**
                - id : 제출 내역 ID
                """
    )
    public void uploadFiles(
            @PathVariable Long id,
            @RequestPart(value="files", required=false) List<MultipartFile> files
    ) throws Exception {
        submitService.saveFiles(id, files);
    }



    @GetMapping("/3q-info")
    @Operation(
            summary = "내 3Q 지표 요약",
            description = """
                **사용법**
                - Method : GET
                - Path : /api/student/3q-info
                
                **헤더**
                - Authorization: Bearer {accessToken}
                """
    )
    public ApiResponse<StudentScoreDto.Response> getStudent3QInfo(HttpServletRequest request) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        StudentScoreDto.Response result = scoreService.getStudent3QInfo(userId);
        return ApiResponse.success(result, request.getRequestURI());
    }



    @Operation(
            summary = "내 점수, 학과 평균, 전체 평균",
            description = """
                **설명**
                - 최근 6개월 점수 조회
                
                **사용법**
                - Method : GET
                - Path : /api/student/3q-averages
                
                **헤더**
                - Authorization: Bearer {accessToken}
                """
    )
    @GetMapping("/3q-averages")
    public ApiResponse<StudentScoreAverageDto> getStudent3QWithAverages(HttpServletRequest request) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        StudentScoreAverageDto result = scoreService.getStudent3QWithAverages(userId);
        return ApiResponse.success(result, request.getRequestURI());
    }



    @GetMapping("/3q-change/month")
    @Operation(
            summary = "월별 3Q 점수 변화",
            description = """
                **사용법**
                - Method : GET
                - Path : /api/student/3q-change/month
                
                **헤더**
                - Authorization: Bearer {accessToken}
                """
    )
    public ApiResponse<List<MonthlyScoreDto>> getStudentMonthlyScoreDto(HttpServletRequest request) {
        String token = jwtUtil.parseJWT(request);
        Long userId = jwtUtil.getUserId(token);

        List<MonthlyScoreDto> result = scoreSubmitService.getStudentMonthlyScore(userId);
        return ApiResponse.success(result, request.getRequestURI());
    }
}