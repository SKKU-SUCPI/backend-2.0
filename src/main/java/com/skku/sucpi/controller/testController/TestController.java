package com.skku.sucpi.controller.testController;

import org.springframework.web.bind.annotation.*;

import com.skku.sucpi.dto.test.TestRequestDto;
import com.skku.sucpi.dto.test.TestResponseDto;
import com.skku.sucpi.service.TestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    @Operation(summary = "테스트 GET API", description = "간단한 테스트 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 호출됨"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public String test() {
        return "Success";
    }

    @PostMapping("/test")
    @Operation(summary = "테스트 POST API", description = "간단한 테스트 api입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 호출됨"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public TestResponseDto save(@RequestBody TestRequestDto requestDto) {
        return testService.save(requestDto);
    }
}
