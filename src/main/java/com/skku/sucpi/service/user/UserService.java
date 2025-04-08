package com.skku.sucpi.service.user;

import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.score.TScoreDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.user.SSOUserDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.entity.Score;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.ScoreRepository;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.service.submit.SubmitService;
import com.skku.sucpi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ScoreService scoreService;
    private final SubmitService submitService;

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getOrCreateUser(SSOUserDto ssoUserDto) {
        return userRepository.findByHakbun(ssoUserDto.getHakbun())
                .orElseGet(() -> {
                    User newUser = new User(ssoUserDto);
                    return userRepository.save(newUser);
        });
    }

    @Transactional(readOnly = true)
    public StudentDto.DetailInfo searchStudentInfo(
            Long userId
    ) {
        // 1. 학생 정보
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 2. 점수 정보
        Score score = scoreService.getScoreByUserId(userId);

        // 3. 제출 정보
        List<Submit> submits = submitService.getSubmitListByUserId(userId);

        // 4. t-score
        TScoreDto tScoreDto = scoreService.getTScoreByUserId(user.getId());

        StudentDto.BasicInfo info = StudentDto.BasicInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .department(UserUtil.getDepartmentFromCode(user.getHakgwaCd()))
                .studentId(user.getHakbun())
                .grade(user.getYear().intValue())
                .lq(score.getLqScore())
                .rq(score.getRqScore())
                .cq(score.getCqScore())
                .tLq(tScoreDto.getTLq())
                .tCq(tScoreDto.getTCq())
                .tRq(tScoreDto.getTRq())
                .build();



        List<SubmitDto.BasicInfo> submitDtoList = new ArrayList<>();
        for (Submit s : submits) {
            submitDtoList.add(SubmitDto.from(s));
        }

        return StudentDto.DetailInfo.builder()
                .basicInfo(info)
                .submitsList(submitDtoList)
                .build();
    }

    @Transactional(readOnly = true)
    public PaginationDto<StudentDto.BasicInfo> searchStudentsList(
            String name,
            String department,
            String studentId,
            Integer grade,
            Pageable pageable
    ) {
        return userRepository.searchStudentsList(
                name,
                department,
                studentId,
                grade,
                pageable
        );
    }
}
