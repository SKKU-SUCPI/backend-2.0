package com.skku.sucpi.repository;

import com.skku.sucpi.entity.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
}
