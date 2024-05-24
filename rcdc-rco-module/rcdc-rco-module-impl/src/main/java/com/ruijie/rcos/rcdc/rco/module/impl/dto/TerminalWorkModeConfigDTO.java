package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalWorkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description: 终端工作模式配置信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月15日
 *
 * @author ypp
 */
public class TerminalWorkModeConfigDTO {

    private String terminalId;

    private String productType;

    private CbbTerminalWorkModeEnums[] terminalWorkSupportModeArr;

    private CbbTerminalPlatformEnums platform;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public CbbTerminalWorkModeEnums[] getTerminalWorkSupportModeArr() {
        return terminalWorkSupportModeArr;
    }

    public void setTerminalWorkSupportModeArr(CbbTerminalWorkModeEnums[] terminalWorkSupportModeArr) {
        this.terminalWorkSupportModeArr = terminalWorkSupportModeArr;
    }

    public CbbTerminalPlatformEnums getPlatform() {
        return platform;
    }

    public void setPlatform(CbbTerminalPlatformEnums platform) {
        this.platform = platform;
    }
}
