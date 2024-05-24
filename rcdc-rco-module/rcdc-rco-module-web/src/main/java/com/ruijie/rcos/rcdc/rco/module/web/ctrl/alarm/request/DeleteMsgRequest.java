package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2018 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2018/12/26 <br>
 *
 * @author dell
 */
public class DeleteMsgRequest implements WebRequest {

    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
