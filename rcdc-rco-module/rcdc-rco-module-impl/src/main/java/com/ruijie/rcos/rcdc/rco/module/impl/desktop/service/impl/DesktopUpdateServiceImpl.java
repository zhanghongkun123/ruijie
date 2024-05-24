package com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbUpdateDeskStrategyIdVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoAppPoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ThreadExecutorsUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-01-25
 *
 * @author linke
 */
@Service
public class DesktopUpdateServiceImpl implements DesktopUpdateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopUpdateServiceImpl.class);

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcoAppPoolAPI rcoAppPoolAPI;

    @Override
    public void updateNotRecoverableVDIConfigAsync(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");

        try {
            if (checkIsRecoverableVDI(desktopId)) {
                return;
            }

            updateVDIConfigAsync(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("云桌面[{}]执行非还原桌面策略信息变更异常，异常：{}", desktopId, e);
        }
    }

    private boolean checkIsRecoverableVDI(UUID desktopId) throws BusinessException {
        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);
        CbbDeskStrategyDTO cbbDeskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(cbbDeskDTO.getStrategyId());
        return Objects.equals(CbbCloudDeskPattern.RECOVERABLE, cbbDeskStrategyDTO.getPattern());
    }

    @Override
    public void updateVDIConfigAsync(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");
        ThreadExecutorsUtils.executeUpdateDesktopStrategy(() -> {
            doUpdateVDIConfig(desktopId);
        });
    }

    @Override
    public void updateVDIConfigSync(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");
        doUpdateVDIConfig(desktopId);
    }

    private void doUpdateVDIConfig(UUID desktopId) {
        try {
            LOGGER.info("云桌面[{}]关闭DesktopUpdateServiceImpl.doUpdateVDIConfig处理，开始执行关机后策略信息变更", desktopId);
            CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);
            if (cbbDeskDTO.getDesktopPoolType() != DesktopPoolType.COMMON) {
                // 动态池桌面所有的配置必须和池配置一致，自动变更
                // 静态池非独立配置规格（额外盘例外，因为无法保证池规格中额外盘删除或修改的是桌面对应的那个磁盘）需要和池规格一致，自动变更
                updateDesktopInfoByPoolDesk(cbbDeskDTO);
                return;
            }

            // 应用主机桌面
            RcaHostDTO rcaHost = rcaHostAPI.getByDeskId(cbbDeskDTO.getDeskId());
            if (rcaHost != null) {
                updateDesktopInfoByAppDesk(cbbDeskDTO, rcaHost);
                return;
            }

            // 普通桌面
            updateDesktopInfoByCommonDesk(cbbDeskDTO);
        } catch (BusinessException e) {
            LOGGER.error("云桌面[{}]关闭，DesktopUpdateServiceImpl中执行策略信息变更异常，异常：{}", desktopId, e);
        }
    }

    private void updateDesktopInfoByPoolDesk(CbbDeskDTO cbbDeskDTO) throws BusinessException {
        CbbDesktopPoolDTO desktopPool = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(cbbDeskDTO.getDesktopPoolId());

        // 只支持单会话动态池桌面自动变更
        if (Objects.equals(desktopPool.getPoolModel(), CbbDesktopPoolModel.DYNAMIC)) {
            desktopPoolMgmtAPI.syncStrategy(desktopPool, cbbDeskDTO);

            desktopPoolMgmtAPI.syncDeskSpec(desktopPool, cbbDeskDTO);

            desktopPoolMgmtAPI.syncImageTemplate(desktopPool, cbbDeskDTO, null);

            desktopPoolMgmtAPI.syncNetworkStrategy(desktopPool, cbbDeskDTO);

            desktopPoolMgmtAPI.syncSoftwareStrategy(desktopPool, cbbDeskDTO);

            desktopPoolMgmtAPI.syncUserProfileStrategy(desktopPool, cbbDeskDTO);
        } else if (Objects.equals(desktopPool.getPoolModel(), CbbDesktopPoolModel.STATIC)) {
            if (BooleanUtils.isTrue(cbbDeskDTO.getEnableCustom())) {
                // 静态池独立配置按普通桌面处理
                updateDesktopInfoByCommonDesk(cbbDeskDTO);
            } else {
                // 静态池非独立配置规格（额外盘例外，因为无法保证池规格中额外盘删除或修改的是桌面对应的那个磁盘）需要和池规格一致，自动变更
                desktopPoolMgmtAPI.syncDeskSpec(desktopPool, cbbDeskDTO);
            }
        }
    }

    private void updateDesktopInfoByAppDesk(CbbDeskDTO cbbDeskDTO, RcaHostDTO rcaHost) throws BusinessException {
        // 仅支持修订动态派生桌面
        if (RcaEnum.HostSourceType.VDI != rcaHost.getHostSourceType()) {
            return;
        }

        RcaAppPoolBaseDTO rcaAppPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(rcaHost.getPoolId());
        try {
            rcoAppPoolAPI.syncSpec(rcaAppPoolBaseDTO, cbbDeskDTO);
        } catch (BusinessException ex) {
            LOGGER.error("变更应用池规格发生异常，池id: [{}], ex: ", rcaAppPoolBaseDTO.getId(), ex);
        }

        try {
            rcoAppPoolAPI.syncImageTemplate(rcaAppPoolBaseDTO, cbbDeskDTO, null);
        } catch (BusinessException ex) {
            LOGGER.error("变更应用池镜像发生异常，池id: [{}], ex: ", rcaAppPoolBaseDTO.getId(), ex);
        }

        try {
            rcoAppPoolAPI.syncNetworkStrategy(rcaAppPoolBaseDTO, cbbDeskDTO);
        } catch (BusinessException ex) {
            LOGGER.error("变更应用池网络发生异常，池id: [{}], ex: ", rcaAppPoolBaseDTO.getId(), ex);
        }

        try {
            rcoAppPoolAPI.syncMainStrategy(rcaAppPoolBaseDTO, cbbDeskDTO);
        } catch (BusinessException ex) {
            LOGGER.error("变更应用池策略发生异常，池id: [{}], ex: ", rcaAppPoolBaseDTO.getId(), ex);
        }
    }

    private void updateDesktopInfoByCommonDesk(CbbDeskDTO cbbDeskDTO) throws BusinessException {

        UUID imageId = cbbDeskDTO.getImageTemplateId();
        if (Objects.nonNull(cbbDeskDTO.getWillApplyImageId())) {
            // 处理待变更镜像模板
            userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(cbbDeskDTO.getDeskId()));
            CbbDeskDTO tempDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(cbbDeskDTO.getDeskId());
            // 重新取一次
            imageId = tempDeskDTO.getImageTemplateId();
        }
        // 判断是否需要自动变更桌面规格
        CbbDeskSpecDTO deskSpecDTO = cbbDeskSpecAPI.getById(cbbDeskDTO.getDeskSpecId());
        if (Objects.isNull(deskSpecDTO)) {
            LOGGER.debug("桌面{}无规格信息，无需规格自动检测变更", cbbDeskDTO.getDeskId());
            return;
        }
        deskSpecAPI.checkImageOsSupportGpu(imageId, deskSpecDTO.getVgpuInfoDTO(), cbbDeskDTO.getClusterId());

        // 判断桌面spec是否有需要变更的
        if (deskSpecAPI.isSkipChangeDeskSpec(cbbDeskDTO.getDeskId())) {
            LOGGER.debug("桌面{}无需变更规格信息", cbbDeskDTO.getDeskId());
            return;
        }
        CbbUpdateDeskSpecRequest cbbUpdateDeskSpecRequest = deskSpecAPI.buildUpdateDeskSpecRequest(cbbDeskDTO.getDeskId(), deskSpecDTO);
        cbbUpdateDeskSpecRequest.setEnableCustom(cbbDeskDTO.getEnableCustom());
        LOGGER.info("更新桌面={}，更新规格信息={}", cbbDeskDTO.getDeskId(), JSON.toJSONString(cbbUpdateDeskSpecRequest));
        cbbVDIDeskMgmtAPI.updateDeskSpec(cbbUpdateDeskSpecRequest);
    }

}
