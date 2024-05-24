package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author chenli5
 */
public class ExportSoftwareWebRequest implements WebRequest {

    /**
     * 软件分组ID
     */
    @ApiModelProperty(value = "软件分组ID", required = true)
    @Nullable
    private UUID groupId;

    @Nullable
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable UUID groupId) {
        this.groupId = groupId;
    }
}
