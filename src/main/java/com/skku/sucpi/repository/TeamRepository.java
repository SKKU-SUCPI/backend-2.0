package com.skku.sucpi.repository;

import com.skku.sucpi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long>  {
    List<Team> findByProjectId(Long projectId);
}
