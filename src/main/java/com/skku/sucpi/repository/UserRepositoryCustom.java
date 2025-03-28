package com.skku.sucpi.repository;

import com.skku.sucpi.dto.user.StudentDto;
import org.springframework.data.domain.Page;

public interface UserRepositoryCustom {
    Page<StudentDto.basicInfo> searchStudentsList(
            String name,
            String department,
            String studentId,
            Integer grade,
            String sortBy, // 정렬 기준
            String direction, // acs or desc
            int page,
            int size
    );
}
