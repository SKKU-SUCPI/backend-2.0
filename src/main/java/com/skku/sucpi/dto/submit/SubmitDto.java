package com.skku.sucpi.dto.submit;

import com.skku.sucpi.entity.Submit;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

public class SubmitDto {

    @Getter
    @Jacksonized
    @Builder
    static public class BasicInfo {
        private Long id;
        private LocalDateTime submitDate;
        private Integer state;
        private LocalDateTime approvedDate;
        private String content;

        private Long activityId;
        private String activityClass;
        private String activityName;
        private String activityDetail;
        private Double activityWeight;
        private Long activityDomain;

        private Long categoryId;
        private String categoryName;
        private Double categoryRatio;
    }

    static public BasicInfo from(Submit s) {
        return BasicInfo.builder()
                .id(s.getId())
                .submitDate(s.getSubmitDate())
                .state(s.getState())
                .approvedDate(s.getApprovedDate())
                .content(s.getContent())
                .activityId(s.getActivity().getId())
                .activityClass(s.getActivity().getActivityClass())
                .activityName(s.getActivity().getName())
                .activityDetail(s.getActivity().getDetail())
                .activityWeight(s.getActivity().getWeight())
                .activityDomain((s.getActivity().getDomain()))
                .categoryId((s.getActivity().getCategory().getId()))
                .categoryName((s.getActivity().getCategory().getName()))
                .categoryRatio(s.getActivity().getCategory().getRatio())
                .build();
    }
}
