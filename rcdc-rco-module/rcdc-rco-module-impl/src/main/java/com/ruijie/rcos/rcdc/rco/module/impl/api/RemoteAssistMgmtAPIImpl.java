package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.CreateDeskRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EstClientService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.ComputerRemoteAssistMgmtServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.service.struct.RemoteAssistInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
public class RemoteAssistMgmtAPIImpl implements RemoteAssistMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAssistMgmtAPIImpl.class);

    @Autowired
    private RemoteAssistService remoteAssistMgmtService;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private EstClientService estClientService;

    @Override
    public void applyRemoteAssist(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CreateDeskRemoteAssistDTO dto = new CreateDeskRemoteAssistDTO();
        BeanUtils.copyProperties(request, dto);

        checkVncNum(request.getDeskId());
        remoteAssistMgmtService.applyAssist(dto);
    }

    @Override
    public void cancelRemoteAssist(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.adminCancelAssist(request.getDeskId(), request.getAdminId());
    }

    @Override
    public CloudDesktopRemoteAssistDTO queryRemoteAssistInfo(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        RemoteAssistInfo info = remoteAssistMgmtService.queryAssisInfo(request.getDeskId(), request.getAdminId());
        return RemoteAssistInfo.convertToResponse(info);
    }


    @Override
    public void createVncChannelResult(RemoteAssistRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        remoteAssistMgmtService.createVncChannelResult(request.getDeskId(), request.getAdminId());
    }

    @Override
    public void deskShutdownHandle(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        remoteAssistMgmtService.deskShutdownHandle(deskId);
    }

    /**
     * 判断 EstClient 数量限制
     *
     * @param deskId 桌面标识
     * @throws BusinessException 限制远程协助
     */
    private void checkVncNum(UUID deskId) throws BusinessException {
        RemoteAssistInfo remoteAssistInfo = remoteAssistMgmtService.queryRemoteAssistInfo(deskId);
        if (remoteAssistInfo == null) {
            long vncCount = remoteAssistMgmtService.remoteAssistNum() + cbbImageTemplateMgmtAPI.getVncEditingImageNum();
            int vncLimit = estClientService.estClientLimit();
            LOGGER.info("deskId：{} vncCount：{} vncLimit：{}", deskId, vncCount, vncLimit);
            if (vncCount + 1 > vncLimit) {
                throw new BusinessException(BusinessKey.RCDC_USER_REMOTE_ASSIST_VM_LIMIT);
            }
        }
    }



}
