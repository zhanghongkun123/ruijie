package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DeskSessionShutDownCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description: 启动桌面事件处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/8
 *
 * @author Jarman
 */
@Service
public class StartDeskHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartDeskHandler.class);

    @Autowired
    private StartDesktopSPIHelper startDesktopSPIHelper;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private DeskSessionShutDownCache deskSessionShutDownCache;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        switch (request.getOperateType()) {
            case START_DESK:
            case WAKE_UP_DESK:
            case REBOOT_DESK:
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) {
        LOGGER.info("桌面操作：{{}}，桌面id:{}", request.getOperateType().name(), request.getDeskId());
        UUID deskId = request.getDeskId();
        CountDownLatch deskLatch = deskSessionShutDownCache.getCache(deskId);
        if (null != deskLatch) {
            LOGGER.info("桌面[{}]执行唤醒完成，更新deskSessionShutDownCache", deskId);
            deskLatch.countDown();
        }
        CbbDeskDTO cbbDeskDTO = null;
        //移除-关机事件已处理还原或者变更镜像操作缓存
        cbbDeskMgmtAPI.removeEditImageCache(deskId);
        try {
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        } catch (BusinessException e) {
            LOGGER.error("查询桌面状态信息失败，deskId=" + request.getDeskId(), e);
        }
        if (cbbDeskDTO != null && request.getIsSuccess()
                && (CbbCloudDeskOperateType.START_DESK == request.getOperateType()
                || CbbCloudDeskOperateType.WAKE_UP_DESK == request.getOperateType())) {
            // 动态挂载磁盘
            userDiskMgmtAPI.attachDesktopDisk(request.getDeskId());
            uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.RUNNING);
            rcaHostAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.RUNNING);
        }
        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(deskId);
        if (desktopRequestDTO == null) {
            throw new IllegalStateException("找不到请求终端数据,deskId=" + request.getDeskId());
        }
        if (isDesktopStartFromWeb(desktopRequestDTO)) {
            desktopOperateRequestCache.removeCache(deskId);
            return;
        }

        try {
            boolean isSuccess = startDesktopSPIHelper.responseDeskConnectionInfoToShine(request);

            if (isSuccess) {
                LOGGER.info("启动桌面[{}]成功，准备执行绑定终端操作", deskId);
                // 响应连接信息成功，绑定终端
                bindTerminal(request);
                // 更新缓存信息，桌面运行在终端上
                desktopRequestDTO.setDesktopRunInTerminal(true);
                desktopOperateRequestCache.addCache(deskId, desktopRequestDTO);
                // 完成桌面与终端的绑定后，删除用户使用信息内桌面绑定终端的缓存
                userLoginRecordService.deleteTerminalCacheByDeskId(deskId.toString());
            }
        } catch (Exception e) {
            LOGGER.error("相应消息给shine失败，deskId=" + request.getDeskId(), e);
        }
    }

    private void bindTerminal(CbbDeskOperateNotifyRequest request) {
        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(request.getDeskId());
        if (desktopRequestDTO == null) {
            throw new IllegalStateException("找不到请求终端数据,deskId=" + request.getDeskId());
        }
        CbbDispatcherRequest dispatcherRequest = desktopRequestDTO.getCbbDispatcherRequest();
        String terminalId = dispatcherRequest.getTerminalId();
        cloudDesktopOperateService.bindDesktopTerminal(request.getDeskId(), terminalId);
    }

    private boolean isDesktopStartFromWeb(DesktopRequestDTO desktopRequestDTO) {
        if (desktopRequestDTO == null) {
            return false;
        }

        return desktopRequestDTO.isDesktopStartFromWeb();
    }

}
