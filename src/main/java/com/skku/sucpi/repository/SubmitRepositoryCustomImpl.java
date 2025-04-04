package com.skku.sucpi.repository;

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
import org.springframework.data.domain.Pageable;

import java.util.List;

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
}
