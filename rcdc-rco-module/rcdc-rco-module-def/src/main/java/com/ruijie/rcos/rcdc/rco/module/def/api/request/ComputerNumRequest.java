package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: pc在线离线情况请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/7 16:39
 *
 * @author conghaifeng
 */
public class ComputerNumRequest implements Request {

    @Nullable
    private UUID[] groupIdArr;

    @Nullable
    public UUID[] getGroupIdArr() {
        return groupIdArr;
    }

    public void setGroupIdArr(@Nullable UUID[] groupIdArr) {
        this.groupIdArr = groupIdArr;
    }
}
