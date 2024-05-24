package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.debuglog;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 删除调试日志web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月19日
 *
 * @author fyq
 */
public class BaseDeleteDebugLogWebRequest implements WebRequest {

    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
