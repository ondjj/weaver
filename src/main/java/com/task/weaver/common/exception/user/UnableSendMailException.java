package com.task.weaver.common.exception.user;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class UnableSendMailException extends BusinessException {

    public UnableSendMailException(Throwable cause) {
        super(ErrorCode.PROJECT_NOT_FOUND, cause);
    }
}
