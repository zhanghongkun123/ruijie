package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskStrategyState;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy.DeskStrategyIDVBaseWebRequest;

/**
 * Description: IDV云桌面策略前端返回VO类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/6
 *
 * @author songxiang
 */
public class DeskStrategyIDVVO extends DeskStrategyIDVBaseWebRequest {

    private Integer cloudNumber;

    private UUID id;

    private Boolean canUsed;

    private CbbDeskStrategyState deskStrategyState;

    /**
     * 根据DTO对象转换成VO对象
     * 
     * @param dto 策略的DTO对象
     * @return VO对象
     */
    public static DeskStrategyIDVVO buildByDTO(CbbDeskStrategyIDVDTO dto) {
        Assert.notNull(dto, "dto must be not null");
        DeskStrategyIDVVO deskStrategyIDVVO = new DeskStrategyIDVVO();
        // canUsed字段后期需要额外增加判断
        deskStrategyIDVVO.setCanUsed(dto.getState() == CbbDeskStrategyState.AVAILABLE);

        deskStrategyIDVVO.setId(dto.getId());
        deskStrategyIDVVO.setName(dto.getName());
        deskStrategyIDVVO.setDesktopType(dto.getPattern());
        deskStrategyIDVVO.setSystemDisk(dto.getSystemSize());
        deskStrategyIDVVO.setUsbTypeIdArr(dto.getUsbTypeIdArr());
        deskStrategyIDVVO.setEnableUsbReadOnly(dto.getOpenUsbReadOnly());
        deskStrategyIDVVO.setEnableAllowLocalDisk(dto.getAllowLocalDisk());
        deskStrategyIDVVO.setCloudNumber(dto.getRefCount());
        deskStrategyIDVVO.setDeskStrategyState(dto.getState());
        deskStrategyIDVVO.setEnableOpenDesktopRedirect(dto.getOpenDesktopRedirect());
        return deskStrategyIDVVO;
    }


    public Integer getCloudNumber() {
        return cloudNumber;
    }

    public void setCloudNumber(Integer cloudNumber) {
        this.cloudNumber = cloudNumber;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getCanUsed() {
        return canUsed;
    }

    public void setCanUsed(Boolean canUsed) {
        this.canUsed = canUsed;
    }

    public CbbDeskStrategyState getDeskStrategyState() {
        return deskStrategyState;
    }

    public void setDeskStrategyState(CbbDeskStrategyState deskStrategyState) {
        this.deskStrategyState = deskStrategyState;
    }
}
