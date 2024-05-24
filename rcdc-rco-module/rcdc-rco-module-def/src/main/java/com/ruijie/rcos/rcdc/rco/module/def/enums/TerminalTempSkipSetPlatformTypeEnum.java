package com.ruijie.rcos.rcdc.rco.module.def.enums;

import java.util.Arrays;

/**
 * Description: skip set platform
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/23
 *
 * @author xwx
 */
public enum TerminalTempSkipSetPlatformTypeEnum {

    RG_CT3100L_G2("RG-CT3100L-G2"),

    RG_CT3100C_G2("RG-CT3100C-G2"),

    RG_CT3120("RG-CT3120"),

    RG_CT3100_G2("RG-CT3100-G2");

    private String terminalType;

    TerminalTempSkipSetPlatformTypeEnum(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalType() {
        return terminalType;
    }


    /**
     * skip set platform
     * @param terminalType terminal
     * @return boolean
     */
    public static boolean isInTerminalTempSkipSetPlatformList(String terminalType) {
        return Arrays.stream(TerminalTempSkipSetPlatformTypeEnum.values()).anyMatch(type -> type.getTerminalType().equals(terminalType));
    }
}
