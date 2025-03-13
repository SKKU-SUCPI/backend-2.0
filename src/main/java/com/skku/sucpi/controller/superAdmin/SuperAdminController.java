package com.skku.sucpi.controller.superAdmin;

import com.skku.sucpi.dto.category.RatioRequestDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/super-admin")
public class SuperAdminController {

    private final CategoryService categoryService;

    @PostMapping("/ratio")
    public ResponseEntity<RatioResponseDto> changeRatio(@RequestBody RatioRequestDto ratioRequestDto) {
        if (ratioRequestDto.getCq() + ratioRequestDto.getLq() + ratioRequestDto.getRq() != 100D) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        categoryService.changeRatio(ratioRequestDto);
        return ResponseEntity.ok().body(categoryService.getAllRatio());
    }
}
