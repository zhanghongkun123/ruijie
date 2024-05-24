package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestApplicationAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftPackageDiskInfo;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppResourceTypeEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.message.MessageUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.appcenter.dto.AppSoftPackageDiskListDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.appcenter.dto.DeskIdDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月15日
 *
 * @author xgx
 */
@DispatcherImplemetion(Constants.GET_APP_DISK_LIST)
public class GetAppDiskListSPIImpl implements CbbDispatcherHandlerSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAppDiskListSPIImpl.class);

    private static final ExecutorService GET_APP_DISK_LIST_THREAD_POOL = ThreadExecutors.newBuilder("get_app_disk_list_thread_pool") //
            .maxThreadNum(50) //
            .queueSize(1000) //
            .build();

    @Autowired
    private CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    @Autowired
    private CbbUamAppTestApplicationAPI cbbUamAppTestApplicationAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI appSoftwarePackageMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("shine请求获取应用软件包磁盘列表，参数为[{}]", JSON.toJSON(request));

        GET_APP_DISK_LIST_THREAD_POOL.execute(() -> {
            String data = request.getData();
            Assert.hasText(data, "request.data can not be empty");

            DeskIdDTO deskIdDTO = JSON.parseObject(data, DeskIdDTO.class);
            UUID deskId = deskIdDTO.getDeskId();
            Assert.notNull(deskId, "deskId can not be null");

            try {
                List<AppSoftPackageDiskInfo> idvAppDiskList = appSoftwarePackageMgmtAPI.getIDVAppDiskList(AppResourceTypeEnum.CLOUD_DESKTOP, deskId);
                AppSoftPackageDiskInfo[] appSoftPackageDiskInfoArr = idvAppDiskList.toArray(new AppSoftPackageDiskInfo[0]);
                messageHandlerAPI.response(MessageUtils.buildResponseMessage(request, new AppSoftPackageDiskListDTO(appSoftPackageDiskInfoArr)));
            } catch (Throwable e) {
                LOGGER.error("获取桌面[" + deskId + "]应用软件包磁盘列表失败", e);
                messageHandlerAPI.response(MessageUtils.buildErrorResponseMessage(request));
            }
        });
    }

}
