package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import java.util.UUID;

/**
 * Description: 云桌面DTO（大屏）
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author zhangyichi
 */
public class BigScreenCloudDesktopDTO {

    @NotBlank
    private UUID userId;

    @NotBlank
    private UUID desktopId;

    @NotBlank
    private String desktopName;

    @NotBlank
    private String status;

    public BigScreenCloudDesktopDTO() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigScreenCloudDesktopDTO(CloudDesktopDTO dto) {
        this.desktopId = dto.getId();
        this.desktopName = dto.getDesktopName();
        this.status = dto.getDesktopState();
    }
}
