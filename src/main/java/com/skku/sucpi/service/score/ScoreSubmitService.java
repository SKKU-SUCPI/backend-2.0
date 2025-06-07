package com.skku.sucpi.service.score;

import com.skku.sucpi.dto.score.MonthlyScoreDto;
import com.skku.sucpi.entity.Score;
import com.skku.sucpi.repository.ScoreRepository;
import com.skku.sucpi.repository.SubmitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class ScoreSubmitService {

    private final ScoreRepository scoreRepository;
    private final ScoreService scoreService;
    private final SubmitRepository submitRepository;

    public List<MonthlyScoreDto> getStudentMonthlyScoreDto (Long userId) {
        List<MonthlyScoreDto> monthlyScoreList = submitRepository.searchStudentMonthlyScore(userId);
        Score score = scoreService.getScoreByUserId(userId);

        // 누적합으로 어떻게 구하지??
        List<MonthlyScoreDto> temp = new ArrayList<>();
        Double lq = 0D;
        Double rq = 0D;
        Double cq = 0D;

        List<MonthlyScoreDto> grouped = groupedMonthlyScoreDto(monthlyScoreList);

        for (MonthlyScoreDto scoreDto : grouped) {
            lq += scoreDto.getLq();
            rq += scoreDto.getRq();
            cq += scoreDto.getCq();

            temp.add(MonthlyScoreDto.builder()
                    .month(scoreDto.getMonth())
                    .lq(lq + score.getLqScore())
                    .rq(rq + score.getRqScore())
                    .cq(cq + score.getCqScore())
                    .build());
        }

        List<MonthlyScoreDto> result = new ArrayList<>();
        for (MonthlyScoreDto scoreDto : temp) {
            result.add(MonthlyScoreDto.builder()
                    .month(scoreDto.getMonth())
                    .lq(scoreDto.getLq() - lq)
                    .rq(scoreDto.getRq() - rq)
                    .cq(scoreDto.getCq() - cq)
                    .build());
        }

        return result;
    }

    private List<MonthlyScoreDto> groupedMonthlyScoreDto(List<MonthlyScoreDto> monthlyScore) {
        Map<String, MonthlyScoreDto> groupedMap = monthlyScore.stream()
                .collect(Collectors.toMap(
                        MonthlyScoreDto::getMonth,
                        m -> MonthlyScoreDto.builder().month(m.getMonth()).lq(m.getLq()).rq(m.getRq()).cq(m.getCq()).build(),
                        (m1, m2) -> MonthlyScoreDto.builder()
                                .month(m1.getMonth())
                                .lq(m1.getLq() + m2.getLq())
                                .rq(m1.getRq() + m2.getRq())
                                .cq(m1.getCq() + m2.getCq())
                                .build()
                ));

        List<String> fullMonths = IntStream.rangeClosed(0, 5)
                .mapToObj(i -> YearMonth.from(LocalDate.now().minusMonths(i)))
                .sorted() // 오름차순
                .map(YearMonth::toString)
                .toList();

       return fullMonths.stream()
                .map(month -> groupedMap.getOrDefault(month,
                        MonthlyScoreDto.builder()
                                .month(month)
                                .rq(0D)
                                .lq(0D)
                                .cq(0D)
                                .build()))
                .toList();
    }
}
