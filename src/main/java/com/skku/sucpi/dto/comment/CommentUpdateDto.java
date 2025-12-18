package com.skku.sucpi.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CommentUpdateDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @Schema (name = "CommentUpdateDto_Request")
    static public class Request {
        private Long id;
        private String content;
    }

    @Getter
    @Builder
    @Schema (name = "CommentUpdateDto_Response")
    static public class Response {
        private Long id;
        private String content;
    }
}
