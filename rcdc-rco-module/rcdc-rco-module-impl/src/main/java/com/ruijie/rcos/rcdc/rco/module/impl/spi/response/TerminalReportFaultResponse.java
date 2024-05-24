package com.ruijie.rcos.rcdc.rco.module.impl.spi.response;

/**
 * Description: 终端报障返回消息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月14日
 *
 * @author xwx
 */
public class TerminalReportFaultResponse {
    private String message;

    public TerminalReportFaultResponse() {

    }

    public TerminalReportFaultResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
