package com.skku.sucpi.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.sucpi.dto.user.StudentDto;
import com.skku.sucpi.entity.QScore;
import com.skku.sucpi.entity.QUser;
import com.skku.sucpi.entity.Score;
import com.skku.sucpi.entity.User;
import com.skku.sucpi.util.UserUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final EntityManager em;

    @Override
    public Page<StudentDto.basicInfo> searchStudentsList(
            String name,
            String department,
            String studentId,
            Integer grade,
            String sortBy,
            String direction,
            int page,
            int size) {

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
            builder.and(student.year.between(grade, grade + 1));
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
                .where(builder);

        // 정렬
        OrderSpecifier<?> orderSpecifier;

//        if (List.of("id", "name", "hakgwaCd", "hakbun", "year").contains(sortBy)) {
//            PathBuilder<User> path = new PathBuilder<>(User.class, "user");
//
//            orderSpecifier = direction.equalsIgnoreCase("desc")
//                    ? path.getComparable(sortBy, Comparable.class).desc()
//                    : path.getComparable(sortBy, Comparable.class).asc();
//        } else
        if (List.of("lqScore", "rqScore", "cqScore").contains(sortBy)) {
            PathBuilder<Score> path = new PathBuilder<>(Score.class, "score");

            orderSpecifier = direction.equalsIgnoreCase("desc")
                    ? path.getComparable(sortBy, Comparable.class).desc()
                    : path.getComparable(sortBy, Comparable.class).asc();
        } else {
            // 기본 정렬: id asc
            PathBuilder<User> path = new PathBuilder<>(User.class, "user");

            orderSpecifier = path.getComparable("id", Comparable.class).asc();
        }

        jpaQuery.orderBy(orderSpecifier);

        // 페이지네이션
        jpaQuery.offset((long) page * size);
        jpaQuery.limit(size);

        List<StudentDto.basicInfo> result = jpaQuery.fetchJoin().fetch().stream().map(tuple -> StudentDto.basicInfo.builder()
                        .id(tuple.get(student.id))
                        .name(tuple.get(student.name))
                        .department(UserUtil.getDepartmentFromCode(tuple.get(student.hakgwaCd)))
                        .studentId(tuple.get(student.hakbun))
                        .grade((int) tuple.get(student.year).doubleValue())
                        .lq(tuple.get(score.lqScore))
                        .cq(tuple.get(score.cqScore))
                        .rq(tuple.get(score.rqScore))
                        .tLq(tuple.get(score.lqScore))
                        .tCq(tuple.get(score.cqScore))
                        .tRq(tuple.get(score.rqScore))
                        .totalScore(tuple.get(score.lqScore) + tuple.get(score.cqScore) + tuple.get(score.rqScore))
                        .build())
                .toList();

        Long total = queryFactory
                .select(student.count())
                .from(student)
                .join(score).on(student.id.eq(score.user.id))
                .where(builder)
                .fetchOne();

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("desc") ?
                        Sort.by(sortBy).descending() :
                        Sort.by(sortBy).ascending());

        return new PageImpl<>(result, pageable, total != null ? total : 0);
    }
}
