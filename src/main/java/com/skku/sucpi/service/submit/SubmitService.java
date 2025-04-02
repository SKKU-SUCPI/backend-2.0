package com.skku.sucpi.service.submit;

import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.repository.SubmitRepository;
import com.skku.sucpi.service.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class SubmitService {

    private final SubmitRepository submitRepository;
    private final FileStorageService fileStorageService;

    public SubmitStateDto.Response updateSubmitState(SubmitStateDto.Request request) {
        Optional<Submit> optionalSubmit = submitRepository.findById(request.getId());

        if (optionalSubmit.isEmpty()) {
            throw new IllegalArgumentException("No submit id : " + request.getId());
        } else {
            Submit submit = optionalSubmit.get();
            submit.updateState(request.getState());

            return SubmitStateDto.Response.builder()
                    .id(submit.getId())
                    .state(submit.getState())
                    .build();
        }
    }

    public List<Submit> getSubmitListByUserId(Long userId) {
        return submitRepository.findByUserId(userId);
    }

    public SubmitDto.DetailInfo getSubmitDetailInfoById(Long id) {
        Submit submit = submitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제출 내역입니다."));
        List<FileInfoDto> fileInfoList = fileStorageService.getFileInfoBySubmitId(id);

        return SubmitDto.DetailInfo.builder()
                .basicInfo(SubmitDto.from(submit))
                .fileInfoList(fileInfoList)
                .build();
    }

    public Page<SubmitDto.ListInfo> searchSubmitList(
            String userName,
            Integer state,
            Pageable pageable
    ) {
        return submitRepository.searchSubmitList(userName, state, pageable);
    }
}
