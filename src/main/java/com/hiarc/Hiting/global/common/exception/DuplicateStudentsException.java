package com.hiarc.Hiting.global.common.exception;

import com.hiarc.Hiting.global.common.apiPayload.code.BaseErrorCode;
import com.hiarc.Hiting.global.common.apiPayload.code.dto.ErrorReasonDTO;

import java.util.List;

public class DuplicateStudentsException extends RuntimeException {

    private final BaseErrorCode code;
    private final List<String> duplicateHandles; // 중복된 handle 목록 등

    public DuplicateStudentsException(BaseErrorCode code, List<String> duplicateHandles) {
        super(code.getReason().getMessage());
        this.code = code;
        this.duplicateHandles = duplicateHandles;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }

    public List<String> getDuplicateHandles() {
        return duplicateHandles;
    }

}
