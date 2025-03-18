package com.skku.sucpi.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success; // 요청 성공 여부
    private final String message; // 응답 메세지
    private final T data; // 응답 데이터
    private final String path; // API 경로

    public static <T> ApiResponse<T> success(T data, String path) {
        return new ApiResponse<>(true, "Request Successful", data, path);
    }

    public static <T> ApiResponse<T> error(String message, String path) {
        return new ApiResponse<>(false, message, null, path);
    }

    public ApiResponse(boolean success, String message, T data, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.path = path;
    }
}
