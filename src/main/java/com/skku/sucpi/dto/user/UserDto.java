package com.skku.sucpi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;


public class UserDto {
    @Getter
    @Jacksonized
    @Builder
    static public class Response {
        private Long id;
        private String name;
        private String department;
        private String studentId;
        private String role;
    }
}
