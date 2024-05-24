package com.ruijie.rcos.rcdc.rco.module.def.enums;

import com.google.common.base.Objects;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 终端可部署为瘦终端，也可部署为胖终端名单，此类终端在check_upgrade不进行授权，部署时再授权
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月24日
 *
 * @author ypp
 */
public enum TerminalSupportVdiAndIdvEnum {

    RG_CT7800_1000("RG-CT7800-1000");

    private String productType;

    TerminalSupportVdiAndIdvEnum(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public static List<String> getProductTypeList() {
        return Arrays.stream(TerminalSupportVdiAndIdvEnum.values()).map(TerminalSupportVdiAndIdvEnum::getProductType).collect(Collectors.toList());
    }

    /**
     * 终端是否支持vdi、且支持idv
     * 
     * @param productType 产品型号
     * @return boolean
     */
    public static boolean hasTerminalSupportVdiAndIdv(@Nullable String productType) {

        return Arrays.stream(TerminalSupportVdiAndIdvEnum.values()).anyMatch(item -> Objects.equal(item.getProductType(),productType));
    }
}
