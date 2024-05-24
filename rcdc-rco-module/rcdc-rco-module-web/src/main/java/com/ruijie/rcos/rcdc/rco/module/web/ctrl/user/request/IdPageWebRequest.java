package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * 只根据关联对象ID查询请求接口
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月20日
 * 
 * @author 
 */
public class IdPageWebRequest extends PageWebRequest {
    
    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
