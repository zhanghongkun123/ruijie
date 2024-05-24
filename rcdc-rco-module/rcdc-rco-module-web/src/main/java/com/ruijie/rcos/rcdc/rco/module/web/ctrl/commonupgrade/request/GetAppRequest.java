package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.request;

import com.ruijie.rcos.base.upgrade.module.def.enums.ArchType;
import com.ruijie.rcos.base.upgrade.module.def.enums.DownloadType;
import com.ruijie.rcos.base.upgrade.module.def.enums.OsType;
import com.ruijie.rcos.base.upgrade.module.def.enums.PacketProductType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import org.springframework.lang.Nullable;


/**
 * Description: CheckUploadRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21
 *
 * @author chenl
 */
public class GetAppRequest extends DefaultWebRequest {

    @NotNull
    private PacketProductType productType;

    @NotNull
    private OsType osType;

    @NotNull
    private ArchType archType;

    /**
     * 安装包类型，支持online、offline、qrcode、iso
     */
    @NotBlank
    private String type;

    /**
     * 安装包下载方式，支持http、bt，默认值http
     */
    @NotNull
    private DownloadType downloadType = DownloadType.HTTP;


    @Nullable
    private String version;

    @Nullable
    private String id;

    public PacketProductType getProductType() {
        return productType;
    }

    public void setProductType(PacketProductType productType) {
        this.productType = productType;
    }

    public OsType getOsType() {
        return osType;
    }

    public void setOsType(OsType osType) {
        this.osType = osType;
    }

    public ArchType getArchType() {
        return archType;
    }

    public void setArchType(ArchType archType) {
        this.archType = archType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DownloadType getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(DownloadType downloadType) {
        this.downloadType = downloadType;
    }

    @Nullable
    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }
}
