package com.skku.sucpi.repository;

import com.skku.sucpi.dto.submit.SubmitCountDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Pageable;

import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.submit.SubmitDto;

public interface SubmitRepositoryCustom {
    PaginationDto<SubmitDto.ListInfo> searchSubmitList(
            String userName,
            Integer state,
            Pageable pageable
    );

    PaginationDto<SubmitDto.BasicInfo> searchMySubmitsByUser(
        Long userId,
        Integer state,
        Pageable pageable
    );

    SubmitCountDto.Count countLQSubmissionsForThisAndLastMonth();

    SubmitCountDto.Count countRQSubmissionsForThisAndLastMonth();

    SubmitCountDto.Count countCQSubmissionsForThisAndLastMonth();
}
