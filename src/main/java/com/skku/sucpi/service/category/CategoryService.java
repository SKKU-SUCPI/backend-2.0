package com.skku.sucpi.service.category;

import com.skku.sucpi.dto.category.RatioRequestDto;
import com.skku.sucpi.dto.category.RatioResponseDto;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void changeRatio(RatioRequestDto ratioRequestDto) {
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            switch (category.getName()) {
                case "LQ" -> category.updateRatio(ratioRequestDto.getLq());
                case "RQ" -> category.updateRatio(ratioRequestDto.getRq());
                case "CQ" -> category.updateRatio(ratioRequestDto.getCq());
            }
        }
    }

    public RatioResponseDto getAllRatio() {
        List<Category> categories = categoryRepository.findAll();

        Double lq = 0D;
        Double rq = 0D;
        Double cq = 0D;

        for (Category category : categories) {
            switch (category.getName()) {
                case "LQ" -> lq = category.getRatio();
                case "RQ" -> rq = category.getRatio();
                case "CQ" -> cq = category.getRatio();
            }
        }

        return RatioResponseDto.builder()
                .lq(lq)
                .rq(rq)
                .cq(cq)
                .build();
    }
}
