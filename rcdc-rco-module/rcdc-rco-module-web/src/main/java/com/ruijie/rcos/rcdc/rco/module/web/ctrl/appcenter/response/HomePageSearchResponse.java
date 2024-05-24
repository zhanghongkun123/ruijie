package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamAppDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamDeliveryGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamPushInstallPackageDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/18 14:50
 *
 * @author coderLee23
 */
public class HomePageSearchResponse {

    private DefaultPageResponse<UamAppDiskDTO> appDiskPage;

    private DefaultPageResponse<UamPushInstallPackageDTO> pushInstallPackagePage;

    /**
     *  推送安装包交付组
     */
    private DefaultPageResponse<UamDeliveryGroupDTO> pushDeliveryGroupPage;

    /**
     *  应用磁盘交付组
     */
    private DefaultPageResponse<UamDeliveryGroupDTO> diskDeliveryGroupPage;


    public DefaultPageResponse<UamAppDiskDTO> getAppDiskPage() {
        return appDiskPage;
    }

    public void setAppDiskPage(DefaultPageResponse<UamAppDiskDTO> appDiskPage) {
        this.appDiskPage = appDiskPage;
    }

    public DefaultPageResponse<UamPushInstallPackageDTO> getPushInstallPackagePage() {
        return pushInstallPackagePage;
    }

    public void setPushInstallPackagePage(DefaultPageResponse<UamPushInstallPackageDTO> pushInstallPackagePage) {
        this.pushInstallPackagePage = pushInstallPackagePage;
    }

    public DefaultPageResponse<UamDeliveryGroupDTO> getPushDeliveryGroupPage() {
        return pushDeliveryGroupPage;
    }

    public void setPushDeliveryGroupPage(DefaultPageResponse<UamDeliveryGroupDTO> pushDeliveryGroupPage) {
        this.pushDeliveryGroupPage = pushDeliveryGroupPage;
    }

    public DefaultPageResponse<UamDeliveryGroupDTO> getDiskDeliveryGroupPage() {
        return diskDeliveryGroupPage;
    }

    public void setDiskDeliveryGroupPage(DefaultPageResponse<UamDeliveryGroupDTO> diskDeliveryGroupPage) {
        this.diskDeliveryGroupPage = diskDeliveryGroupPage;
    }
}
