package com.ruijie.rcos.rcdc.rco.module.web.impl.spi;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbUpdateDiskBaseInfoNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDiskUpdateBaseInfoNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.ThirdUserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.DiskBaseInfoMgmtWebService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants.UPDATE_DISK_BASE_INFO_KEY;

/**
 * Description: 更新磁盘基础信息SPI
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/21
 *
 * @author lanzf
 */
@DispatcherImplemetion(UPDATE_DISK_BASE_INFO_KEY)
public class CbbUpdateDiskBaseInfoNotifySPIImpl implements CbbUpdateDiskBaseInfoNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbUpdateDiskBaseInfoNotifySPIImpl.class);

    @Autowired
    private DiskBaseInfoMgmtWebService webService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Override
    public void updateDiskBindUserDesc(String dispatchKey, CbbDiskUpdateBaseInfoNotifyRequest request) {
        Assert.notNull(dispatchKey, "dispatchKey can not be null");
        Assert.notNull(request, "request can not be null");

        if (Objects.nonNull(request.getDesktopId())) {
            webService.remoteUpdateDesktopDiskBindUserInfo(request.getPlatformId(), request.getDesktopId(), request.getDiskId());
        } else {
            webService.remoteUpdateDiskBindUserInfo(request.getPlatformId(), request.getDiskId(), request.getDiskBindUserDesc());
        }
    }

    @Override
    public void updateDiskDesc(String dispatchKey, CbbDiskUpdateBaseInfoNotifyRequest request) {
        Assert.notNull(dispatchKey, "dispatchKey can not be null");
        Assert.notNull(request, "request can not be null");

        webService.remoteUpdateDiskDescInfo(request.getPlatformId(), request.getDiskId(), request.getDiskDesc());
    }

    @Override
    public void updateAppDiskUseDesc(String dispatchKey, CbbDiskUpdateBaseInfoNotifyRequest request) {
        Assert.notNull(dispatchKey, "dispatchKey can not be null");
        Assert.notNull(request, "request can not be null");

        webService.remoteUpdateDiskUseDescInfo(request.getPlatformId(), request.getDiskId(), request.getDiskUseDesc());
    }

    @Override
    public void updateDesktopDiskFullDesc(String dispatchKey, CbbDiskUpdateBaseInfoNotifyRequest request) {
        Assert.notNull(dispatchKey, "dispatchKey can not be null");
        Assert.notNull(request, "request can not be null");

        if (Objects.isNull(request.getDesktopId())) {
            webService.remoteUpdateDiskFullDescInfo(request.getPlatformId(), request.getDiskId(), request.getDiskDesc(), request.getDiskUseDesc(), null);
        } else {
            final String userName = getUserName(request);
            webService.remoteUpdateDiskFullDescInfo(request.getPlatformId(), request.getDiskId(), request.getDiskDesc(), request.getDiskUseDesc(), userName);
        }

    }

    private String getUserName(CbbDiskUpdateBaseInfoNotifyRequest request) {
        try {
            final CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(request.getDesktopId());
            return Objects.equals(ThirdUserBusinessKey.DEFAULT_EMPTY_USERNAME, desktopDetailDTO.getUserName()) ? null : desktopDetailDTO.getUserName();
        } catch (Exception e) {
            LOGGER.error("获取桌面信息异常，桌面id：" + request.getDesktopId(), e);
            // NO PMD
            return null;
        }
    }
}
