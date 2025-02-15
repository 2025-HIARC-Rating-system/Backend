package com.hiarc.Hiting.global.common.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hiarc.Hiting.global.common.apiPayload.code.BaseCode;
import com.hiarc.Hiting.global.common.apiPayload.code.BaseErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"}) // 순서 지정
public class ApiResponse<T> {

    private boolean isSuccess;
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    //성공한 경우
    public static <T> ApiResponse<T> of(BaseCode code, T result) {
        return new ApiResponse<>(
                true,
                code.getReasonHttpStatus().getCode(),
                code.getReasonHttpStatus().getMessage(),
                result
        );
    }

    //실패한 경우
    public static <T> ApiResponse<T> of(BaseErrorCode code, T result) {
        return new ApiResponse<>(
                false,
                code.getReasonHttpStatus().getCode(),
                code.getReasonHttpStatus().getMessage(),
                result
        );
    }


}

