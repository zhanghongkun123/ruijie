package com.ruijie.rcos.rcdc.rco.module.def.api.request.desk;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.Size;

import java.util.List;
import java.util.UUID;

/**
 * Description: 桌面状态查询列表
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-14 20:40:00
 *
 * @author zjy
 */
public class DeskStateInfoRequest {

    @NotEmpty
    @Size(max = 1000)
    private List<UUID> deskIdList;

    public List<UUID> getDeskIdList() {
        return deskIdList;
    }

    public void setDeskIdList(List<UUID> deskIdList) {
        this.deskIdList = deskIdList;
    }
}
