package com.skku.sucpi.dto.test;

import lombok.*;

/**
 * builder pattern
 * 생성자 arg 순서를 고려안해도 됨
 * setter 보다 디버깅이 쉬움
 */
@Getter
@RequiredArgsConstructor
@Builder
public class TestResponseDto {

    private final Long id;
    private final String title;
    private final String content;
}
