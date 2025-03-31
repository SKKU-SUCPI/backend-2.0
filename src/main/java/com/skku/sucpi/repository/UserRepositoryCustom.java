package com.skku.sucpi.repository;

import com.skku.sucpi.dto.user.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<StudentDto.BasicInfo> searchStudentsList(
            String name,
            String department,
            String studentId,
            Integer grade,
            Pageable pageable
    );
}
