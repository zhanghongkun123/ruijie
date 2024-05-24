package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;


/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/6 16:18
 *
 * @author conghaifeng
 */
public class RevertDesktopWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面ID数组", required = true)
    @NotNull
    @Size(min = 1)
    private UUID[] idArr;

    @ApiModelProperty(value = "保留原镜像数据")
    @NotNull
    private Boolean enableStoreSystemDisk = false;


    @ApiModelProperty(value = "是否同时还原D盘，并删除数据")
    @Nullable
    private Boolean needRestoreDataTemplateDisk = false;

    public Boolean getEnableStoreSystemDisk() {
        return enableStoreSystemDisk;
    }

    public void setEnableStoreSystemDisk(Boolean enableStoreSystemDisk) {
        this.enableStoreSystemDisk = enableStoreSystemDisk;
    }

    @Nullable
    public Boolean getNeedRestoreDataTemplateDisk() {
        return needRestoreDataTemplateDisk;
    }

    public void setNeedRestoreDataTemplateDisk(@Nullable Boolean needRestoreDataTemplateDisk) {
        this.needRestoreDataTemplateDisk = needRestoreDataTemplateDisk;
    }

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
