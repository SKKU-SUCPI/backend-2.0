package com.skku.sucpi.service.user;

import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.score.TScoreDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.dto.user.SSOUserDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.entity.Score;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.service.submit.SubmitService;
import com.skku.sucpi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final ScoreService scoreService;
    private final SubmitService submitService;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    @Transactional(readOnly = true)
    public User getAdminByHakbun(String hakbun) {
        return userRepository.findByHakbun(hakbun).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자입니다."));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getOrCreateUser(SSOUserDto ssoUserDto) throws Exception {
        // 솦융대 학생이 아닐 때
        log.info("{} {}", ssoUserDto.getHakbun(), UserUtil.getCodeFromDepartment(ssoUserDto.getDepartment()));

        if (ssoUserDto.getHakbun().length() == 10 && UserUtil.getCodeFromDepartment(ssoUserDto.getDepartment()) == 0F) {
            throw new Exception("소프트웨어융합대학 학생만 이용하실 수 있습니다.");
        }

        return userRepository.findByHakbun(ssoUserDto.getHakbun())
                .orElseGet(() -> {
                    User newUser = new User(ssoUserDto);
                    userRepository.save(newUser);

                    if (newUser.getRole().equals("student")) {
                        Score score = new Score(newUser);
                        scoreService.createScore(score);

                        if (UserUtil.checkCampusY(newUser.getHakgwaCd())) {
                            categoryService.increaseCountY();
                        } else {
                            categoryService.increaseCountM();
                        }
                    }
                    return newUser;
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
