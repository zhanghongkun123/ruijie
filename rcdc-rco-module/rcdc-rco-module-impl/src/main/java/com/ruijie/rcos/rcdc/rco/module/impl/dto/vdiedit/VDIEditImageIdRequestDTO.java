package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 02:21
 *
 * @author zhangyichi
 */
public class VDIEditImageIdRequestDTO extends VDIEditImageSessionRequestDTO {

    UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
