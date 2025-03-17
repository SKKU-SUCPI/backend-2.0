package com.skku.sucpi.controller.adminController;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.service.category.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CategoryService categoryService;

    @GetMapping("/ratio")
    public ResponseEntity<ApiResponse<RatioResponseDto>> getAllRatio(HttpServletRequest request) {
        return ResponseEntity.ok().body(ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI()));
    }
}
