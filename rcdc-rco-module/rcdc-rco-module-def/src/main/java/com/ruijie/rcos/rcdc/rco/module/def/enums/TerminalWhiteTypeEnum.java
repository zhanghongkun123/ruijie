package com.ruijie.rcos.rcdc.rco.module.def.enums;

import org.springframework.util.Assert;

/**
 * Description: 终端白名单枚举
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/7 16:37
 *
 * @author conghaifeng
 */
public enum TerminalWhiteTypeEnum {

    /**
     * 终端类型
     */
    RG_RAIN305_256("RG-Rain305(256)"),

    RG_CT5200("RG-CT5200"),

    RG_CT5300("RG-CT5300"),

    RG_CT6200("RG-CT6200"),

    RG_CT6300("RG-CT6300"),

    RG_CT6500("RG-CT6500"),

    RG_CT5530S("RG-CT5530S"),

    RG_CT5330S("RG-CT5330S"),

    RG_CT5320S("RG-CT5320S"),

    RG_CT5200S("RG-CT5200S"),

    RG_RAIN405_256("RG-Rain405(256)"),

    RG_RAIN400W("RG-Rain400W"),

    RG_RAIN410W("RG-Rain410W"),

    RG_RAIN400W_V2("RG-Rain400W V2"),

    RG_RAIN310_256("RG-Rain310(256)"),

    RG_RAIN300W("RG-Rain300W"),

    RG_RAIN320_256("RG-Rain320(256)"),

    RG_RAIN310W("RG-Rain310W"),

    RG_RAIN300W_V2("RG-Rain300W V2"),

    RG_RAIN305W("RG-Rain305W"),

    RG_RAIN310W_V2("RG-Rain310W V2"),

    RG_RAIN310W_500HD("RG-Rain310W(500HD)"),

    RG_RAIN320W("RG-Rain320W"),

    RG_CT5330M("RG-CT5330M"),

    RG_CT5200C_G3("RG-CT5200C-G3"),

    RG_CT5300C_G3("RG-CT5300C-G3"),

    RG_CT5500C_G3("RG-CT5500C-G3"),

    RG_CT6200C_G3("RG-CT6200C-G3"),

    RG_CT6300C_G3("RG-CT6300C-G3"),

    RG_CT6500C_G3("RG-CT6500C-G3"),

    RG_CT5300C_CS("RG-CT5300C-CS"),

    RG_CT5300C("RG-CT5300C"),

    RG_CT5702C_G3("RG-CT5702C-G3"),

    RG_CT_3100L_G2("RG-CT3100L-G2"),

    RG_CT_3100C_G2("RG-CT3100C-G2"),

    RG_CT5502C_G3("RG-CT5502C-G3"),

    RG_CT5200C_G4("RG-CT5200C-G4"),

    RG_CT5300C_G4("RG-CT5300C-G4"),

    RG_CT5500C_G4("RG-CT5500C-G4"),

    RG_CT6200C_G4("RG-CT6200C-G4"),

    RG_CT6300C_G4("RG-CT6300C-G4"),

    RG_CT6500C_G4("RG-CT6500C-G4"),

    RG_CT5302C_G4("RG-CT5302C-G4"),

    RG_CT5502C_G4("RG-CT5502C-G4"),

    RG_CT5702C_G4("RG-CT5702C-G4"),

    RG_CT5500C_CS("RG-CT5500C-CS"),

    RG_RAIN310W_V2_V210("RG-Rain310W V2 V2.10");

    private String terminalType;

    TerminalWhiteTypeEnum(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalType() {
        return terminalType;
    }


    /**
     * 判断终端是否包含在终端列表中
     * 
     * @param terminalType 终端类型名称
     * @return 终端类型是否包含在白名单里面
     */
    public static boolean isInTerminalWhiteList(String terminalType) {
        Assert.hasText(terminalType, "terminalType can not be blank");
        for (TerminalWhiteTypeEnum terminalWhite : TerminalWhiteTypeEnum.values()) {
            if (terminalType.equals(terminalWhite.getTerminalType())) {
                return true;
            }
        }
        return false;
    }



}
