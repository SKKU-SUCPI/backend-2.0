package com.skku.sucpi.repository;

import com.skku.sucpi.dto.fileStorage.FileInfoDto;

import java.util.List;

public interface FileStorageRepositoryCustom {
    List<FileInfoDto> findFileInfoBySubmitId(Long submitId);
}
