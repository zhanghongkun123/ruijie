package com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月08日
 *
 * @author jarman
 */
public class UserIdListDTO {

    @NotNull
    private List<UUID> userIdList;

    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<UUID> userIdList) {
        this.userIdList = userIdList;
    }
}
