package com.ruijie.rcos.rcdc.rco.module.def.enums;

import java.util.Arrays;

/**
 * Description: 终端临时跳过授权类型
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月09日
 *
 * @author xwx
 */
public enum TerminalTempSkipAuthTypeEnum {

    RG_CT3120("RG-CT3120"),

    RG_CT3100_G2("RG-CT3100-G2");

    private String terminalType;

    TerminalTempSkipAuthTypeEnum(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalType() {
        return terminalType;
    }


    /**
     * 判断终端型号是否在临时跳过授权列表中
     * @param terminalType 终端类型
     * @return 是否存在
     */
    public static boolean isInTerminalTempSkipAuthList(String terminalType) {
        return Arrays.stream(TerminalTempSkipAuthTypeEnum.values()).anyMatch(type -> type.getTerminalType().equals(terminalType));
    }
}
