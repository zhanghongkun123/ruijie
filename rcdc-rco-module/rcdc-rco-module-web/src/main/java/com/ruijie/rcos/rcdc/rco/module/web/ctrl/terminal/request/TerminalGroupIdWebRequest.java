package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 分组请求只含有id的请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月22日
 * 
 * @author nt
 */
public class TerminalGroupIdWebRequest implements WebRequest {
    
    /**
     * 分组id
     */
    @NotNull
    private UUID id;
    

    public TerminalGroupIdWebRequest() {
    }

    public TerminalGroupIdWebRequest(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    
}
