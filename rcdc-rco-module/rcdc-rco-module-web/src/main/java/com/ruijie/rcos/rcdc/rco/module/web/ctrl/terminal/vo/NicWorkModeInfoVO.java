package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNicWorkModeEnums;

/**
 * Description: 返回给前端网卡工作模式信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/20 15:11
 *
 * @author yxq
 */
public class NicWorkModeInfoVO {

    private CbbNicWorkModeEnums nicWorkMode;

    public NicWorkModeInfoVO(CbbNicWorkModeEnums nicWorkMode) {
        this.nicWorkMode = nicWorkMode;
    }

    public CbbNicWorkModeEnums getNicWorkMode() {
        return nicWorkMode;
    }

    public void setNicWorkMode(CbbNicWorkModeEnums nicWorkMode) {
        this.nicWorkMode = nicWorkMode;
    }
}
