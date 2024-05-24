
package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskStrategyState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/13 20:22
 *
 * @author linke
 */
public class RestDeskStrategyDTO extends EqualsHashcodeSupport {

    private UUID id;

    private String strategyName;

    /**
     * 创建者登录账号
     */
    private String  creatorUserName;

    private CbbDeskStrategyState deskStrategyState;

    private CbbStrategyType strategyType;

    private CbbCloudDeskPattern desktopType;

    private Integer systemDisk;

    private Integer cloudNumber;

    /**
     * 云桌面标签
     */
    private String remark;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public Integer getCloudNumber() {
        return cloudNumber;
    }

    public void setCloudNumber(Integer cloudNumber) {
        this.cloudNumber = cloudNumber;
    }

    public CbbDeskStrategyState getDeskStrategyState() {
        return deskStrategyState;
    }

    public void setDeskStrategyState(CbbDeskStrategyState deskStrategyState) {
        this.deskStrategyState = deskStrategyState;
    }

    public CbbStrategyType getStrategyType() {
        return strategyType;
    }

    public void setStrategyType(CbbStrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

}
