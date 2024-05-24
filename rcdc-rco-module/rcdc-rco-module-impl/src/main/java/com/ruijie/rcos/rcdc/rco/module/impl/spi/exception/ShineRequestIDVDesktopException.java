package com.ruijie.rcos.rcdc.rco.module.impl.spi.exception;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/27
 *
 * @author chen zj
 */
public class ShineRequestIDVDesktopException extends BusinessException {

    private int responseErrorCode;

    public ShineRequestIDVDesktopException(int responseErrorCode) {
        super(String.valueOf(responseErrorCode));
        this.responseErrorCode = responseErrorCode;
    }

    public ShineRequestIDVDesktopException(int responseErrorCode, Throwable e) {
        super(String.valueOf(responseErrorCode), e);
        this.responseErrorCode = responseErrorCode;
    }

    public int getResponseErrorCode() {
        return responseErrorCode;
    }
}
