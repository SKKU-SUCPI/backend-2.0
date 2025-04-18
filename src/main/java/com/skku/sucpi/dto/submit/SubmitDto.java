package com.skku.sucpi.dto.submit;

import com.skku.sucpi.dto.fileStorage.FileInfoDto;
import com.skku.sucpi.entity.Submit;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Jacksonized
    @Builder
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
    static public class DetailInfo {
        private BasicInfo basicInfo;
        private List<FileInfoDto> fileInfoList;
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
