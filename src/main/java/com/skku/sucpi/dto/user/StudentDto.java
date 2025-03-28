package com.skku.sucpi.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

public class StudentDto {

    // 목록
    @Getter
    @Jacksonized
    @Builder
    static public class basicInfo {
        private Long id;
        private String name; // 이름
        private String department; // 학과
        private String studentId; // 학번
        private Integer grade; // 학년
        private Double lq; // 원점수
        private Double rq; // 원점수
        private Double cq; // 원점수
        private Double tLq; // 조정 점수
        private Double tRq; // 조정 점수
        private Double tCq; // 조정 점수
        private Double totalScore; // LQ + RQ + CQ
    }

    // 상세 정보
    @Getter
    @Jacksonized
    @Builder
    static public class detailInfo {

    }
}
