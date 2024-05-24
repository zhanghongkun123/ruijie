package com.ruijie.rcos.rcdc.rco.module.def.enums;


/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 * 
 * @author Ghang
 */
public enum OperationControlType {

    DEFAULT(""),
    OPERATION_ONE("50#150"),
    OPERATION_TWO("0#150"),
    CUSTOM("###");
    OperationControlType(String code) {
        this.code = code;
    }
    
    private String code;

    public String getCode() {
        return code;
    }
    
    /**
     * 通过code找枚举
     * @param code 
     * @return OperationControlType 
     */
    public static OperationControlType findEnumByCode(String code) {
        switch (code) {
            case "":
                return OperationControlType.DEFAULT;
            case "50#150":
                return OperationControlType.OPERATION_ONE;
            case "0#150":
                return OperationControlType.OPERATION_TWO;
            default:
                return OperationControlType.CUSTOM;
        }
    }
    
}
