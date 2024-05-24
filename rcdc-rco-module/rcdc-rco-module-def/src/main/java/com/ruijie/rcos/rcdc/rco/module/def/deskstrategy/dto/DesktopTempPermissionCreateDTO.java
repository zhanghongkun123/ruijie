package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbCreateDesktopTempPermissionDTO;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 创建云桌面临时权限DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-05-08
 *
 * @author linke
 */
public class DesktopTempPermissionCreateDTO extends CbbCreateDesktopTempPermissionDTO {

    @Nullable
    private List<UUID> desktopIdList;

    @Nullable
    private List<UUID> userIdList;

    @Nullable
    public List<UUID> getDesktopIdList() {
        return desktopIdList;
    }

    public void setDesktopIdList(@Nullable List<UUID> desktopIdList) {
        this.desktopIdList = desktopIdList;
    }

    @Nullable
    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(@Nullable List<UUID> userIdList) {
        this.userIdList = userIdList;
    }
}
