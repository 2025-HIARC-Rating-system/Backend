package com.hiarc.Hiting.global.common.exception;

import com.hiarc.Hiting.global.common.apiPayload.code.BaseErrorCode;
import com.hiarc.Hiting.global.common.apiPayload.code.dto.ErrorReasonDTO;

public class GeneralException extends RuntimeException {


    private final BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super(code.getReason().getMessage());
        this.code = code;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }
    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
