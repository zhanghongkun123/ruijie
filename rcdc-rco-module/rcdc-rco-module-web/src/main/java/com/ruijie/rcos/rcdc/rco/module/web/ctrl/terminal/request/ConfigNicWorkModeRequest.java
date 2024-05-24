package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNicWorkModeEnums;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/20 10:26
 *
 * @author yxq
 */
public class ConfigNicWorkModeRequest {

    @ApiModelProperty(value = "ID数组", required = true)
    @NotEmpty
    @Size(min = 1)
    private String[] idArr;

    @ApiModelProperty(value = "启动模式", required = true)
    @NotNull
    private CbbNicWorkModeEnums nicWorkMode;

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }

    public CbbNicWorkModeEnums getNicWorkMode() {
        return nicWorkMode;
    }

    public void setNicWorkMode(CbbNicWorkModeEnums nicWorkMode) {
        this.nicWorkMode = nicWorkMode;
    }
}
