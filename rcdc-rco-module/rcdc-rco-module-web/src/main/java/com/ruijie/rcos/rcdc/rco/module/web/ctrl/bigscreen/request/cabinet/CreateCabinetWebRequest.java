package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

/**
 * Description: 创建机柜提交的数据
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 15:16
 *
 * @author BaiGuoliang
 */
public class CreateCabinetWebRequest implements WebRequest {

    /**
     * 机柜名
     */
    @NotBlank
    @TextShort
    private String name;

    @Nullable
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
