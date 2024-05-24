package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021.10.09 <br>
 *
 * @author liangyifeng
 */
public class DeleteSnapshotWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像标识", required = true)
    @NotNull
    private UUID imageTemplateId;

    @ApiModelProperty(value = "快照标识", required = true)
    @NotEmpty
    private UUID[] snapshotIdArr;


    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID[] getSnapshotIdArr() {
        return snapshotIdArr;
    }

    public void setSnapshotIdArr(UUID[] snapshotIdArr) {
        this.snapshotIdArr = snapshotIdArr;
    }
}
