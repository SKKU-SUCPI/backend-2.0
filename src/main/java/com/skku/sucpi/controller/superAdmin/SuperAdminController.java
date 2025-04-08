package com.skku.sucpi.controller.superAdmin;

import com.skku.sucpi.dto.ApiResponse;
import com.skku.sucpi.dto.category.RatioRequestDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.service.category.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/super-admin")
public class SuperAdminController {

    private final CategoryService categoryService;

    @PostMapping("/ratio")
    public ResponseEntity<ApiResponse<RatioResponseDto>> changeRatio(@Valid @RequestBody RatioRequestDto ratioRequestDto, HttpServletRequest request) {
        Double cq = ratioRequestDto.getCq();
        Double lq = ratioRequestDto.getLq();
        Double rq = ratioRequestDto.getRq();

        if (cq + lq + rq != 100D) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("The sum of LQ, CQ, RQ must be 100.", request.getRequestURI()));
        }

        categoryService.changeRatio(ratioRequestDto);
        return ResponseEntity.ok().body(ApiResponse.success(categoryService.getAllRatio(), request.getRequestURI()));
    }
}
