package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 镜像计算机名称重名检查请求对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/24
 *
 * @author WuShengQiang
 */
public class CheckComputerNameDuplicationWebRequest implements WebRequest {

    @NotBlank
    private String computerName;

    @Nullable
    private UUID id;

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }
}
