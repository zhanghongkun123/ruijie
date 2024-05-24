package com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm;

import java.util.UUID;

/**
 * Description: 同步策略-镜像同步任务进行中的镜像集合
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/11
 *
 * @author WuShengQiang
 */
public class UnifiedManageSyncingImageDTO {

    /**
     * 镜像ID
     */
    private UUID imageTemplateId;

    /**
     * 镜像统一管理ID
     */
    private UUID imageUnifiedDataId;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getImageUnifiedDataId() {
        return imageUnifiedDataId;
    }

    public void setImageUnifiedDataId(UUID imageUnifiedDataId) {
        this.imageUnifiedDataId = imageUnifiedDataId;
    }
}
