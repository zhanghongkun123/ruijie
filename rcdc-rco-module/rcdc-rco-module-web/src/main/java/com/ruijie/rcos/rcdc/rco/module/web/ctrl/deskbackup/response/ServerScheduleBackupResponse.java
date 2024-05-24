package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.dto.ServerScheduleBackupDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月25日
 *
 * @author zhanghongkun
 */
@ApiModel("备份信息返回体")
public class ServerScheduleBackupResponse extends ServerScheduleBackupDTO {

    @ApiModelProperty("UUID")
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
