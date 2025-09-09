package com.skku.sucpi.service.submit;

import java.time.LocalDate;
import java.util.List;

import com.skku.sucpi.dto.activity.ActivityStatsDto;
import com.skku.sucpi.dto.submit.SubmitCountDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.dto.submit.SubmitCreateRequestDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.submit.SubmitStateDto;
import com.skku.sucpi.entity.Activity;
import com.skku.sucpi.entity.FileStorage;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.ActivityRepository;
import com.skku.sucpi.repository.FileStorageRepository;
import com.skku.sucpi.repository.SubmitRepository;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.fileStorage.FileStorageService;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.util.UserUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class SubmitService {

    private final SubmitRepository submitRepository;
    private final FileStorageService fileStorageService;
    private final ScoreService scoreService;
    private final CategoryService categoryService;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final FileStorageRepository fileStorageRepository;

    public void checkSubmitOwnedByStudent(Long userId, Long submitId) {
        Submit submit = submitRepository.findById(submitId)
                .orElseThrow(() -> new IllegalArgumentException("No submit id : " + submitId));

        if (!submit.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("학생은 본인의 제출 내역만 확인할 수 있습니다.");
        }
    }


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
                .comment("")
                .build();

    }

    public List<Submit> getSubmitListByUserId(Long userId) {
        return submitRepository.findByUserId(userId);
    }

    public SubmitDto.DetailInfo getSubmitDetailInfoById(Long id) {
        Submit submit = submitRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제출 내역입니다."));
        List<FileInfoDto> fileInfoList = fileStorageService.getFileInfoBySubmitId(id);
        User user = submit.getUser();

        return SubmitDto.DetailInfo.builder()
                .basicInfo(SubmitDto.from(submit))
                .fileInfoList(fileInfoList)
                .userId(user.getId())
                .userName(user.getName())
                .studentId(user.getHakbun())
                .department(UserUtil.getDepartmentFromCode(user.getHakgwaCd()))
                .build();
    }

    public PaginationDto<SubmitDto.ListInfo> searchSubmitList(
            String userName,
            Integer state,
            Pageable pageable
    ) {
        return submitRepository.searchSubmitList(userName, state, pageable);
    }

    // 학생 본인의 제출 내역을 상태별로 페이징 조회
    @Transactional(readOnly = true)
    public PaginationDto<SubmitDto.BasicInfo> getMySubmits(
            Long userId,
            Integer state,
            Pageable pageable
    ) {
        return submitRepository.searchMySubmitsByUser(userId, state, pageable);
    }

    // 제출 삭제
    @Transactional
    public void deleteSubmit(Long userId, Long submitId) {
        Submit submit = submitRepository.findById(submitId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제출 내역입니다."));

        // 1) 본인 소유 확인
        if (!submit.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 제출만 삭제할 수 있습니다.");
        }
        // 2) 승인된 제출은 삭제 불가
        if (submit.getState() == 1) {
            throw new IllegalArgumentException("승인된 제출은 삭제할 수 없습니다.");
        }
        // 3) 삭제 (연관된 FileStorage Cascade 삭제)
        submitRepository.delete(submit);
    }

    
    /**
     * 학생 제출 생성
     */
    @Transactional
    public SubmitDto.BasicInfo createSubmit(
            Long userId,
            SubmitCreateRequestDto dto
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Activity activity = activityRepository.findById(dto.getActivityId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Activity 입니다."));

        Submit submit = Submit.builder()
            .user(user)
            .activity(activity)
            .title(dto.getTitle())
            .content(dto.getContent())
            .state(0)
            .build();
        Submit saved = submitRepository.save(submit);

        return SubmitDto.from(saved);
    }



    public void saveFiles(
            Long submitId,
            List<MultipartFile> files
    ) throws Exception
    {
        Submit submit = submitRepository.findById(submitId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제출 내역입니다."));

        // (1) 제출 삳태 미승인으로 변경
        submit.updateState(0);

        // (2) 기존 제출 내역 삭제
        fileStorageService.deleteAllFileBySubmitId(submitId);

        // (3) 새로운 첨부파일 저장
        if (files != null) {
            for (MultipartFile f : files) {
                String orig = f.getOriginalFilename();
                String base = orig == null ? "" : orig.replaceFirst("\\.[^.]+$", "");
                String ext  = orig != null && orig.contains(".")
                              ? orig.substring(orig.lastIndexOf('.')+1)
                              : "";
                FileStorage fs = FileStorage.builder()
                    .submit(submit)
                    .fileName(base)
                    .fileType(ext)
                    .fileDate(f.getBytes())
                    .build();
                fileStorageRepository.save(fs);
            }
        }
    }



    // 활동의 제출 내역 수 조회
    public ActivityStatsDto.SubmitCount getSubmitCountByActivity(
            Long activityId,
            LocalDate start,
            LocalDate end
    ) throws Exception {
        return submitRepository.getSubmitCountByActivity(activityId, start, end);
    }



    @Transactional
    public void saveFileBinary(
            Long submitId,
            String name,
            String type,
            byte[] data
    ) {
        Submit sub = submitRepository.findById(submitId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 submitId"));
        FileStorage fs = FileStorage.builder()
            .submit(sub)
            .fileName(name)
            .fileType(type)
            .fileDate(data)
            .build();
        fileStorageRepository.save(fs);
    }



    public SubmitCountDto.Response countSubmissionsForThisAndLastMonth () {
        return SubmitCountDto.Response.builder()
                .lq(submitRepository.countLQSubmissionsForThisAndLastMonth())
                .rq(submitRepository.countRQSubmissionsForThisAndLastMonth())
                .cq(submitRepository.countCQSubmissionsForThisAndLastMonth())
                .build();
    }
}
