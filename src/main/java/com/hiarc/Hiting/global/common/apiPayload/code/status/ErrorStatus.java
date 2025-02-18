package com.hiarc.Hiting.global.common.apiPayload.code.status;

import com.hiarc.Hiting.global.common.apiPayload.code.BaseErrorCode;
import com.hiarc.Hiting.global.common.apiPayload.code.dto.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    //기본 응답
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    NOT_CORRECT_PERIOD(HttpStatus.BAD_REQUEST, "COMMON4001", "해당 기간이 아닙니다."),

    //학회원 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "학회원이 존재하지 않습니다."),
    MEMBER_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 존재하는 학회원입니다."),
    MEMBER_INFO_INVALID(HttpStatus.BAD_REQUEST, "MEMBER4003", "학회원 정보 형식이 잘못되었습니다."),
    MEMBER_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER5002", "학회원 삭제에 실패하였습니다."),

    //정보 불러오기 관련
    OPEN_API_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "OPENAPI5001", "오픈 API 정보 불러오기 실패"),

    //날짜 관련
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "DATE4001", "날짜 형식이 올바르지 않습니다."),
    DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "DATE4002", "DATE entity가 존재하지 않습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .build();
    }


}
