package com.skku.sucpi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Jacksonized
@Builder
public class PaginationDto<T> {

    private List<T> content;
    private int page;
    private long totalPage;
    private int size;
    private long totalElements;

    public static <T> PaginationDtoBuilder<T> builder() {
        return new PaginationDtoBuilder<T>();
    }
}
