package com.skku.sucpi.dto.submit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SubmitCommentDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema (name = "SubmitCommentDto_Request")
    static public class Request {
        private Long id;
        private String content;
    }

    @Getter
    @Builder
    @Schema (name = "SubmitCommentDto_Response")
    static public class Response {
        private Long id;
        private String content;
    }
}
