package com.ruijie.rcos.rcdc.rco.module.def.api.dto.image;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: 从端回滚镜像同步接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
public class SlaveRollbackImageSyncDTO implements Serializable {

    private UUID imageId;

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
