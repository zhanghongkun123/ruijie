package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

/**
 * Description: 删除临时权限
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/8
 *
 * @author linke
 */
public class DeleteDesktopTempPermissionRequest {

    @ApiModelProperty(value = "临时权限ID列表", required = true)
    @NotEmpty
    @Size(min = 1)
    private List<UUID> idList;

    public List<UUID> getIdList() {
        return idList;
    }

    public void setIdList(List<UUID> idList) {
        this.idList = idList;
    }
}
