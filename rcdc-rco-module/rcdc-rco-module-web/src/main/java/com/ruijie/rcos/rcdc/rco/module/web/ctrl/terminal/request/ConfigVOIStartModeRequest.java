package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalStartMode;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月31日
 *
 * @author xwx
 */
public class ConfigVOIStartModeRequest {

    @ApiModelProperty(value = "终端ID数组", required = true)
    @NotNull
    @Size(min = 1)
    private String[] idArr;

    @NotNull
    @ApiModelProperty(value = "VOI启动方式", required = true)
    private CbbTerminalStartMode terminalStartMode;  //前端只会传TC、UEFI

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }

    public CbbTerminalStartMode getTerminalStartMode() {
        return terminalStartMode;
    }

    public void setTerminalStartMode(CbbTerminalStartMode terminalStartMode) {
        this.terminalStartMode = terminalStartMode;
    }
}
