package com.skku.sucpi.service.score;

import com.skku.sucpi.dto.score.*;
import com.skku.sucpi.entity.Category;
import com.skku.sucpi.entity.ProjectActivityRule;
import com.skku.sucpi.entity.Score;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.repository.ProjectActivityRuleRepository;
import com.skku.sucpi.repository.ScoreRepository;
import com.skku.sucpi.repository.SubmitRepository;
import com.skku.sucpi.service.category.CategoryService;
import com.skku.sucpi.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final ProjectActivityRuleRepository ruleRepository;
    private final CategoryService categoryService;
    private final SubmitRepository submitRepository;

    public void createScore (Score score) {
        scoreRepository.save(score);
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

    public ScoreAverageDto get3QAverage() {
        return ScoreAverageDto.builder()
                .lq(scoreRepository.findAverageLqScore())
                .cq(scoreRepository.findAverageCqScore())
                .rq(scoreRepository.findAverageRqScore())
                .build();
    }

    public ScoreDepartmentAverageDto scoreDepartmentAverage() {
        return ScoreDepartmentAverageDto.builder()
                .sw(scoreRepository.findAverageScoreOfSw())
                .intelligentSw(scoreRepository.findAverageScoreOfIntelligentSw())
                .soc(scoreRepository.findAverageScoreOfSoc())
                .build();
    }

    public StudentScoreDto.Response getStudent3QInfo(Long userId) {
        StudentScoreDto.ScoreInfoInterface studentLqInfo = scoreRepository.findStudentLqInfo(userId);
        StudentScoreDto.ScoreInfo lqInfo = StudentScoreDto.ScoreInfo.builder()
                .score(studentLqInfo.getScore())
                .average(studentLqInfo.getAverage())
                .percentile(studentLqInfo.getPercentRank())
                .build();

//        log.info("{} {}, {}", studentLqInfo.getRank(), studentLqInfo.getTotal(), studentLqInfo.getPercentRank());

        StudentScoreDto.ScoreInfoInterface studentRqInfo = scoreRepository.findStudentRqInfo(userId);
        StudentScoreDto.ScoreInfo rqInfo = StudentScoreDto.ScoreInfo.builder()
                .score(studentRqInfo.getScore())
                .average(studentRqInfo.getAverage())
                .percentile(studentRqInfo.getPercentRank())
                .build();

//        log.info("{} {}, {}", studentRqInfo.getRank(), studentRqInfo.getTotal(), studentRqInfo.getPercentRank());

        StudentScoreDto.ScoreInfoInterface studentCqInfo = scoreRepository.findStudentCqInfo(userId);
        StudentScoreDto.ScoreInfo cqInfo = StudentScoreDto.ScoreInfo.builder()
                .score(studentCqInfo.getScore())
                .average(studentCqInfo.getAverage())
                .percentile(studentCqInfo.getPercentRank())
                .build();

        return StudentScoreDto.Response.builder()
                .lq(lqInfo)
                .rq(rqInfo)
                .cq(cqInfo)
                .build();
    }

    public StudentScoreAverageDto getStudent3QWithAverages(Long userId) {
        Score score = scoreRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Float code = score.getUser().getHakgwaCd();
        ScoreAverageDto department = ScoreAverageDto.builder().build();

        if (code == 1F) {
            department = scoreRepository.findAverageScoreOfSw();
        } else if (code == 2F) {
            department = scoreRepository.findAverageScoreOfIntelligentSw();
        } else if (code == 3F) {
            department = scoreRepository.findAverageScoreOfSoc();
        }


        return StudentScoreAverageDto.builder()
                .student(ScoreAverageDto.builder()
                        .lq(score.getLqScore())
                        .rq(score.getRqScore())
                        .cq(score.getCqScore())
                        .build())
                .department(department)
                .total(ScoreAverageDto.builder()
                        .lq(scoreRepository.findAverageLqScore())
                        .rq(scoreRepository.findAverageRqScore())
                        .cq(scoreRepository.findAverageCqScore())
                        .build())
                .build();
    }

    public StudentScoreDto.Response getStudent3QInfoByProject(Long userId, Long projectId) {
        StudentScoreDto.Response defaultScores = getStudent3QInfo(userId);

        List<ProjectActivityRule> rules = ruleRepository.findByProjectId(projectId);

        if(rules.isEmpty()) {
            return defaultScores;
        }

        Map<Long, Double> customWeightMap = rules.stream()
                .collect(Collectors.toMap(
                        rule -> rule.getActivity().getId(),
                        rule -> rule.getCustomWeight()
                ));

        List<Submit> userSubmits = submitRepository.findByUserId(userId);

        double projectLq = 0.0;
        double projectRq = 0.0;
        double projectCq = 0.0;

        // 3. Calculate dynamic score
        for (Submit submit : userSubmits) {
            // Check if submission is approved (assuming state 1 = approved based on ScoreRepository)
            if (submit.getState() == 1) {
                Long activityId = submit.getActivity().getId();
                Long categoryId = submit.getActivity().getCategory().getId();
                Double baseWeight = submit.getActivity().getWeight();

                // Apply custom multiplier if it exists, otherwise multiply by 1.0
                Double multiplier = customWeightMap.getOrDefault(activityId, 1.0);
                Double finalScore = baseWeight * multiplier;

                if (categoryId == 1L) {
                    projectLq += finalScore;
                } else if (categoryId == 2L) {
                    projectRq += finalScore;
                } else if (categoryId == 3L) {
                    projectCq += finalScore;
                }
            }
        }

        // 4. Build and return the updated response
        return StudentScoreDto.Response.builder()
                .lq(StudentScoreDto.ScoreInfo.builder()
                        .score(projectLq)
                        .average(defaultScores.getLq().getAverage())
                        .percentile(defaultScores.getLq().getPercentile())
                        .build())
                .rq(StudentScoreDto.ScoreInfo.builder()
                        .score(projectRq)
                        .average(defaultScores.getRq().getAverage())
                        .percentile(defaultScores.getRq().getPercentile())
                        .build())
                .cq(StudentScoreDto.ScoreInfo.builder()
                        .score(projectCq)
                        .average(defaultScores.getCq().getAverage())
                        .percentile(defaultScores.getCq().getPercentile())
                        .build())
                .build();
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
