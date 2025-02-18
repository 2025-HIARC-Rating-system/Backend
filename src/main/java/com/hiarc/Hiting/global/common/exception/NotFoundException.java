package com.hiarc.Hiting.global.common.exception;

import com.hiarc.Hiting.global.common.apiPayload.code.BaseErrorCode;

public class NotFoundException extends RuntimeException {

  private final BaseErrorCode code;

  public NotFoundException(BaseErrorCode code) {
    super(code.getReason().getMessage());
    this.code = code;
  }


}

