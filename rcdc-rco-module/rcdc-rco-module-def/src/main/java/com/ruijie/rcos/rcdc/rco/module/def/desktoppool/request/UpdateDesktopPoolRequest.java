package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolConfigDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/5
 *
 * @author linke
 */
public class UpdateDesktopPoolRequest {

    @NotNull
    private CbbDesktopPoolDTO cbbDesktopPoolDTO;

    @NotNull
    private Boolean needUpdateConfig = false;

    @Nullable
    private CbbDeskSpecDTO cbbDeskSpecDTO;

    @Nullable
    private DesktopPoolConfigDTO desktopPoolConfigDTO;

    public CbbDesktopPoolDTO getCbbDesktopPoolDTO() {
        return cbbDesktopPoolDTO;
    }

    public void setCbbDesktopPoolDTO(CbbDesktopPoolDTO cbbDesktopPoolDTO) {
        this.cbbDesktopPoolDTO = cbbDesktopPoolDTO;
    }

    public Boolean getNeedUpdateConfig() {
        return needUpdateConfig;
    }

    public void setNeedUpdateConfig(Boolean needUpdateConfig) {
        this.needUpdateConfig = needUpdateConfig;
    }

    @Nullable
    public CbbDeskSpecDTO getCbbDeskSpecDTO() {
        return cbbDeskSpecDTO;
    }

    public void setCbbDeskSpecDTO(@Nullable CbbDeskSpecDTO cbbDeskSpecDTO) {
        this.cbbDeskSpecDTO = cbbDeskSpecDTO;
    }

    @Nullable
    public DesktopPoolConfigDTO getDesktopPoolConfigDTO() {
        return desktopPoolConfigDTO;
    }

    public void setDesktopPoolConfigDTO(@Nullable DesktopPoolConfigDTO desktopPoolConfigDTO) {
        this.desktopPoolConfigDTO = desktopPoolConfigDTO;
    }
}
