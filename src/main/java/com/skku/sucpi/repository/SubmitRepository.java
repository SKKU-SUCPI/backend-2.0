package com.skku.sucpi.repository;

import com.skku.sucpi.entity.Submit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmitRepository extends JpaRepository<Submit, Long>, SubmitRepositoryCustom {
    List<Submit> findByUserId(Long userId);
}
