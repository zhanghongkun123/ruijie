package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import org.springframework.util.Assert;

/**
 * Description: DesktopStateNumDTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-08
 *
 * @author hli
 */
public class DesktopStateNumDTO {

    private CbbCloudDeskState deskState;

    private Long num;

    public DesktopStateNumDTO(String deskState, Long num) {
        Assert.hasText(deskState, "deskState cannot be null.");
        Assert.notNull(num, "num cannot be null.");
        this.deskState = CbbCloudDeskState.valueOf(deskState);
        this.num = num;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

}
