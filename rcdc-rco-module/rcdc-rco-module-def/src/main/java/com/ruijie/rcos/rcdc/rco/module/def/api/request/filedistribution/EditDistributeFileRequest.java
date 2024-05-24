package com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/15 17:01
 *
 * @author zhangyichi
 */
public class EditDistributeFileRequest extends DefaultRequest {

    @NotNull
    private UUID id;

    @TextMedium
    private String description;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
