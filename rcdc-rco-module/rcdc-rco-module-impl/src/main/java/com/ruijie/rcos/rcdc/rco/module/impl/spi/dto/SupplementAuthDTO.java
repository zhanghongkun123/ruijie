package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalWorkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description: 7800终端离线登录补充授权信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月15日
 *
 * @author ypp
 */
public class SupplementAuthDTO {

    /**
     * 产品型号
     */
    @Nullable
    private String productType;

    /**
     * 终端平台型号
     */
    @Nullable
    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums platform;

    /**
     * 终端支持的工作模式
     */
    @Nullable
    private CbbTerminalWorkModeEnums[] terminalWorkSupportModeArr;

    /**
     * 终端本地工作模式
     */
    @Nullable
    private CbbTerminalPlatformEnums workMode;

    @Nullable
    public String getProductType() {
        return productType;
    }

    public void setProductType(@Nullable String productType) {
        this.productType = productType;
    }

    @Nullable
    public CbbTerminalPlatformEnums getPlatform() {
        return platform;
    }

    public void setPlatform(@Nullable CbbTerminalPlatformEnums platform) {
        this.platform = platform;
    }

    @Nullable
    public CbbTerminalWorkModeEnums[] getTerminalWorkSupportModeArr() {
        return terminalWorkSupportModeArr;
    }

    public void setTerminalWorkSupportModeArr(@Nullable CbbTerminalWorkModeEnums[] terminalWorkSupportModeArr) {
        this.terminalWorkSupportModeArr = terminalWorkSupportModeArr;
    }

    @Nullable
    public CbbTerminalPlatformEnums getWorkMode() {
        return workMode;
    }

    public void setWorkMode(@Nullable CbbTerminalPlatformEnums workMode) {
        this.workMode = workMode;
    }
}
