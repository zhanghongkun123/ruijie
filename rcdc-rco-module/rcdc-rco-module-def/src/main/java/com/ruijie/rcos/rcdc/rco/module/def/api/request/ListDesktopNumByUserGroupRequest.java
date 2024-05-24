package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ListDesktopNumByUserGroupRequest
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-08
 *
 * @author hli
 */
public class ListDesktopNumByUserGroupRequest implements Request {

    @Nullable
    private UUID[] uuidArr;

    public UUID[] getUuidArr() {
        return uuidArr;
    }

    public void setUuidArr(UUID[] uuidArr) {
        this.uuidArr = uuidArr;
    }
}
