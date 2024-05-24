package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.deskoperate.CbbRestoreDeskRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbUserDesktopSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDetachDesktopUserDiskNotifyRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoAppPoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.UserPoolDiskHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * 用户和云桌面关系通知SPI接口实现
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月24日
 *
 * @author brq
 */
public class CbbUserDesktopSPIImpl implements CbbUserDesktopSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbUserDesktopSPIImpl.class);

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private UserPoolDiskHelper userPoolDiskHelper;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcoAppPoolAPI rcoAppPoolAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;


    @Override
    public void unbindUserAndDesktopRelation(UUID deskId) {
        Assert.notNull(deskId, "deskId不能为null");
        try {
            userDesktopService.unbindUserAndDesktopRelation(deskId);
        } catch (Exception e) {
            LOGGER.warn("解绑用户和池桌面[{}]的关系出现异常:{} ", deskId, e);
        }
    }

    @Override
    public void tryToApplyNewImageTemplateIfNeed(UUID deskId, CbbCloudDeskPattern pattern) {
        Assert.notNull(deskId, "deskId is not null");
        Assert.notNull(pattern, "pattern is not null");

        CbbDeskDTO cbbDeskDTO = null;
        try {
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        } catch (BusinessException e) {
            LOGGER.error(String.format("获取云桌面[%s]信息失败", deskId), e);
        }

        if (Objects.isNull(cbbDeskDTO) || Objects.isNull(cbbDeskDTO.getWillApplyImageId())) {
            return;
        }
        //处理待变更镜像模板
        boolean isSuccess = userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(deskId));
        //关机变更镜像模板失败，执行还原
        if (isSuccess) {
            // 单会话动态池的桌面进行自动判断池策略是否一致，自动变更相关策略，网络策略等
            autoUpdateDesktopPoolVDIConfig(cbbDeskDTO);

            // 动态应用池主机关机后进行自动判断池策略是否一致，自动变更相关策略，网络策略等
            autoUpdateAppPoolVDIConfig(cbbDeskDTO);
            return;
        }

        // 变更失败了继续执行还原动作
        restore(cbbDeskDTO, pattern);
    }

    @Override
    public void detachDiskNotice(CbbDetachDesktopUserDiskNotifyRequest request) {
        Assert.notNull(request, "detachDiskNotice request can not be null");
        try {
            userPoolDiskHelper.detachDiskFollowUpHandler(request);
        } catch (BusinessException e) {
            LOGGER.error("卸载磁盘[{}]后续业务处理出现异常", request.getDiskId(), e);
        }
    }

    private void restore(CbbDeskDTO cbbDeskDTO, CbbCloudDeskPattern pattern) {
        //执行还原操作
        if (cbbDeskDTO != null && pattern == CbbCloudDeskPattern.RECOVERABLE) {
            try {
                LOGGER.info("云桌面[{}]变更镜像失败，开始执行还原", cbbDeskDTO.getName());
                cloudDesktopOperateAPI.restore(new CbbRestoreDeskRequest(cbbDeskDTO.getDeskId()));
            } catch (BusinessException businessException) {
                LOGGER.error("云桌面[{}]变更镜像失败，执行还原异常", cbbDeskDTO.getName(), businessException);
            }
        }
    }

    /**
     * 单会话动态池的桌面进行自动判断池策略是否一致，自动变更相关策略，网络策略等
     *
     * @param cbbDeskDTO cbbDeskDTO
     */
    private void autoUpdateDesktopPoolVDIConfig(CbbDeskDTO cbbDeskDTO) {
        if (Objects.isNull(cbbDeskDTO.getDesktopPoolId())) {
            return;
        }
        CbbDesktopPoolDTO desktopPool = null;
        try {
            desktopPool = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(cbbDeskDTO.getDesktopPoolId());

            // 只支持单会话动态池桌面自动变更
            if (desktopPool.getPoolModel() != CbbDesktopPoolModel.DYNAMIC) {
                return;
            }
            desktopUpdateService.updateVDIConfigSync(cbbDeskDTO.getDeskId());
        } catch (BusinessException e) {
            LOGGER.error("动态池的桌面[{}]进行自动判断池策略是否一致，自动变更相关策略，网络策略后续业务处理出现异常", cbbDeskDTO.getDeskId(), e);
        }

        // 关机后 动态池桌面，需要解绑用户和桌面的关系
        if (Objects.nonNull(desktopPool) && desktopPool.getPoolModel() == CbbDesktopPoolModel.DYNAMIC) {
            unbindUserAndDesktopRelation(cbbDeskDTO.getDeskId());
        }
    }

    /**
     * 动态应用池的桌面进行自动判断池策略是否一致，自动变更相关策略，网络策略等
     *
     * @param cbbDeskDTO cbbDeskDTO
     */
    private void autoUpdateAppPoolVDIConfig(CbbDeskDTO cbbDeskDTO) {
        // 应用主机桌面
        RcaHostDTO rcaHost = rcaHostAPI.getByDeskId(cbbDeskDTO.getDeskId());
        if (rcaHost == null) {
            LOGGER.info("该桌面不是应用主机，跳过处理，主机id: [{}]", cbbDeskDTO.getDeskId());
            return;
        }

        if (RcaEnum.HostSourceType.THIRD_PARTY.equals(rcaHost.getHostSourceType())) {
            LOGGER.info("该桌面不是派生主机，跳过处理，主机id: [{}]", cbbDeskDTO.getDeskId());
            return;
        }

        if (rcaHost.getPoolId() == null) {
            LOGGER.info("应用主机不存在绑定应用池，跳过处理，主机id: [{}]", cbbDeskDTO.getDeskId());
            return;
        }

        RcaAppPoolBaseDTO appPoolBaseDTO = null;
        try {
            appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(rcaHost.getPoolId());
            desktopUpdateService.updateVDIConfigSync(cbbDeskDTO.getDeskId());
        } catch (BusinessException e) {
            LOGGER.error("应用池的桌面[{}]进行自动判断池策略是否一致，自动变更相关策略，网络策略后续业务处理出现异常", cbbDeskDTO.getDeskId(), e);
        }

        // 关机后 动态池桌面，需要解绑用户和桌面的关系
        if (Objects.nonNull(appPoolBaseDTO) && RcaEnum.PoolType.DYNAMIC.equals(appPoolBaseDTO.getPoolType())) {
            unbindUserAndDesktopRelation(cbbDeskDTO.getDeskId());
            try {
                rcaHostSessionAPI.deleteAllByHostId(rcaHost.getId());
            } catch (Exception ex) {
                LOGGER.error("解绑应用主机会话信息发生异常，主机id: [{}], ex : ", rcaHost.getId(), ex);
            }
        }
    }
}
