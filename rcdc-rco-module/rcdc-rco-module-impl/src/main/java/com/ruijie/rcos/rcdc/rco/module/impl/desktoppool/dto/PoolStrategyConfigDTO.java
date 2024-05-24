package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCreateDeskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateDesktopPoolRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/20
 *
 * @author linke
 */
public class PoolStrategyConfigDTO {

    private UUID strategyId;

    private UUID networkId;

    private UUID softwareStrategyId;

    private UUID userProfileStrategyId;

    private CbbDesktopPoolModel poolModel;

    private UUID imageTemplateId;

    private Integer personSize;

    private CbbDesktopSessionType sessionType;

    private CbbDesktopPoolType poolType;

    public PoolStrategyConfigDTO(CbbCreateDeskPoolDTO cbbCreateDeskPoolDTO) {
        Assert.notNull(cbbCreateDeskPoolDTO, "cbbCreateDeskPoolDTO must not null");
        BeanUtils.copyProperties(cbbCreateDeskPoolDTO, this);
        personSize = Objects.nonNull(cbbCreateDeskPoolDTO.getDeskSpec()) ? cbbCreateDeskPoolDTO.getDeskSpec().getPersonSize() : null;
    }

    public PoolStrategyConfigDTO(UpdateDesktopPoolRequest request) {
        Assert.notNull(request, "request must not null");
        BeanUtils.copyProperties(request.getCbbDesktopPoolDTO(), this);
        if (Objects.nonNull(request.getDesktopPoolConfigDTO())) {
            this.setSoftwareStrategyId(request.getDesktopPoolConfigDTO().getSoftwareStrategyId());
            this.setUserProfileStrategyId(request.getDesktopPoolConfigDTO().getUserProfileStrategyId());
        }
        personSize = Objects.nonNull(request.getCbbDeskSpecDTO()) ? request.getCbbDeskSpecDTO().getPersonSize() : null;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public CbbDesktopPoolModel getPoolModel() {
        return poolModel;
    }

    public void setPoolModel(CbbDesktopPoolModel poolModel) {
        this.poolModel = poolModel;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public Integer getPersonSize() {
        return personSize;
    }

    public void setPersonSize(Integer personSize) {
        this.personSize = personSize;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public CbbDesktopPoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(CbbDesktopPoolType poolType) {
        this.poolType = poolType;
    }
}
