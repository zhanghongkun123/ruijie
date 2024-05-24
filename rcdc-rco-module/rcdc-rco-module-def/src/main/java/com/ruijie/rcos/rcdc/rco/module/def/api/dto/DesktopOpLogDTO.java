package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopOpLogDTO;
import org.springframework.lang.Nullable;

/**
 * Description: 云桌面审计日志API
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-1-14
 * 
 * @author artom
 */
public class DesktopOpLogDTO extends CbbDesktopOpLogDTO {

    @Nullable
    private String terminalId;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
