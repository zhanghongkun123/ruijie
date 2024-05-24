package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.enums.DesktopExportSourceEnum;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 导出桌面WebRequest
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/14
 *
 * @author linke
 */
public class DesktopExportWebRequest implements WebRequest {

    @ApiModelProperty(value = "导出的来源")
    @Nullable
    private DesktopExportSourceEnum exportSource;


    @ApiModelProperty(value = "桌面池ID数组")
    @Nullable
    @Size(min = 1)
    private UUID[] desktopPoolIdArr;

    @ApiModelProperty(value = "镜像类型")
    @Nullable
    private ImageUsageTypeEnum imageUsage;

    @ApiModelProperty(value = "应用池ID数组")
    @Nullable
    private UUID[] appPoolIdArr;

    @Nullable
    public DesktopExportSourceEnum getExportSource() {
        return exportSource;
    }

    public void setExportSource(@Nullable DesktopExportSourceEnum exportSource) {
        this.exportSource = exportSource;
    }

    @Nullable
    public UUID[] getDesktopPoolIdArr() {
        return desktopPoolIdArr;
    }

    public void setDesktopPoolIdArr(@Nullable UUID[] desktopPoolIdArr) {
        this.desktopPoolIdArr = desktopPoolIdArr;
    }

    @Nullable
    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(@Nullable ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }

    @Nullable
    public UUID[] getAppPoolIdArr() {
        return appPoolIdArr;
    }

    public void setAppPoolIdArr(@Nullable UUID[] appPoolIdArr) {
        this.appPoolIdArr = appPoolIdArr;
    }
}
