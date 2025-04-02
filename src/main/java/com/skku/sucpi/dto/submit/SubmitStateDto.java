package com.skku.sucpi.dto.submit;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

public class SubmitStateDto {

    @Getter
    @Setter
    @NoArgsConstructor
    static public class Request {
        private Long id;
        private Integer state; // 0: 미승인, 1: 승인, 2: 거부
        private String comment; // 거부 시 반려 사유
    }

    @Getter
    @Jacksonized
    @Builder
    static public class Response {
        private Long id;
        private Integer state; // 0: 미승인, 1: 승인, 2: 거부
    }
}
