package com.skku.sucpi.repository;

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
}
