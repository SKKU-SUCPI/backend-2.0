package com.skku.sucpi.repository;

import com.skku.sucpi.dto.submit.SubmitDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubmitRepositoryCustom {
    public Page<SubmitDto.ListInfo> searchSubmitList(
            String userName,
            Integer state,
            Pageable pageable
    );
}
