package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021.10.09 <br>
 *
 * @author liangyifeng
 */
public class RenameSnapshotWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像标识", required = true)
    @NotNull
    private UUID imageTemplateId;

    @ApiModelProperty(value = "快照标识", required = true)
    @NotNull
    private UUID snapshotId;

    @ApiModelProperty(value = "快照名称", required = true)
    @NotNull
    @Size(max = 64)
    @TextName
    private String name;

    @ApiModelProperty(value = "备注")
    @Nullable
    @Size(max = 64)
    private String remark;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(UUID snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getRemark() {
        return remark;
    }

    public void setRemark(@Nullable String remark) {
        this.remark = remark;
    }
}
