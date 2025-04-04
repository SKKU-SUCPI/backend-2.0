package com.skku.sucpi.service.submit;

import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.entity.Activity;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.repository.SubmitRepository;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.fileStorage.FileStorageService;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class SubmitService {

    private final SubmitRepository submitRepository;
    private final FileStorageService fileStorageService;
    private final ScoreService scoreService;
    private final CategoryService categoryService;

    public SubmitStateDto.Response updateSubmitState(SubmitStateDto.Request request) {
        Submit submit = submitRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("No submit id : " + request.getId()));

        Integer state = request.getState();
        Integer curState = submit.getState();
        boolean isYuljeon = UserUtil.checkCampusY(submit.getUser().getHakgwaCd());
        Activity activity = submit.getActivity();
        Long categoryId = activity.getCategory().getId();
        Double diff = activity.getWeight();

        // 1. 미승인, 거부 -> 승인
        if ((curState == 0 || curState == 2) && state == 1) {
            // 카테고리 점수 수정
            categoryService.updateSumAndSquareSum(categoryId, diff, isYuljeon);

            // 학생 점수 수정
            scoreService.updateScore(submit.getUser().getId() ,categoryId, diff);
        }
        // 2. 승인 -> 거절
        else if ((curState == 1) && state == 2) {
            diff *= -1;
            // 카테고리 점수 수정
            categoryService.updateSumAndSquareSum(categoryId, diff, isYuljeon);

            // 학생 점수 수정
            scoreService.updateScore(submit.getUser().getId() ,categoryId, diff);
        }

        submit.updateComment(request.getComment());
        submit.updateState(request.getState());

        return SubmitStateDto.Response.builder()
                .id(submit.getId())
                .state(submit.getState())
                .build();

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

    public PaginationDto<SubmitDto.ListInfo> searchSubmitList(
            String userName,
            Integer state,
            Pageable pageable
    ) {
        return submitRepository.searchSubmitList(userName, state, pageable);
    }
}
