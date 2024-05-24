package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/25 20:35
 *
 * @author linrenjian
 */
public class PublishImageDTO extends AdminInfoDTO {

    // 镜像模板标识
    private UUID imageTemplateId;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }
}
