package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request;

import com.ruijie.rcos.sk.base.annotation.Size;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月22日
 *
 * @author zdc
 */
public class BatchTaskRequest {

    /**
     * 任务id
     */
    @Nullable
    @Size(max = 64)
    private String id;

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }
}
