package com.skku.sucpi.service.fileStorage;

import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.entity.FileStorage;
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

    @Transactional(readOnly = true)
    public FileStorage getFileStorageById(Long id) {
        return fileStorageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일 id 입니다."));
    }

    public void deleteAllFileBySubmitId(Long id) {
        fileStorageRepository.deleteAllBySubmitId(id);
    }
}
