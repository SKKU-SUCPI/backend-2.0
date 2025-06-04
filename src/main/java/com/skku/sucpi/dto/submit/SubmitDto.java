package com.skku.sucpi.dto.submit;

import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.entity.Submit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

public class SubmitDto {

    @Getter
    @Jacksonized
    @Builder
    @Schema (name = "SubmitDto_BasicInfo")
    static public class BasicInfo {
        private Long id;
        private LocalDateTime submitDate;
        private Integer state;
        private LocalDateTime approvedDate;
        private String content;
        private String comment;

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

    @Getter
    @Jacksonized
    @Builder
    @Schema (name = "SubmitDto_ListInfo")
    static public class ListInfo {
        // 목록 조회 용
        private BasicInfo basicInfo;

        private Long userId;
        private String userName;
        private String studentId;
        private Integer grade;
        private String department;
    }

    @Getter
    @Jacksonized
    @Builder
    @Schema (name = "SubmitDto_DetailInfo")
    static public class DetailInfo {
        private BasicInfo basicInfo;
        private List<FileInfoDto> fileInfoList;

        private Long userId;
        private String userName;
        private String studentId;
        private String department;
    }

    static public BasicInfo from(Submit s) {
        return BasicInfo.builder()
                .id(s.getId())
                .submitDate(s.getSubmitDate())
                .state(s.getState())
                .approvedDate(s.getApprovedDate())
                .content(s.getContent())
                .comment(s.getComment())
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
