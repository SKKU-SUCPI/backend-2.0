package com.skku.sucpi.service.fileStorage;

import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.repository.FileStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileStorageService {

    private final FileStorageRepository fileStorageRepository;

    @Transactional(readOnly = true)
    public List<FileInfoDto> getFileInfoBySubmitId(Long submitId) {
        return fileStorageRepository.findFileInfoBySubmitId(submitId);
    }
}
