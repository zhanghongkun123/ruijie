package com.ruijie.rcos.rcdc.rco.module.impl.enums;

/**
 * Description: 修改终端服务器地址响应枚举
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/15 19:04
 *
 * @author shiruifeng
 */
public enum TerminalChangeServerIpResponseCode {

    /** 终端云桌面正在运行，不可初始化 */
    WRITE_TERMINAL_SETTING_FAIL(-1);

    /** shine返回状态码 */
    private int code;

    TerminalChangeServerIpResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
