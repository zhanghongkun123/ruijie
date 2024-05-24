package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCreateDeskPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateDesktopPoolRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/4
 *
 * @author linke
 */
public class DesktopPoolDesktopConfigDTO {

    private Integer desktopNum;

    private Integer preStartDesktopNum;

    private UUID imageTemplateId;

    private UUID platformId;

    private UUID clusterId;

    private CbbDeskSpecDTO deskSpec;

    private UUID networkId;

    private UUID strategyId;

    public DesktopPoolDesktopConfigDTO(CbbCreateDeskPoolDTO cbbCreateDeskPoolDTO) {
        Assert.notNull(cbbCreateDeskPoolDTO, "cbbCreateDeskPoolDTO must not null");
        BeanUtils.copyProperties(cbbCreateDeskPoolDTO, this);
        if (Objects.nonNull(cbbCreateDeskPoolDTO.getDeskSpec())) {
            CbbDeskSpecDTO cbbDeskSpecDTO = new CbbDeskSpecDTO();
            BeanUtils.copyProperties(cbbCreateDeskPoolDTO.getDeskSpec(), cbbDeskSpecDTO);
            this.setDeskSpec(cbbDeskSpecDTO);
        }
    }

    public DesktopPoolDesktopConfigDTO(UpdateDesktopPoolRequest request) {
        Assert.notNull(request, "request must not null");
        BeanUtils.copyProperties(request.getCbbDesktopPoolDTO(), this);
        if (Objects.nonNull(request)) {
            this.setDeskSpec(request.getCbbDeskSpecDTO());
        }
    }

    public Integer getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(Integer desktopNum) {
        this.desktopNum = desktopNum;
    }

    public Integer getPreStartDesktopNum() {
        return preStartDesktopNum;
    }

    public void setPreStartDesktopNum(Integer preStartDesktopNum) {
        this.preStartDesktopNum = preStartDesktopNum;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public CbbDeskSpecDTO getDeskSpec() {
        return deskSpec;
    }

    public void setDeskSpec(CbbDeskSpecDTO deskSpec) {
        this.deskSpec = deskSpec;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }
}
