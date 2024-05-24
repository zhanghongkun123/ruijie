package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/23 11:13
 *
 * @author ketb
 */
public class QueryFaultRequest implements Request {

    @NotNull
    private CloudDesktopDTO[] desktopArr;

    public CloudDesktopDTO[] getDesktopArr() {
        return desktopArr;
    }

    public void setDesktopArr(CloudDesktopDTO[] desktopArr) {
        this.desktopArr = desktopArr;
    }
}
