package com.skku.sucpi.repository;

import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.user.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    PaginationDto<StudentDto.BasicInfo> searchStudentsList(
            String name,
            String department,
            String studentId,
            Pageable pageable
    );
}
