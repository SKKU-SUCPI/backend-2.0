package com.skku.sucpi.controller.testController;

import com.skku.sucpi.dto.test.TestRequestDto;
import com.skku.sucpi.dto.test.TestResponseDto;
import com.skku.sucpi.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {

    private final TestService testService;

    @GetMapping("/test")
    public String test() {
        return "Success";
    }

    @PostMapping("/test")
    public TestResponseDto save(@RequestBody TestRequestDto requestDto) {
        return testService.save(requestDto);
    }
}
