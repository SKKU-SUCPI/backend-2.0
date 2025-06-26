package com.skku.sucpi.controller.adminController;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.activity.ActivityDto;
import com.skku.sucpi.dto.activity.ActivityStatsDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.dto.score.ScoreAverageDto;
import com.skku.sucpi.dto.score.ScoreDepartmentAverageDto;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class AdminController {

    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final UserService userService;
    private final SubmitService submitService;
    private final FileStorageService fileStorageService;
    private final ScoreService scoreService;

    @GetMapping("/ratio")
    @Operation(summary = "RQ, LQ, CQ 비율 조회", description = "")
    public ResponseEntity<ApiResponse<RatioResponseDto>> getAllRatio(HttpServletRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI()));
    }

    @GetMapping("/activities")
    @Operation(summary = "모든 활동 조회", description = "")
    public ResponseEntity<ApiResponse<List<ActivityDto.Response>>> getAllActivities(HttpServletRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(activityService.getAllActivities(), request.getRequestURI()));
    }

    @GetMapping("/students")
    @Operation(summary = "학생 목록 조회", description = """
            name : 학생이름 <br />
            department : 소프트웨어학과, 지능형소프트웨어학과, 글로벌융합학과 <br />
            page : 페이지 번호 (0 부터 시작) <br />
            size : 한 페이지 당 개수 (default : 20) <br />
            sort : 항목/정렬 (항목 : lqScore, rqScore, cqScore / 정렬 : asc, desc / ex> lqScore,desc) <br />
            <br />
            예시 : http://siop-dev.skku.edu:8080/api/admin/students?sort=lqScore,asc&page=0 <br />
            grade : 사용금지 <br />
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
    @Operation(summary = "학생 상세 정보 조회", description = "")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponse<StudentDto.DetailInfo>> getStudent(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(userService.searchStudentInfo(id), r.getRequestURI()));
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
        
        **Header**
        - Authorization: Bearer {accessToken}
        
        **Query Parameter**
        - name (String, not required) : 학생 이름
        - state (Integer, not required) : 0=미승인, 1=승인, 2=반려
        - size (Integer, not required) : 한 페이지 당 개수 (default = 20)
        - page (Integer, not required) : 페이지 번호 (default = 0, 첫 페이지 = 0)
        - sort (String, not required) : submitDate,desc(default) / submitDate,asc
        
        **사용법**
        - GET /api/admin/submits?state={state}&page={page}&size={size}&sort=submitDate,desc&name={name}
        
        **응답 예시**
        ```json
        {
            "success": true,
            "message": "Request Successful",
            "data": {
                "content": [
                    {
                        "basicInfo": {
                            "id": 1,
                            "submitDate": "2025-06-24T14:08:31",
                            "state": 1,
                            "approvedDate": "2025-06-24T14:10:40",
                            "content": "제출 내역 설명입니다.",
                            "comment": null,
                            "activityId": 12,
                            "activityClass": "swActivity",
                            "activityName": "commitStar4",
                            "activityDetail": "커미터로서의 활동 : 4점",
                            "activityWeight": 4.0,
                            "activityDomain": 0,
                            "categoryId": 1,
                            "categoryName": "LQ",
                            "categoryRatio": 33.3
                        },
                        "userId": 2,
                        "userName": "건진신",
                        "studentId": "12221222",
                        "grade": 0,
                        "department": "소프트웨어학과"
                    }
                ],
                "page": 0,
                "totalPage": 364,
                "size": 1,
                "totalElements": 363
            },
            "path": "/api/admin/submits"
        }
        ```
        """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
    })
    public ResponseEntity<ApiResponse<PaginationDto<SubmitDto.ListInfo>>> getSubmits(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer state,
            @PageableDefault(size = 20, sort = "submitDate", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.searchSubmitList(name, state, pageable), request.getRequestURI()));
    }

    @GetMapping("/submit/{id}")
    @Operation(summary = "제출 내역 상세 조회", description = "")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponse<SubmitDto.DetailInfo>> getSubmitDetailInfo(
            @PathVariable Long id,
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.getSubmitDetailInfoById(id), r.getRequestURI()));
    }

    @PostMapping("/submit/state")
    @Operation(summary = "제출 승인/거부 api", description = """
            id : 제출 내역 id <br />
            state : 0, 1, 2 (미승인, 승인, 거부) <br />
            comment : 반려(= 거부) 이유. 반려일 때만 보내기
            """)
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ApiResponse<SubmitStateDto.Response>> updateSubmitState(
            @RequestBody SubmitStateDto.Request request,
            HttpServletRequest r
            ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.updateSubmitState(request), r.getRequestURI()));
    }

    @GetMapping("/files/{id}/download")
    @Operation(summary = "", description = "Media Type 확인해주세요. api request 요청하면 브라우저에서 다운로드가 됩니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Bad Request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
    @Operation(summary = "전체 학생에 대한 3Q 평균")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
    })
    public ResponseEntity<ApiResponse<ScoreAverageDto>> get3QAverage(
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(scoreService.get3QAverage(), r.getRequestURI()));
    }

    @GetMapping("/3q-average/department")
    @Operation(summary = "학과별 3Q 평균")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
    })
    public ResponseEntity<ApiResponse<ScoreDepartmentAverageDto>> getDepartment3QAverage(
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(scoreService.scoreDepartmentAverage(), r.getRequestURI()));
    }

    @GetMapping("/submit/summary")
    @Operation(summary = "3Q의 총, 이번달, 저번달 활동 제출 내역 횟수")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
    })
    public ResponseEntity<ApiResponse<SubmitCountDto.Response>> countSubmissionsForThisAndLastMonth(
            HttpServletRequest r
    ) {
        return ResponseEntity.ok().body(ApiResponse.success(submitService.countSubmissionsForThisAndLastMonth(), r.getRequestURI()));
    }

    @GetMapping("submit-count/activity/{activityId}")
    @Operation(summary = "활동 별 제출 내역 횟수")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
    })
    public ResponseEntity<ApiResponse<ActivityStatsDto.SubmitCount>> getSubmitCountByActivity(
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

        return ResponseEntity.ok().body(ApiResponse.success(submitService.getSubmitCountByActivity(activityId, startDate, endDate), request.getRequestURI()));
    };

}
