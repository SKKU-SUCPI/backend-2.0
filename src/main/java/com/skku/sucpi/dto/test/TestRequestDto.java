package com.skku.sucpi.dto.test;

import com.skku.sucpi.entity.TestEntity;
import lombok.*;

/**
 * request: setter 사용
 * query parameter, form-data, body 모두 setter 사용 가능
 */
@Getter
@Setter
@NoArgsConstructor
public class TestRequestDto {

    private String title;
    private String content;
}
