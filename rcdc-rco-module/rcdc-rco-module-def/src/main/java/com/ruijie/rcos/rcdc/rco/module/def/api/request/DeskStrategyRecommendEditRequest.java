package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

import java.util.UUID;

/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/3  <br>
 *
 * @author yyz
 */
public class DeskStrategyRecommendEditRequest implements Request {

    @NotEmpty
    private UUID[] uuidArr;

    public UUID[] getUuidArr() {
        return uuidArr;
    }

    public void setUuidArr(UUID[] uuidArr) {
        this.uuidArr = uuidArr;
    }
}
