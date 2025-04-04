package com.skku.sucpi.repository;

import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubmitRepositoryCustom {
    PaginationDto<SubmitDto.ListInfo> searchSubmitList(
            String userName,
            Integer state,
            Pageable pageable
    );
}
