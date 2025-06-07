package com.skku.sucpi.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.querydsl.core.types.dsl.Expressions;
import com.skku.sucpi.dto.score.MonthlyScoreDto;
import com.skku.sucpi.dto.submit.SubmitCountDto;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.skku.sucpi.dto.PaginationDto;
import com.skku.sucpi.dto.submit.SubmitDto;
import com.skku.sucpi.entity.QSubmit;
import com.skku.sucpi.entity.Submit;
import com.skku.sucpi.util.UserUtil;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubmitRepositoryCustomImpl implements SubmitRepositoryCustom{

    private final EntityManager em;

    @Override
    public PaginationDto<SubmitDto.ListInfo> searchSubmitList(
            String userName,
            Integer state,
            Pageable pageable) {
        QSubmit submit = QSubmit.submit;

        BooleanBuilder builder = new BooleanBuilder();

        if (userName != null && !userName.isEmpty()) {
            builder.and(submit.user.name.containsIgnoreCase(userName));
        }

        if (state != null) {
            builder.and(submit.state.eq(state));
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        JPAQuery<Submit> jpaQuery = queryFactory
                .select(submit)
                .from(submit)
                .where(builder)
                .orderBy(submit.submitDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<SubmitDto.ListInfo> result = jpaQuery.fetch().stream().map(t -> SubmitDto.ListInfo.builder()
                .basicInfo(SubmitDto.from(t))
                .userId(t.getUser().getId())
                .userName(t.getUser().getName())
                .studentId(t.getUser().getHakbun())
                .grade(t.getUser().getYear().intValue())
                .department(UserUtil.getDepartmentFromCode(t.getUser().getHakgwaCd()))
                .build())
                .toList();

        Long total = queryFactory
                .select(submit.count())
                .from(submit)
                .where(builder)
                .fetchOne();


        total = total != null ? total : 0;

//        return new PageImpl<>(result, pageable, total != null ? total : 0);
        return PaginationDto.<SubmitDto.ListInfo>builder()
                .content(result)
                .page(pageable.getPageNumber())
                .totalPage(total / pageable.getPageSize() + 1)
                .size(pageable.getPageSize())
                .totalElements(total)
                .build();
    }

    @Override
    public PaginationDto<SubmitDto.BasicInfo> searchMySubmitsByUser(
            Long userId,
            Integer state,
            Pageable pageable) {

        QSubmit submit = QSubmit.submit;
        BooleanBuilder builder = new BooleanBuilder()
            .and(submit.user.id.eq(userId));               // 본인 필터

        if (state != null) {
            builder.and(submit.state.eq(state));           // 상태 필터
        }

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        // 1) 조회 쿼리
        List<SubmitDto.BasicInfo> content = queryFactory
            .select(submit)
            .from(submit)
            .where(builder)
            .orderBy(submit.submitDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(SubmitDto::from)
            .toList();

        // 2) 전체 카운트
        long total = queryFactory
            .select(submit.count())
            .from(submit)
            .where(builder)
            .fetchOne();

        // 3) 페이징 DTO 빌드
        return PaginationDto.<SubmitDto.BasicInfo>builder()
                .content(content)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .totalElements(total)
                .totalPage((total + pageable.getPageSize() - 1) / pageable.getPageSize())
                .build();
    }

    @Override
    public SubmitCountDto.Count countLQSubmissionsForThisAndLastMonth() {
        QSubmit submit = QSubmit.submit;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        LocalDate prevMonth = now.minusMonths(1);
        int prevYear = prevMonth.getYear();
        int prevMonthValue = prevMonth.getMonthValue();

        Long totalCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.state.eq(1),
                        submit.activity.category.id.eq(1L))
                .fetchOne();

        Long currentMonthCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.activity.category.id.eq(1L),
                        submit.state.eq(1),
                        Expressions.numberTemplate(Integer.class, "year({0})", submit.submitDate).eq(year),
                        Expressions.numberTemplate(Integer.class, "month({0})", submit.submitDate).eq(month))
                .fetchOne();

        Long lastMonthCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.activity.category.id.eq(1L),
                        submit.state.eq(1),
                        Expressions.numberTemplate(Integer.class, "year({0})", submit.submitDate).eq(prevYear),
                        Expressions.numberTemplate(Integer.class, "month({0})", submit.submitDate).eq(prevMonthValue))
                .fetchOne();


        return SubmitCountDto.Count.builder()
                .lastMonth(lastMonthCount)
                .currentMonth(currentMonthCount)
                .total(totalCount)
                .build();
    }

    @Override
    public SubmitCountDto.Count countRQSubmissionsForThisAndLastMonth() {
        QSubmit submit = QSubmit.submit;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        LocalDate prevMonth = now.minusMonths(1);
        int prevYear = prevMonth.getYear();
        int prevMonthValue = prevMonth.getMonthValue();

        Long totalCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.state.eq(1),
                        submit.activity.category.id.eq(2L))
                .fetchOne();

        Long currentMonthCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.activity.category.id.eq(2L),
                        submit.state.eq(1),
                        Expressions.numberTemplate(Integer.class, "year({0})", submit.submitDate).eq(year),
                        Expressions.numberTemplate(Integer.class, "month({0})", submit.submitDate).eq(month))
                .fetchOne();

        Long lastMonthCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.activity.category.id.eq(2L),
                        submit.state.eq(1),
                        Expressions.numberTemplate(Integer.class, "year({0})", submit.submitDate).eq(prevYear),
                        Expressions.numberTemplate(Integer.class, "month({0})", submit.submitDate).eq(prevMonthValue))
                .fetchOne();


        return SubmitCountDto.Count.builder()
                .lastMonth(lastMonthCount)
                .currentMonth(currentMonthCount)
                .total(totalCount)
                .build();
    }

    @Override
    public SubmitCountDto.Count countCQSubmissionsForThisAndLastMonth() {
        QSubmit submit = QSubmit.submit;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        LocalDate prevMonth = now.minusMonths(1);
        int prevYear = prevMonth.getYear();
        int prevMonthValue = prevMonth.getMonthValue();

        Long totalCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.state.eq(1),
                        submit.activity.category.id.eq(3L))
                .fetchOne();

        Long currentMonthCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.activity.category.id.eq(3L),
                        submit.state.eq(1),
                        Expressions.numberTemplate(Integer.class, "year({0})", submit.submitDate).eq(year),
                        Expressions.numberTemplate(Integer.class, "month({0})", submit.submitDate).eq(month))
                .fetchOne();

        Long lastMonthCount = queryFactory
                .select(submit.count())
                .from(submit)
                .where(submit.activity.category.id.eq(3L),
                        submit.state.eq(1),
                        Expressions.numberTemplate(Integer.class, "year({0})", submit.submitDate).eq(prevYear),
                        Expressions.numberTemplate(Integer.class, "month({0})", submit.submitDate).eq(prevMonthValue))
                .fetchOne();


        return SubmitCountDto.Count.builder()
                .lastMonth(lastMonthCount)
                .currentMonth(currentMonthCount)
                .total(totalCount)
                .build();
    }

    @Override
    public List<MonthlyScoreDto> searchStudentMonthlyScore(Long userId) {
        QSubmit submit = QSubmit.submit;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        LocalDate now = LocalDate.now();
        LocalDate sixMonthsAgo = now.minusMonths(5).withDayOfMonth(1);

        JPAQuery<Submit> jpaQuery = queryFactory
                .select(submit)
                .from(submit)
                .where(submit.state.eq(1),
                        submit.user.id.eq(userId),
                        submit.submitDate.between(sixMonthsAgo.atStartOfDay(), now.atTime(23, 59, 59))
                );


        return jpaQuery.fetch().stream().map(t -> MonthlyScoreDto.builder()
                        .month(t.getSubmitDate().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                        .lq(t.getActivity().getCategory().getId() == 1 ? t.getActivity().getWeight() : 0)
                        .rq(t.getActivity().getCategory().getId() == 2 ? t.getActivity().getWeight() : 0)
                        .cq(t.getActivity().getCategory().getId() == 3 ? t.getActivity().getWeight() : 0)
                        .build())
                .toList();
    }
}
