package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImagePublishType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageSyncMode;
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
 * Create Time: 2021.07.05 <br>
 *
 * @author linhj
 */
public class CreateImageTemplatePublishTaskWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像模板标识", required = true)
    @NotNull
    private UUID imageId;

    @ApiModelProperty(value = "定时任务时间")
    @Nullable
    private String scheduleTime;

    @ApiModelProperty(value = "通知任务时间")
    @Nullable
    private String noticeTime;

    @ApiModelProperty(value = "通知信息")
    @Nullable
    private String tipMsg;

    @ApiModelProperty(value = "是否强制发布", required = true)
    @NotNull
    private Boolean enableForcePublish;

    @ApiModelProperty(value = "镜像发布类型")
    @NotNull
    private ImagePublishType publishType;

    @ApiModelProperty(value = "快照名称")
    @Nullable
    @Size(max = 64)
    @TextName
    private String snapshotName;

    @ApiModelProperty(value = "备注")
    @Nullable
    @Size(max = 64)
    private String remark;

    @ApiModelProperty(value = "镜像模板版本名称")
    @Nullable
    @TextName
    @Size(max = 64)
    private String versionImageName;

    @ApiModelProperty(value = "模版版本描述")
    @Nullable
    @Size(max = 64)
    private String desc;

    @ApiModelProperty(value = "镜像同步模式")
    @Nullable
    private ImageSyncMode syncType;

    @ApiModelProperty(value = "同步目标存储池ID集合")
    @Nullable
    private UUID[] storagePoolIdArr;

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    @Nullable
    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(@Nullable String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Nullable
    public String getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(@Nullable String noticeTime) {
        this.noticeTime = noticeTime;
    }

    @Nullable
    public String getTipMsg() {
        return tipMsg;
    }

    public void setTipMsg(@Nullable String tipMsg) {
        this.tipMsg = tipMsg;
    }

    public Boolean getEnableForcePublish() {
        return enableForcePublish;
    }

    public void setEnableForcePublish(Boolean enableForcePublish) {
        this.enableForcePublish = enableForcePublish;
    }

    public ImagePublishType getPublishType() {
        return publishType;
    }

    public void setPublishType(ImagePublishType publishType) {
        this.publishType = publishType;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    @Nullable
    public String getRemark() {
        return remark;
    }

    public void setRemark(@Nullable String remark) {
        this.remark = remark;
    }

    @Nullable
    public String getVersionImageName() {
        return versionImageName;
    }

    public void setVersionImageName(@Nullable String versionImageName) {
        this.versionImageName = versionImageName;
    }

    @Nullable
    public String getDesc() {
        return desc;
    }

    public void setDesc(@Nullable String desc) {
        this.desc = desc;
    }

    @Nullable
    public ImageSyncMode getSyncType() {
        return syncType;
    }

    public void setSyncType(@Nullable ImageSyncMode syncType) {
        this.syncType = syncType;
    }

    @Nullable
    public UUID[] getStoragePoolIdArr() {
        return storagePoolIdArr;
    }

    public void setStoragePoolIdArr(@Nullable UUID[] storagePoolIdArr) {
        this.storagePoolIdArr = storagePoolIdArr;
    }
}