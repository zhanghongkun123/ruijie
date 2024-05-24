package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCreateDeskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateDesktopPoolRequest;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/5
 *
 * @author linke
 */
public class PoolSessionConfigDTO {

    private CbbDesktopSessionType sessionType;

    private Integer maxSession;

    private Integer idleDesktopRecover;

    public PoolSessionConfigDTO(CbbCreateDeskPoolDTO createDeskPoolDTO) {
        Assert.notNull(createDeskPoolDTO, "createDeskPoolDTO must not null");
        this.setIdleDesktopRecover(createDeskPoolDTO.getIdleDesktopRecover());
    }

    public PoolSessionConfigDTO(UpdateDesktopPoolRequest request) {
        Assert.notNull(request, "request must not null");
        this.setIdleDesktopRecover(request.getCbbDesktopPoolDTO().getIdleDesktopRecover());
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Integer getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(Integer maxSession) {
        this.maxSession = maxSession;
    }

    public Integer getIdleDesktopRecover() {
        return idleDesktopRecover;
    }

    public void setIdleDesktopRecover(Integer idleDesktopRecover) {
        this.idleDesktopRecover = idleDesktopRecover;
    }
}
