package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.UUID;

/**
 * Description: 机柜配置服务器信息DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/8 20:22
 *
 * @author BaiGuoliang
 */
public class CabinetMappingServerDTO {

    /**
     * 机柜Id
     */
    private UUID cabinetId;

    /**
     * 服务器Id
     */
    private UUID serverId;

    private Integer cabinetLocationBegin;

    private Integer cabinetLocationEnd;

    private String serverIp;

    private String serverName;

    public UUID getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(UUID cabinetId) {
        this.cabinetId = cabinetId;
    }

    public UUID getServerId() {
        return serverId;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public Integer getCabinetLocationBegin() {
        return cabinetLocationBegin;
    }

    public void setCabinetLocationBegin(Integer cabinetLocationBegin) {
        this.cabinetLocationBegin = cabinetLocationBegin;
    }

    public Integer getCabinetLocationEnd() {
        return cabinetLocationEnd;
    }

    public void setCabinetLocationEnd(Integer cabinetLocationEnd) {
        this.cabinetLocationEnd = cabinetLocationEnd;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
