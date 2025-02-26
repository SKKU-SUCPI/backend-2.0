package com.skku.sucpi.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SSOUserDto {

    private String userId;
    private String userName;
    private String hakbun; // 학번 or 교직원 번호
    private String department;
    private String degree;
    private String status;
    private String group;
    private String role;

}
