package com.ruijie.rcos.rcdc.rco.module.def.terminaloperate.exception;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/22
 *
 * @author wjp
 */
public class TerminalOperateSuccessBusinessException extends BusinessException {

    private static final long serialVersionUID = 978533564078438498L;

    public TerminalOperateSuccessBusinessException(String key, String... args) {
        super(key, args);
    }

    public TerminalOperateSuccessBusinessException(String key, Throwable throwable, String... args) {
        super(key, throwable, args);
    }
}
