package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.enums.ImageListType;

/**
 * 
 * Description: 进入业务维护模式
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/4
 *
 * @author zhiweiHong
 */
public class BusinessMaintenanceDetailWebResponse {

    private Long retrySoftCloseCount;

    private String tipContent;

    private Long afterTipShutdownTime;

    private Boolean enablePublishRecoveryImage;

    private ImageListType imageListType;

    private GroupIdLabelEntry[] recoveryImageArr;

    private Boolean enableForceShutdown;

    public Long getRetrySoftCloseCount() {
        return retrySoftCloseCount;
    }

    public void setRetrySoftCloseCount(Long retrySoftCloseCount) {
        this.retrySoftCloseCount = retrySoftCloseCount;
    }

    public String getTipContent() {
        return tipContent;
    }

    public void setTipContent(String tipContent) {
        this.tipContent = tipContent;
    }

    public Long getAfterTipShutdownTime() {
        return afterTipShutdownTime;
    }

    public void setAfterTipShutdownTime(Long afterTipShutdownTime) {
        this.afterTipShutdownTime = afterTipShutdownTime;
    }

    public Boolean getEnablePublishRecoveryImage() {
        return enablePublishRecoveryImage;
    }

    public void setEnablePublishRecoveryImage(Boolean enablePublishRecoveryImage) {
        this.enablePublishRecoveryImage = enablePublishRecoveryImage;
    }

    public ImageListType getImageListType() {
        return imageListType;
    }

    public void setImageListType(ImageListType imageListType) {
        this.imageListType = imageListType;
    }

    public GroupIdLabelEntry[] getRecoveryImageArr() {
        return recoveryImageArr;
    }

    public void setRecoveryImageArr(GroupIdLabelEntry[] recoveryImageArr) {
        this.recoveryImageArr = recoveryImageArr;
    }

    public Boolean getEnableForceShutdown() {
        return enableForceShutdown;
    }

    public void setEnableForceShutdown(Boolean enableForceShutdown) {
        this.enableForceShutdown = enableForceShutdown;
    }
}
