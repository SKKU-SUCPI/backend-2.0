package com.skku.sucpi.dto.fileStorage;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
public class FileInfoDto {

    private Long id;
    private String fileName;
    private String fileType;
}
