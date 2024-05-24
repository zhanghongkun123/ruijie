package com.ruijie.rcos.rcdc.rco.module.def.api.dto.image;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: 从端准备镜像同步接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
public class MasterCrossClusterImageSyncDTO implements Serializable {


    @NotNull
    private UUID unifiedManageDataId;

    @NotNull
    private UUID imageId;


    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
