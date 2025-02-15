package com.hiarc.Hiting.global.common.apiPayload.code;

import com.hiarc.Hiting.global.common.apiPayload.code.dto.ErrorReasonDTO;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();
    public ErrorReasonDTO getReasonHttpStatus();
}
