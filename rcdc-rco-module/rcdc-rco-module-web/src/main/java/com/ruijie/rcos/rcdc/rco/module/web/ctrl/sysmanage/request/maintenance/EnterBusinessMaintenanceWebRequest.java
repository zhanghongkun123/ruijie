package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.maintenance;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.enums.ImageListType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

/**
 * 
 * Description: 进入业务维护模式
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/4
 *
 * @author zhiweiHong
 */
public class EnterBusinessMaintenanceWebRequest implements WebRequest {


    @Nullable
    private Long retrySoftCloseCount = 3L;

    @Nullable
    private String tipContent;

    @Nullable
    private Long afterTipShutdownTime = 60L;

    @Nullable
    private Boolean enablePublishRecoveryImage;

    @Nullable
    private ImageListType imageListType;

    @Nullable
    private UUID[] recoveryImageArr;

    @Nullable
    private Boolean enableForceShutdown = Boolean.FALSE;

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

    public UUID[] getRecoveryImageArr() {
        return recoveryImageArr;
    }

    public void setRecoveryImageArr(UUID[] recoveryImageArr) {
        this.recoveryImageArr = recoveryImageArr;
    }

    @Nullable
    public Boolean getEnableForceShutdown() {
        return enableForceShutdown;
    }

    public void setEnableForceShutdown(@Nullable Boolean enableForceShutdown) {
        this.enableForceShutdown = enableForceShutdown;
    }
}
