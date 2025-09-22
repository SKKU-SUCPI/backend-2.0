package com.skku.sucpi.dto.comment;

import com.skku.sucpi.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {

    @Getter
    @Builder
    @Schema (name = "CommentDto_BasicInfo")
    static public class BasicInfo {
        private Long id;
        private String content;
        private Integer state;
        private LocalDateTime date;
    }

    static public List<CommentDto.BasicInfo> from(List<Comment> comments) {

        if (comments == null) {
            return List.of();
        }

        return comments.stream().map(CommentDto::from).toList();
    }

    static public CommentDto.BasicInfo from(Comment comment) {
        return BasicInfo.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .state(comment.getState())
                .date(comment.getDate())
                .build();
    }
}
