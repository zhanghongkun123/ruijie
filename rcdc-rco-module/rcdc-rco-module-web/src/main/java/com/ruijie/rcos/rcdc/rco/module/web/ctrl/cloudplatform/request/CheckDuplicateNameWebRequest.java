package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 重名检查
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/8
 *
 * @author WuShengQiang
 */
public class CheckDuplicateNameWebRequest implements WebRequest {

    @Nullable
    private UUID id;

    @NotBlank
    private String name;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
