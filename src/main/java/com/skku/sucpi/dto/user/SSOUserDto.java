package com.skku.sucpi.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SSOUserDto {
    private String userName;
    private String hakbun; // 학번 or 교직원 번호
    private String department;
    private Float hakgwaCd;
    private String degree;
    private String role;
}
