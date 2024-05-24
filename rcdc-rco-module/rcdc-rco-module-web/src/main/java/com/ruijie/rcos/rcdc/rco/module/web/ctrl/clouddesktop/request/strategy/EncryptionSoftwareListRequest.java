package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.EncryptionSoftwareType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 获取透明解密受控软件列表请求体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/6
 *
 * @author WuShengQiang
 */
public class EncryptionSoftwareListRequest {

    /**
     * 软件类型
     */
    @ApiModelProperty(value = "软件类型")
    @Nullable
    private EncryptionSoftwareType type;

    @Nullable
    public EncryptionSoftwareType getType() {
        return type;
    }

    public void setType(@Nullable EncryptionSoftwareType type) {
        this.type = type;
    }
}
