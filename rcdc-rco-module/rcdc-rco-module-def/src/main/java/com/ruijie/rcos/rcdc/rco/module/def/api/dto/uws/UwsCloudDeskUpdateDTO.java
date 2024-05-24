package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.uws.UwsCloudDestOpearteTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 云桌面更新信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-18 11:19:00
 *
 * @author zjy
 */
public class UwsCloudDeskUpdateDTO extends UwsBaseDTO {

    /**
     * 桌面绑定账号id，公共桌面可能为空
     */
    @Nullable
    private UUID uuid;

    /**
     * 桌面信息
     */
    @NotNull
    private UwsDesktopDTO desktop;

    /**
     * 桌面状态
     */
    @NotBlank
    private String status;

    /**
     * 操作类型
     */
    @NotNull
    private UwsCloudDestOpearteTypeEnum opearteType;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UwsDesktopDTO getDesktop() {
        return desktop;
    }

    public void setDesktop(UwsDesktopDTO desktop) {
        this.desktop = desktop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UwsCloudDestOpearteTypeEnum getOpearteType() {
        return opearteType;
    }

    public void setOpearteType(UwsCloudDestOpearteTypeEnum opearteType) {
        this.opearteType = opearteType;
    }
}
