package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageDriverType;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;


/**
 * Description: 驱动上传个数限制枚举
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/04
 *
 * @author ypp
 */
public enum ServerTypeDriverUploadLimit {

    MINI_SERVER_IDV_DRIVER_LIMIT(ServerModelEnum.MINI_SERVER_MODEL, ImageDriverType.SELF_DEVELOPED_IDV_DRIVER,
            "rcdc.image.driver.idv.upload.limit.mini"),

    VDI_SERVER_IDV_DRIVER_LIMIT(ServerModelEnum.VDI_SERVER_MODEL, ImageDriverType.SELF_DEVELOPED_IDV_DRIVER,
            "rcdc.image.driver.idv.upload.limit.vdi"),

    IDV_SERVER_IDV_DRIVER_LIMIT(ServerModelEnum.IDV_SERVER_MODEL, ImageDriverType.SELF_DEVELOPED_IDV_DRIVER,
            "rcdc.image.driver.idv.upload.limit.idv"),

    MINI_SERVER_TCI_DRIVER_LIMIT(ServerModelEnum.MINI_SERVER_MODEL, ImageDriverType.SELF_DEVELOPED_TCI_DRIVER,
            "rcdc.image.driver.tci.upload.limit.mini"),

    VDI_SERVER_TCI_DRIVER_LIMIT(ServerModelEnum.VDI_SERVER_MODEL, ImageDriverType.SELF_DEVELOPED_TCI_DRIVER,
            "rcdc.image.driver.tci.upload.limit.vdi"),

    IDV_SERVER_TCI_DRIVER_LIMIT(ServerModelEnum.IDV_SERVER_MODEL, ImageDriverType.SELF_DEVELOPED_TCI_DRIVER,
            "rcdc.image.driver.tci.upload.limit.idv"),

    MINI_SERVER_THIRD_PARTY_LIMIT(ServerModelEnum.MINI_SERVER_MODEL, ImageDriverType.THIRD_PARTY_DRIVER,
            "rcdc.image.driver.third.party.upload.limit.mini"),

    VDI_SERVER_THIRD_PARTY_LIMIT(ServerModelEnum.VDI_SERVER_MODEL, ImageDriverType.THIRD_PARTY_DRIVER,
            "rcdc.image.driver.third.party.upload.limit.vdi"),

    IDV_SERVER_THIRD_PARTY_LIMIT(ServerModelEnum.IDV_SERVER_MODEL, ImageDriverType.THIRD_PARTY_DRIVER,
            "rcdc.image.driver.third.party.upload.limit.idv"),


    MINI_SERVER_USER_DEFINED_LIMIT(ServerModelEnum.MINI_SERVER_MODEL, ImageDriverType.USER_DEFINED_DRIVER,
            "rcdc.image.driver.user.defined.upload.limit.mini"),

    VDI_IDV_SERVER_USER_DEFINED_LIMIT(ServerModelEnum.VDI_SERVER_MODEL, ImageDriverType.USER_DEFINED_DRIVER,
            "rcdc.image.driver.user.defined.upload.limit.vdi"),

    IDV_IDV_SERVER_USER_DEFINED_LIMIT(ServerModelEnum.IDV_SERVER_MODEL, ImageDriverType.USER_DEFINED_DRIVER,
            "rcdc.image.driver.user.defined.upload.limit.idv"),

    MINI_USER_DRIVER_PROGRAM_LIMIT(ServerModelEnum.MINI_SERVER_MODEL, ImageDriverType.USER_DRIVER_PROGRAM,
            "rcdc.image.driver.user.driver.program.upload.limit.mini"),

    VDI_USER_DRIVER_PROGRAM_LIMIT(ServerModelEnum.VDI_SERVER_MODEL, ImageDriverType.USER_DRIVER_PROGRAM,
            "rcdc.image.driver.user.driver.program.upload.limit.vdi"),
    IDV_USER_DRIVER_PROGRAM_LIMIT(ServerModelEnum.IDV_SERVER_MODEL, ImageDriverType.USER_DRIVER_PROGRAM,
            "rcdc.image.driver.user.driver.program.upload.limit.idv");

    ServerTypeDriverUploadLimit(ServerModelEnum serverModel, ImageDriverType driverType, String limitKey) {

        this.serverModel = serverModel;

        this.driverType = driverType;

        this.limitKey = limitKey;
    }


    private ServerModelEnum serverModel;

    private ImageDriverType driverType;

    private String limitKey;

    public ServerModelEnum getServerModel() {
        return serverModel;
    }

    public void setServerModel(ServerModelEnum serverModel) {
        this.serverModel = serverModel;
    }

    public ImageDriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(ImageDriverType driverType) {
        this.driverType = driverType;
    }

    public String getLimitKey() {
        return limitKey;
    }

    public void setLimitKey(String limitKey) {
        this.limitKey = limitKey;
    }

    /**
     * 根据服务器类型、驱动类型查询个数限制key
     * 
     * @param serverModel 服务器类型
     * @param driverType 驱动类型
     * @return String
     */
    public static String getLimitKeyByServerModelAndDriverType(ServerModelEnum serverModel, ImageDriverType driverType) {
        Assert.notNull(serverModel, "serverModel must not be null");
        Assert.notNull(driverType, "driverType must not be null");

        return Objects.requireNonNull(Arrays.stream(ServerTypeDriverUploadLimit.values())
                .filter(item -> item.getServerModel() == serverModel && item.getDriverType() == driverType).findFirst()
                .orElse(MINI_SERVER_IDV_DRIVER_LIMIT)).getLimitKey();

    }
}
