package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalOnlineTimeRecordService;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.BusinessAction;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageDownloadStateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalEventNoticeSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbNoticeRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:
 *
 * @author artom
 */
@DispatcherImplemetion(BusinessAction.OFFLINE)
public class TerminalOfflineSPIImpl implements CbbTerminalEventNoticeSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOfflineSPIImpl.class);

    private static final int IMAGE_DOWNLOADING_TERMINAL_OFFLINE_CODE = 104;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;

    @Autowired
    private TerminalOnlineTimeRecordService onlineTimeRecordService;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public void notify(CbbNoticeRequest request) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getTerminalBasicInfo(), "terminalBaseInfo can not be null");
        String terminalId = request.getTerminalBasicInfo().getTerminalId();
        Assert.hasText(terminalId, "terminal id can not be null");

        // 终端离线，用户退出登录，移除登录session
        LOGGER.info("终端[{}]离线", terminalId);
        userLoginSession.removeLoginUser(terminalId);
        UserTerminalEntity entity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (entity == null) {
            LOGGER.error("terminal not exist,terminal id : {}", terminalId);
            return;
        }

        // 终端接入时机跟NettyServerInit初始化有顺序先后问题，故如果终端是在线状态则不去修改IDV云桌面后续操作
        try {
            ViewTerminalEntity viewTerminalEntity = terminalService.getViewByTerminalId(terminalId);
            if (CbbTerminalStateEnums.ONLINE == viewTerminalEntity.getTerminalState()) {
                LOGGER.info("终端[{}]的状态为在线[ONLINE]状态, 不去修改对应的IDV云桌面后续操作", terminalId);
                return;
            }
        } catch (BusinessException e) {
            LOGGER.error("[{}]终端[{}]离线, 修改对应的IDV云桌面[id:{}]状态为[OFF_LINE]出现异常:{}",
                    request.getTerminalBasicInfo().getPlatform(), request.getTerminalBasicInfo().getTerminalId(), entity.getBindDeskId(), e);
        }

        // IDV终端离线,修改对应IDV云桌面未离线状态
        handleIDVDesktopState(request, entity);
        // 解除云桌面与终端的绑定
        cloudDesktopOperateService.unbindDesktopTerminal(entity);
        // 清除对应的桌面操作缓存
        desktopOperateRequestCache.removeByTerminalId(terminalId);

        // 处理云桌面下载状态
        handleImageDownloadState(terminalId);

        // 处理终端在线总时长
        handleTerminalOfflineTime(request.getTerminalBasicInfo());

        // 把终端所有连接中的记录改为未连接
        userLoginRecordService.compensateTerminalConnectingRecord(terminalId);
    }

    /**
     * 终端离线时，若镜像下载状态为下载中，则需要把对应的状态修改为离线导致下载失败
     *
     * @param terminalId 终端ID
     */
    private void handleImageDownloadState(String terminalId) {
        ImageDownloadStateDTO dto = imageDownloadStateService.getByTerminalId(terminalId);

        // 如果镜像正在下载
        if (dto != null && dto.getDownloadState() == DownloadStateEnum.START) {
            LOGGER.info("终端[{}]离线时，镜像下载状态为[{}]，需要修改为下载失败", terminalId, dto.getDownloadState());
            ImageDownloadStateDTO updateRequest = new ImageDownloadStateDTO();
            updateRequest.setId(dto.getId());
            updateRequest.setImageId(dto.getImageId());
            updateRequest.setTerminalId(dto.getTerminalId());
            updateRequest.setDownloadState(DownloadStateEnum.FAIL);
            updateRequest.setFailCode(IMAGE_DOWNLOADING_TERMINAL_OFFLINE_CODE);
            imageDownloadStateService.update(updateRequest);
        }
    }

    /**
     * IDV终端离线修改对应的IDV云桌面为离线状态
     *
     * @param cbbNoticeRequest CbbNoticeRequest
     */
    private void handleIDVDesktopState(CbbNoticeRequest cbbNoticeRequest, UserTerminalEntity entity) {
        CbbShineTerminalBasicInfo terminalBasicInfo = cbbNoticeRequest.getTerminalBasicInfo();
        try {
            ViewTerminalEntity viewTerminalEntity = terminalService.getViewByTerminalId(terminalBasicInfo.getTerminalId());
            // IDV VOI终端离线 通知
            if (CbbTerminalPlatformEnums.IDV == viewTerminalEntity.getPlatform()
                    || CbbTerminalPlatformEnums.VOI == viewTerminalEntity.getPlatform()) {
                // 桌面ID为空 ，直接返回
                if (entity.getBindDeskId() == null) {
                    LOGGER.warn("[{}]终端[{}]离线, 云桌面信息不存在，不进行通知", viewTerminalEntity.getPlatform(), terminalBasicInfo.getTerminalId());
                    return;
                }
                LOGGER.info("[{}]终端[{}]离线, 修改对应的IDV云桌面[id:{}]状态为[OFF_LINE]", viewTerminalEntity.getPlatform(),
                        terminalBasicInfo.getTerminalId(),
                        entity.getBindDeskId());
                // 判断云桌面是否存在  云桌面操作不区分IDV  VPO
                cbbIDVDeskMgmtAPI.updateIDVDeskStateByDeskId(entity.getBindDeskId(), CbbCloudDeskState.OFF_LINE);

                // 通知UWS
                uwsDockingAPI.notifyDesktopStateUpdate(entity.getBindDeskId(), CbbCloudDeskState.OFF_LINE);
                uwsDockingAPI.notifyTerminalStateUpdate(entity.getBindDeskId(), entity.getTerminalId(), CbbTerminalStateEnums.OFFLINE);
            }
        } catch (Exception e) {
            LOGGER.error("[{}]终端[{}]离线, 修改对应的IDV云桌面[id:{}]状态为[OFF_LINE]出现异常:{}",
                    terminalBasicInfo.getPlatform(), terminalBasicInfo.getTerminalId(), entity.getBindDeskId(), e);
        }
    }

    /**
     * 终端离线更新在线时长
     *
     * @param terminalBasicInfo 终端信息
     */
    private void handleTerminalOfflineTime(CbbShineTerminalBasicInfo terminalBasicInfo) {
        String terminalId = null;
        try {
            terminalId = terminalBasicInfo.getTerminalId();
            onlineTimeRecordService.handleTerminalOfflineTime(terminalId, terminalBasicInfo.getHasServerOnline());
        } catch (Exception e) {
            LOGGER.error("终端[{}]离线,更新终端的在线时长错误", terminalId, e);
        }
    }
}


