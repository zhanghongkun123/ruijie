package com.ruijie.rcos.rcdc.rco.module.def.api.request.user;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.Size;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户列表查询
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-12-10 15:37:00
 *
 * @author zjy
 */
public class UserInfoListRequest {

    @NotEmpty
    @Size(max = 1000)
    private List<UUID> userIdList;

    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<UUID> userIdList) {
        this.userIdList = userIdList;
    }
}
