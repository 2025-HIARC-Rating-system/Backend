package com.hiarc.Hiting.global.common.apiPayload.code.status;

import com.hiarc.Hiting.global.common.apiPayload.code.BaseCode;
import com.hiarc.Hiting.global.common.apiPayload.code.dto.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 공통 성공
    OK(HttpStatus.OK, "COMMON200", "요청 성공"),
    CREATED(HttpStatus.CREATED, "COMMON201", "생성 완료"),

    // 학회원 관련
    MEMBER_CREATE_SUCCESS(HttpStatus.CREATED, "MEMBER2001", "학회원 등록 성공"),
    MEMBER_DELETE_SUCCESS(HttpStatus.OK, "MEMBER2002", "학회원 삭제 성공")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }






}
