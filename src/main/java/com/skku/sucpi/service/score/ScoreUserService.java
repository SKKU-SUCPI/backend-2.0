package com.skku.sucpi.service.score;

import com.skku.sucpi.dto.score.TScoreDto;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.ScoreRepository;
import com.skku.sucpi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScoreUserService {

    private final UserRepository userRepository;
    private final ScoreService scoreService;

    public TScoreDto get3QAverage() {
        List<User> users = userRepository.findAll();
        int studentNum = 0;
        Double lq = 0D;
        Double cq = 0D;
        Double rq = 0D;

        for (User user : users) {
            if (user.getRole().equals("students")) {
                studentNum++;
            }

            TScoreDto tScore = scoreService.getTScoreByUserId(user.getId());
            lq += tScore.getTLq();
            cq += tScore.getTCq();
            rq += tScore.getTRq();
        }

        lq = lq / studentNum;
        cq = cq / studentNum;
        rq = rq / studentNum;

        return TScoreDto.builder()
                .tRq(rq)
                .tCq(cq)
                .tLq(lq)
                .build();
    }
}
