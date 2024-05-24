package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import org.springframework.util.Assert;

/**
 * Description: 需要进行授权的MINI服务器型号
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/12 20:11
 *
 * @author yxq
 */
public enum NeedAuthMniServerEnum {

    /**
     * RG-CT5300C-CS型号
     */
    RG_CT5300C_CS("RG-CT5300C-CS"),

    RG_CT5500C_CS("RG-CT5500C-CS");

    private String productName;

    NeedAuthMniServerEnum(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    /**
     * 根据产品型号名，获取对应的枚举类
     *
     * @param productName 型号
     * @return 对应枚举类
     */
    public static NeedAuthMniServerEnum getByProductName(String productName) {
        Assert.notNull(productName, "productName must not be null");

        for (NeedAuthMniServerEnum needAuthMniServerEnum : values()) {
            if (needAuthMniServerEnum.getProductName().equals(productName)) {
                return needAuthMniServerEnum;
            }
        }

        // 没有匹配的枚举值
        return null;
    }
}
