package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums;

import org.springframework.util.Assert;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 * 
 * @author Ghang
 */
public enum SpiceDisplayStrategyType {

    DEFAULT(""),
    SMOOTH("1"),
    HD("2"),
    LOSSLESS("3");
    SpiceDisplayStrategyType(String code) {
        this.code = code;
    }
    
    private String code;

    public String getCode() {
        return code;
    }

    /**
     * 通过code找枚举
     * @param code  
     * @return SpiceShowStrategyType  
     */
    public static SpiceDisplayStrategyType findEnumByCode(String code) {
        Assert.notNull(code, "code is not null");
        switch (code) {
            case "":
                return DEFAULT;
            case "1":
                return SMOOTH;
            case "2":
                return HD;
            case "3":
                return LOSSLESS;
            default:
                return DEFAULT;
        }
    }
    
}
