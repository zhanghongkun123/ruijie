package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

/**
 * Description: 用户组下绑定磁盘数量
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/30
 *
 * @author TD
 */
public class UserGroupBindDiskNumDTO {

    @JSONField(name = "group_id")
    private UUID groupId;

    @JSONField(name = "bind_disk_num")
    private Integer bindDiskNum;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public Integer getBindDiskNum() {
        return bindDiskNum;
    }

    public void setBindDiskNum(Integer bindDiskNum) {
        this.bindDiskNum = bindDiskNum;
    }
}
