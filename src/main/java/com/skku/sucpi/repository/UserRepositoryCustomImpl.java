package com.skku.sucpi.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.sucpi.dto.score.TScoreDto;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.entity.QScore;
import com.skku.sucpi.entity.QUser;
import com.skku.sucpi.entity.Score;
import com.skku.sucpi.service.score.ScoreService;
import com.skku.sucpi.util.UserUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final EntityManager em;
    private final ScoreService scoreService;

    @Override
    public Page<StudentDto.BasicInfo> searchStudentsList(
            String name,
            String department,
            String studentId,
            Integer grade,
            Pageable pageable) {

        QUser student = QUser.user;
        QScore score = QScore.score;

        BooleanBuilder builder = new BooleanBuilder();

        // 검색(이름)
        if (name != null && !name.isEmpty()) {
            builder.and(student.name.containsIgnoreCase(name));
        }
        // 필터링(학과)
        if (department != null && !department.isEmpty()) {
            Float code = UserUtil.getCodeFromDepartment(department);
            if (code != 0)  {
                builder.and(student.hakgwaCd.eq(code));
            }
        }
        // 필터링(학번)
        if (studentId != null && !studentId.isEmpty()) {
            builder.and(student.hakbun.eq(studentId));
        }
        // 필터링(학년)
        if (grade != null) {
            builder.and(student.year.between(grade, grade + 0.9));
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        JPAQuery<Tuple> jpaQuery = queryFactory
                .select(
                        student.id,
                        student.name,
                        student.hakgwaCd,
                        student.hakbun,
                        student.year,
                        score.lqScore,
                        score.rqScore,
                        score.cqScore
                )
                .from(student)
                .join(score)
                .on(student.id.eq(score.user.id))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable.getSort(), score));

        List<StudentDto.BasicInfo> result = jpaQuery.fetchJoin().fetch().stream().map(tuple -> {
                    TScoreDto tScoreDto = scoreService.getTScoreByUserId(tuple.get(student.id));
                    return StudentDto.BasicInfo.builder()
                            .id(tuple.get(student.id))
                            .name(tuple.get(student.name))
                            .department(UserUtil.getDepartmentFromCode(tuple.get(student.hakgwaCd)))
                            .studentId(tuple.get(student.hakbun))
                            .grade((int) tuple.get(student.year).doubleValue())
                            .lq(tuple.get(score.lqScore))
                            .cq(tuple.get(score.cqScore))
                            .rq(tuple.get(score.rqScore))
                            .tLq(tScoreDto.getTLq())
                            .tCq(tScoreDto.getTCq())
                            .tRq(tScoreDto.getTRq())
                            .totalScore(tuple.get(score.lqScore) + tuple.get(score.cqScore) + tuple.get(score.rqScore))
                            .build();
                })
                .toList();

        Long total = queryFactory
                .select(student.count())
                .from(student)
                .join(score).on(student.id.eq(score.user.id))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(result, pageable, total != null ? total : 0);
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Sort sort, QScore score) {
        return sort.stream()
                .map(order -> {
                    PathBuilder<Score> pathBuilder = new PathBuilder<>(score.getType(), score.getMetadata());
                    return new OrderSpecifier(
                            order.isAscending() ? Order.ASC : Order.DESC,
                            pathBuilder.get(order.getProperty())
                    );
                })
                .toArray(OrderSpecifier[]::new);
    }
}
