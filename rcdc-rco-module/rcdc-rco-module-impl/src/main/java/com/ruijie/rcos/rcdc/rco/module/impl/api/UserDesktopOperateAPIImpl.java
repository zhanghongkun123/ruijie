package com.ruijie.rcos.rcdc.rco.module.impl.api;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.google.common.base.Optional;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskOperateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.deskoperate.CbbRestoreDeskRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopRebootRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AutoStartVmCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.StartVmByStateHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/10
 *
 * @author Jarman
 */
public class UserDesktopOperateAPIImpl implements UserDesktopOperateAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDesktopOperateAPIImpl.class);

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private AutoStartVmCache autoStartVmCache;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskOperateMgmtAPI cbbThirdPartyDeskOperateAPI;

    @Autowired
    private StartVmByStateHandler startVmByStateHandler;

    private static final int IDV_SHUTDOWN_TIMEOUT_SECOND = 5;

    @Override
    public void shutdown(CloudDesktopShutdownRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID id = request.getId();
        Boolean isForce = request.getForce();
        Assert.notNull(id, "id must not be null");
        Assert.notNull(isForce, "isForce must not be null");

        UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(id);
        CbbShutdownDeskVDIDTO cbbReq = new CbbShutdownDeskVDIDTO();
        cbbReq.setId(userDesktopEntity.getCbbDesktopId());
        cbbReq.setIsForce(isForce);
        cbbReq.setAllowCancel(request.getAllowCancel());
        // OpenAPI改动
        if (request.getTaskId() != null) {
            cbbReq.setTaskId(request.getTaskId());
        }

        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(id);
        if (desktopRequestDTO == null) {
            desktopRequestDTO = new DesktopRequestDTO();
        }
        desktopRequestDTO.setDesktopInnerShutdown(false);
        // 记录操作，由管理后台页面主动发起
        desktopOperateRequestCache.addCache(id, desktopRequestDTO);
        try {
            if (userDesktopEntity.getDesktopType() == CbbCloudDeskType.THIRD) {
                cbbThirdPartyDeskOperateAPI.shutdownDeskThirdParty(cbbReq);
            } else {
                cbbVDIDeskOperateAPI.shutdownDeskVDI(cbbReq);
            }
        } catch (BusinessException e) {
            desktopRequestDTO.setDesktopInnerShutdown(true);
            desktopOperateRequestCache.addCache(id, desktopRequestDTO);
            throw e;
        }


    }

    @Override
    public void start(CloudDesktopStartRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        DesktopRequestDTO desktopRequestDTO = Optional.fromNullable(desktopOperateRequestCache.getCache(request.getId())).or(new DesktopRequestDTO());
        desktopRequestDTO.setDesktopStartFromWeb(true);
        desktopOperateRequestCache.addCache(request.getId(), desktopRequestDTO);
        try {
            resolveDeskStateAndVmState(request);
            cloudDesktopOperateService.startDesktop(request);
        } catch (BusinessException e) {
            desktopOperateRequestCache.removeCache(request.getId());
            throw e;
        }
    }

    private void resolveDeskStateAndVmState(CloudDesktopStartRequest request) throws BusinessException {
        CbbCloudDeskState state = queryCloudDesktopService.queryState(request.getId());
        StartVmDispatcherDTO startVmDispatcherDTO = new StartVmDispatcherDTO();
        startVmDispatcherDTO.setDesktopId(request.getId());
        startVmDispatcherDTO.setDeskState(state);
        startVmDispatcherDTO.setDeskBackupState(request.getDeskBackupState());
        startVmDispatcherDTO.setVmState(request.getVmState());
        startVmByStateHandler.preHandleMessageNoExecute(startVmDispatcherDTO);
        request.setVmState(startVmDispatcherDTO.getVmState());
        request.setDeskBackupState(startVmDispatcherDTO.getDeskBackupState());
    }

    @Override
    public void restore(CbbRestoreDeskRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        cbbVDIDeskOperateAPI.restoreDeskVDI(request);
    }

    @Override
    public void failback(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        cloudDesktopOperateService.recoverDeskFromError(request.getId());
    }

    @Override
    public void startIdv(CloudDesktopStartRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        try {
            cloudDesktopOperateService.startIdvDesktop(request);
        } catch (BusinessException e) {
            LOGGER.error("启动IDV桌面发生业务异常, 异常信息：", e);
            throw e;
        }
    }

    @Override
    public void shutdownIdv(CloudDesktopShutdownRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID id = request.getId();
        Boolean isForce = request.getForce();
        Assert.notNull(id, "id must not be null");
        Assert.notNull(isForce, "isForce must not be null");

        UserDesktopEntity userDesktopEntity = null;
        try {
            userDesktopEntity = queryCloudDesktopService.checkAndFindById(id);
        } catch (Exception ex) {
            LOGGER.info("云桌面不存在，桌面id : ", id, ex);
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, ex);
        }

        CbbShutdownDeskIDVDTO cbbReq = new CbbShutdownDeskIDVDTO();
        cbbReq.setId(userDesktopEntity.getCbbDesktopId());
        cbbReq.setIsForce(isForce);
        cbbReq.setTimeout(TimeUnit.MINUTES.toMillis(IDV_SHUTDOWN_TIMEOUT_SECOND));
        // 记录操作，由管理后台页面主动发起
        try {
            cbbIDVDeskOperateAPI.shutdownDeskIDV(cbbReq);
        } catch (BusinessException e) {
            throw e;
        }
    }

    @Override
    public void updateDeskAutoStartVmCache(String terminalId) {
        Assert.hasText(terminalId, "terminalId must not be null");

        LOGGER.info("新增自动进入虚机缓存，终端id: [{}]", terminalId);
        autoStartVmCache.addCache(terminalId);
    }

    @Override
    public boolean checkAutoStartVmFromCache(String terminalId) {
        Assert.hasText(terminalId, "terminalId must not be null");

        String cache = autoStartVmCache.getCache(terminalId);
        if (!StringUtils.isEmpty(cache)) {
            LOGGER.info("移除自动进入虚机缓存，终端id: [{}]", terminalId);
            autoStartVmCache.deleteCache(terminalId);
        }
        return !StringUtils.isEmpty(cache);
    }

    @Override
    public void deleteDeskAutoStartVmCache(String terminalId) {
        Assert.hasText(terminalId, "terminalId must not be null");

        LOGGER.info("移除自动进入虚机缓存，终端id: [{}]", terminalId);
        autoStartVmCache.deleteCache(terminalId);
    }

    @Override
    public void rebootDeskVDI(CloudDesktopRebootRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        cbbVDIDeskOperateAPI.rebootDeskVDI(request.getId());
    }

    @Override
    public void rebootDeskThird(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        cbbThirdPartyDeskOperateAPI.rebootDeskThird(deskId);
    }

    @Override
    public void changeDeskMaintenanceModel(List<UUID> desktopIds, Boolean isOpen) throws BusinessException {
        Assert.notNull(isOpen, "isOpen must not be null");
        Assert.notEmpty(desktopIds, "desktopPoolIdList cannot be null");
        this.cloudDesktopOperateService.changeDeskMaintenanceModel(desktopIds, isOpen);
    }

    @Override
    public void checkDesktopMaintenanceReady(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId can not be null");

        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
        if (Boolean.TRUE.equals(cloudDesktopDetailDTO.getIsOpenDeskMaintenance())) {
            LOGGER.error("云桌面[{}]处于维护模式", cloudDesktopDetailDTO.getDesktopName());
            throw new BusinessException(BusinessKey.RCDC_USER_SHINE_START_VM_UNDER_DESKTOP_MAINTENANCE_ERR);
        }
    }

    @Override
    public void sleepRcaHost(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        cbbVDIDeskOperateAPI.sleepAppDeskVDI(request.getId());
    }
}
