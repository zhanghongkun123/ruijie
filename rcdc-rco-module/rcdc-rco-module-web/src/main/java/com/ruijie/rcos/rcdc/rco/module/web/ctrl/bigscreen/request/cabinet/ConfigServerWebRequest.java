package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import java.util.UUID;

/**
 * Description: 配置服务器提交的数据
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 15:16
 *
 * @author BaiGuoliang
 */
public class ConfigServerWebRequest implements WebRequest {

    @NotNull
    private UUID cabinetId;

    @NotNull
    private UUID serverId;

    @NotNull
    private Integer cabinetLocationBegin;

    @NotNull
    private Integer cabinetLocationEnd;

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
}
