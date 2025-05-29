package com.skku.sucpi.service.score;

import com.skku.sucpi.dto.score.TScoreDto;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.entity.Score;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.repository.ScoreRepository;
import com.skku.sucpi.repository.UserRepository;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.service.user.UserService;
import com.skku.sucpi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final CategoryService categoryService;

    public Score createScore (Score score) {
        return scoreRepository.save(score);
    }

    public Score getScoreByUserId (Long userId) {
        return scoreRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public TScoreDto getTScoreByUserId (Long userId) {
        List<Category> allCategory = categoryService.getAllCategory();
        Score score = scoreRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        User user = score.getUser();

        Double tLq = 0D;
        Double tCq = 0D;
        Double tRq = 0D;

        for (Category category : allCategory) {
            boolean yCampus = UserUtil.checkCampusY(user.getHakgwaCd());
            Double squareSum;
            Double sum;
            Integer count;

            if(yCampus) {
                squareSum = category.getSquareSumY();
                sum = category.getSumY();
                count = category.getCountY();
            } else {
                squareSum = category.getSquareSumM();
                sum = category.getSumM();
                count = category.getCountM();
            }

            if (category.getName().equals("LQ")) {
                tLq = calculateTscore(score.getLqScore(),sum / count ,calculateStandardDeviation(squareSum, sum, count));
            } else if (category.getName().equals("CQ")) {
                tCq = calculateTscore(score.getCqScore(),sum / count ,calculateStandardDeviation(squareSum, sum, count));
            } else if (category.getName().equals("RQ")) {
                tRq = calculateTscore(score.getRqScore(),sum / count ,calculateStandardDeviation(squareSum, sum, count));
            }
        }

        return TScoreDto.builder()
                .tCq(tCq)
                .tLq(tLq)
                .tRq(tRq)
                .build();
    }

    public void updateScore(Long userId, Long categoryId, Double diff) {
        Score score = scoreRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        score.updateScore(categoryId, diff);
    }

    public Double calculateStandardDeviation(Double squareSum, Double sum, Integer count) {
        if (count == 0) throw new IllegalArgumentException("모집단의 수가 1 이상이어야 합니다.");
        double meanSquare = sum * sum / count;

        double std = Math.sqrt((squareSum - meanSquare) / count);

        return std;
    }

    public Double calculateTscore (Double rq, Double avg, Double std) {
        return (((rq - avg) / std) * 10 + 50) / 3;
    }

}
