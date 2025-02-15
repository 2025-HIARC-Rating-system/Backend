package com.hiarc.Hiting.global.common.apiPayload.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ErrorReasonDTO {

    private boolean isSuccess;
    private String code;
    private String message;
    private HttpStatus httpStatus;

}
