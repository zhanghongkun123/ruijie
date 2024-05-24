package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 处理软删除云桌面消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月8日
 *
 * @author yinfeng
 */
@Service
public class RecoverDeskHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDeskHandler.class);

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private HostUserService hostUserService;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.RECOVER_DESK == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws BusinessException {
        if (!request.getIsSuccess()) {
            LOGGER.error("收到恢复云桌面[{}] 失败消息[{}]，删除 user desktop 数据", request.getDeskId(),
                    request.getErrorMsg());
            return;
        }

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(request.getDeskId());
        //存在rccm纳管，需要处理用户集群关系缓存
        pushUserToRCCM(request, cbbDeskDTO);
        LOGGER.info("收到恢复云桌面消息, 桌面id[{}]", request.getDeskId());
        uwsDockingAPI.notifyDesktopRecover(request.getDeskId());
        notifyDesktopPoolRecover(cbbDeskDTO, request.getDeskId());
    }

    private void pushUserToRCCM(CbbDeskOperateNotifyRequest request, CbbDeskDTO cbbDeskDTO) {
        try {
            RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
            // 区分单会话与多会话桌面
            if (CbbDesktopSessionType.SINGLE == cbbDeskDTO.getSessionType()) {
                if (rccmServerConfig != null && rccmServerConfig.hasHealth()) {
                    UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(request.getDeskId());
                    LOGGER.info("收到恢复云桌面[{}]成功,通知rccm添加用户集群关系缓存", request.getDeskId());
                    //推送用户数据
                    rccmManageService.pushUserByUserIdList(Collections.singletonList(userDesktopEntity.getUserId()));
                }
            } else {
                List<UUID> userIdList = desktopAPI.findUserIdByDeskId(request.getDeskId());
                if (CollectionUtils.isEmpty(userIdList)) {
                    LOGGER.info("收到恢复云桌面[{}]成功,通知rccm添加用户集群关系缓存", request.getDeskId());
                    //推送用户数据
                    rccmManageService.pushUserByUserIdList(userIdList);
                }
            }
        } catch (Exception e) {
            LOGGER.error("恢复云桌面[{}]，通知rccm添加用户集群关系异常，", request.getDeskId(), e);
        }
    }

    private void notifyDesktopPoolRecover(CbbDeskDTO cbbDeskDTO, UUID deskId) {
        try {
            if (Objects.isNull(cbbDeskDTO.getDesktopPoolType()) || Objects.isNull(cbbDeskDTO.getDesktopPoolId())
                    || !DesktopPoolType.isPoolDesktop(cbbDeskDTO.getDesktopPoolType())) {
                return;
            }

            UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(deskId);
            if (Objects.nonNull(userDesktopEntity.getUserId()) && !desktopPoolUserMgmtAPI.checkUserInDesktopPool(cbbDeskDTO.getDesktopPoolId(),
                    userDesktopEntity.getUserId())) {
                desktopPoolUserMgmtAPI.addUserToDesktopPool(cbbDeskDTO.getDesktopPoolId(), userDesktopEntity.getUserId());
                IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(userDesktopEntity.getUserId());
                CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(cbbDeskDTO.getDesktopPoolId());
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_RECOVER_POOL_DESK_ADD_USER, cbbDeskDTO.getName(), userDetailDTO.getUserName(),
                        desktopPoolDTO.getName());
            }

            if (CbbDesktopSessionType.MULTIPLE != cbbDeskDTO.getSessionType()) {
                return;
            }
            List<UUID> userIdList = desktopAPI.findUserIdByDeskId(deskId);
            if (CollectionUtils.isEmpty(userIdList)) {
                return;
            }
            // 添加多会话桌面的关联用户与桌面池关系
            for (UUID userId : userIdList) {
                boolean isInDesktopPool = desktopPoolUserMgmtAPI.checkUserInDesktopPool(cbbDeskDTO.getDesktopPoolId(), userId);
                if (!isInDesktopPool) {
                    desktopPoolUserMgmtAPI.addUserToDesktopPool(cbbDeskDTO.getDesktopPoolId(), userId);
                    IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(userId);
                    CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(cbbDeskDTO.getDesktopPoolId());
                    auditLogAPI.recordLog(BusinessKey.RCDC_RCO_RECOVER_POOL_DESK_ADD_USER, cbbDeskDTO.getName(), userDetailDTO.getUserName(),
                            desktopPoolDTO.getName());
                }
            }

            // 多会话桌面指定桌面池恢复后，更新桌面的desktopPoolId
            List<HostUserEntity> hostUserEntityList = hostUserService.findByDeskId(deskId);
            if (CbbDesktopSessionType.MULTIPLE == cbbDeskDTO.getSessionType() && !CollectionUtils.isEmpty(hostUserEntityList)) {
                hostUserEntityList.stream().filter(entity -> !cbbDeskDTO.getDesktopPoolId().equals(entity.getDesktopPoolId()))
                        .forEach(entity -> {
                            entity.setDesktopPoolId(cbbDeskDTO.getDesktopPoolId());
                            hostUserService.updateHostUserEntity(entity);
                        });
            }

        } catch (BusinessException e) {
            LOGGER.error("恢复云桌面[{}]后，桌面池相关后续操作异常", deskId, e);
        }
    }
}
