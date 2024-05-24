package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import java.util.UUID;
import org.springframework.lang.Nullable;

/**
 * Description: 检查名称唯一提交的数据
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24 15:16
 *
 * @author BaiGuoliang
 */
public class CheckCabinetNameDuplicationWebRequest implements WebRequest {

    @Nullable
    private UUID id;

    @NotBlank
    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
