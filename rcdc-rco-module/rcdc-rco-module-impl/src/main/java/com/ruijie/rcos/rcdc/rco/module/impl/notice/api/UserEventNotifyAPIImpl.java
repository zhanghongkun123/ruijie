package com.ruijie.rcos.rcdc.rco.module.impl.notice.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.spi.UserLoginEventNoticeSPI;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserGroupOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.RcdcGuestToolCmdKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CreateDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.CmsDockingProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.AdUserMessageSendDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.UserOperSyncNotifyStrategy;
import com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.init.UserOperSyncNotifyStrategyFactory;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.IDV;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.VDI;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月31日
 *
 * @author jarman
 */
public class UserEventNotifyAPIImpl implements UserEventNotifyAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventNotifyAPIImpl.class);

    @Autowired
    private UserOperSyncNotifyStrategyFactory userOperSyncNotifyStrategyFactory;

    @Autowired
    private CmsDockingProducerAPI cmsDockingProducerAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private CreateDesktopService createDesktopService;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserLoginEventNoticeSPI userLoginEventNoticeSPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private IacUserGroupMgmtAPI iacUserGroupMgmtAPI;

    private static final ExecutorService NOTIFY_USER_AUTHORITY_CHANGED_THREAD_POOL =
            ThreadExecutors.newBuilder("notifyUserAuthorityChanged").maxThreadNum(10).queueSize(10000).build();


    @Override
    public void domainUserDisabled(List<UUID> userIdList) {
        Assert.notEmpty(userIdList, "userIds can not be null");
        uwsDockingAPI.notifyUserDisabled(userIdList);

        for (UUID userId : userIdList) {
            IacUserDetailDTO cbbUserDetailDTO = null;
            try {
                cbbUserDetailDTO = cbbUserAPI.getUserDetail(userId);
            } catch (BusinessException e) {
                LOGGER.error("查询用户发生异常，用户id: {}", userId);
                continue;
            }
            if (cbbUserDetailDTO.getUserType() != IacUserTypeEnum.THIRD_PARTY) {
                LOGGER.info("用户类型不为第三方用户，跳过处理，用户id: {}", userId);
                return;
            }
            try {
                // 通知终端退出
                notifyTerminal(cbbUserDetailDTO);
                // 关闭桌面
                closeVm(cbbUserDetailDTO);
            } catch (Exception ex) {
                LOGGER.error("第三用户禁用后置处理发生异常，用户id: {} ex:", userId, ex);
            }
        }
    }

    @Override
    public void domainUserAuthorityChanged(UUID userId) {
        Assert.notNull(userId, "userId cannot be null");
        try {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
            doSendMsg(userId, userDetail.getAdUserAuthority());
        } catch (Exception e) {
            LOGGER.error("发送用户[" + userId + "]消息失败", e);
        }
    }

    @Override
    public void domainUserSyncFinish(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId cannot be null");
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        UUID groupId = userDetail.getGroupId();
        // 查用户组有没配置vdi桌面，有配置则创建桌面
        UserGroupDesktopConfigDTO desktopConfigDTO = userDesktopConfigAPI.getUserGroupDesktopConfig(groupId, UserCloudDeskTypeEnum.VDI);
        if (desktopConfigDTO == null) {
            LOGGER.info("用户[{}]所属组未配置桌面，不生成桌面", userDetail.getUserName());
            return;
        }
        try {
            CreateCloudDesktopRequest createDesktopRequest = new CreateCloudDesktopRequest();
            createDesktopRequest.setDesktopImageId(desktopConfigDTO.getImageTemplateId());
            createDesktopRequest.setNetworkId(desktopConfigDTO.getNetworkId());
            createDesktopRequest.setStrategyId(desktopConfigDTO.getStrategyId());
            createDesktopRequest.setUserId(userId);
            createDesktopRequest.setClusterId(desktopConfigDTO.getClusterId());
            createDesktopRequest.setUserProfileStrategyId(desktopConfigDTO.getUserProfileStrategyId());
            createDesktopRequest.setPlatformId(desktopConfigDTO.getPlatformId());
            // 赋值组配置的规格信息
            createDesktopRequest.setDeskSpec(cbbDeskSpecAPI.getById(desktopConfigDTO.getDeskSpecId()));
            UserDesktopEntity desktopEntity = createDesktopService.create(createDesktopRequest);
            auditLogAPI.recordLog(BusinessKey.RCDC_USER_CLOUDDESKTOP_CREATE_SUC_LOG, userDetail.getUserName(), desktopEntity.getDesktopName());
        } catch (BusinessException e) {
            LOGGER.error("创建用户[" + userDetail.getUserName() + "]桌面失败", e);
            throw e;
        }
    }

    @Override
    public void userChanged(UserOperNotifyDTO userOperNotifyDTO) {
        Assert.notNull(userOperNotifyDTO, "userOperNotifyDTO is null");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oper", userOperNotifyDTO.getOper());
            jsonObject.put("timestamp", userOperNotifyDTO.getTimestamp());
            jsonObject.put("content", userOperNotifyDTO.getNotifyDto());
            String syncMessage = jsonObject.toJSONString();
            cmsDockingProducerAPI.syncMessageToCMS(syncMessage);
        } catch (Exception e) {
            LOGGER.error("CMS对接：用户同步失败。content = {}", JSONObject.toJSONString(userOperNotifyDTO.getNotifyDto()), e);
        }

        // 用户操作成功后执行的逻辑
        UserOperSyncNotifyDTO userOperSyncDto = userOperNotifyDTO.getUserOperSyncDto();
        if (!StringUtils.equals(userOperSyncDto.getDispatcherKey(), Constants.USER_IDENTITY_CONFIG_SYNC_NOTIFY)) {
            return;
        }
        try {
            String oper = userOperSyncDto.getOper();
            LOGGER.info("userChanged<{}>", oper);
            UserOperSyncNotifyStrategy userOperSyncNotifyStrategy = userOperSyncNotifyStrategyFactory.getUserOperSyncNotifyStrategy(oper);
            Assert.notNull(userOperSyncNotifyStrategy, "userOperSyncNotifyStrategy must not be null");
            userOperSyncNotifyStrategy.syncNotifyUserChange(userOperSyncDto);
        } catch (Exception e) {
            LOGGER.error("syncNotifyUserChange fail,content = " + JSON.toJSONString(userOperSyncDto), e);
        }
    }

    @Override
    public void userGroupChanged(UserGroupOperNotifyDTO userGroupOperNotifyDTO) {
        Assert.notNull(userGroupOperNotifyDTO, "userGroupOperNotifyDTO is null");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oper", userGroupOperNotifyDTO.getOper());
            jsonObject.put("timestamp", userGroupOperNotifyDTO.getTimestamp());
            jsonObject.put("content", userGroupOperNotifyDTO.getUserGroupOperNotifyContentDTO());
            cmsDockingProducerAPI.syncMessageToCMS(jsonObject.toJSONString());
        } catch (Exception e) {
            LOGGER.error("CMS对接：用户组同步失败。content = {}", JSONObject.toJSONString(userGroupOperNotifyDTO.getUserGroupOperNotifyContentDTO()), e);
        }
        UserOperSyncNotifyDTO userOperSyncNotifyDTO = userGroupOperNotifyDTO.getUserOperSyncNotifyDTO();
        if (!StringUtils.equals(userOperSyncNotifyDTO.getDispatcherKey(), Constants.USER_IDENTITY_CONFIG_SYNC_NOTIFY)) {
            return;
        }
        try {
            String oper = userOperSyncNotifyDTO.getOper();
            LOGGER.info("userGroupChanged<{}>", userOperSyncNotifyDTO.getOper());

            UserOperSyncNotifyStrategy userOperSyncNotifyStrategy = userOperSyncNotifyStrategyFactory.getUserOperSyncNotifyStrategy(oper);
            Assert.notNull(userOperSyncNotifyStrategy, "userOperSyncNotifyStrategy must not be null");
            userOperSyncNotifyStrategy.syncNotifyUserChange(userOperSyncNotifyDTO);
        } catch (Exception e) {
            LOGGER.error("userGroupChanged fail,content = " + JSON.toJSONString(userOperSyncNotifyDTO), e);
        }
    }

    @Override
    public void syncNotifyUserChanged(UserOperSyncNotifyDTO userOperSyncNotifyDTO) {
        Assert.notNull(userOperSyncNotifyDTO, "userOperSyncNotifyDTO is null");
        if (!StringUtils.equals(userOperSyncNotifyDTO.getDispatcherKey(), Constants.USER_IDENTITY_CONFIG_SYNC_NOTIFY)) {
            return;
        }
        try {
            String oper = userOperSyncNotifyDTO.getOper();
            LOGGER.info("身份验证SPI收到操作请求<{}>", userOperSyncNotifyDTO.getOper());

            UserOperSyncNotifyStrategy userOperSyncNotifyStrategy = userOperSyncNotifyStrategyFactory.getUserOperSyncNotifyStrategy(oper);
            Assert.notNull(userOperSyncNotifyStrategy, "userOperSyncNotifyStrategy must not be null");
            userOperSyncNotifyStrategy.syncNotifyUserChange(userOperSyncNotifyDTO);
        } catch (Exception e) {
            LOGGER.error("身份验证SPI收到操作请求记录失败。content = " + JSON.toJSONString(userOperSyncNotifyDTO), e);
        }
    }

    @Override
    public DtoResponse userLoginSuccess(UserLoginNoticeDTO request) {
        Assert.notNull(request, "request can not be null");
        UUID userId = request.getUserId();
        Assert.notNull(userId, "user id can not be null");
        return userLoginEventNoticeSPI.notify(request);
    }

    @Override
    public void notifyUserGroupAuthorityChanged(UUID groupId, IacAdUserAuthorityEnum adUserAuthority) {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(adUserAuthority, "adUserAuthority can not be null");
        
        List<UUID> groupIdList;
        try {
            groupIdList = iacUserGroupMgmtAPI.listByGroupIdRecursive(groupId);
        } catch (Exception e) {
            // 失败只影响发消息，不影响主业务更新权限
            LOGGER.error("递归获取用户组id失败：{}", groupId, e);
            return;
        }
        if (CollectionUtils.isEmpty(groupIdList)) {
            return;
        }
        for (UUID id : groupIdList) {
            NOTIFY_USER_AUTHORITY_CHANGED_THREAD_POOL.execute(() -> {
                try {
                    sendUserAuthChangeMessage(id, adUserAuthority);
                } catch (Exception e) {
                    LOGGER.error("发送用户权限变更失败", e);
                }
            });
        }
    }
    
    private void sendUserAuthChangeMessage(UUID groupId, IacAdUserAuthorityEnum adUserAuthority) throws BusinessException {
        // 分页查，1次查1000条
        IacPageResponse<IacUserDetailDTO> pageResult = userService.pageQueryByGroupId(groupId, 0);
        // 总页数
        int totalPage = (int) Math.ceil((double) pageResult.getTotal() / com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.MAX_QUERY_LIST_SIZE);
        for (int page = 0; page < totalPage; page++) {
            // 前面已查过，不重复查
            if (page == 0) {
                for (IacUserDetailDTO iacUserDetailDTO : pageResult.getItemArr()) {
                    doSendMsg(iacUserDetailDTO.getId(), adUserAuthority);
                }
                continue;
            }
            pageResult = userService.pageQueryByGroupId(groupId, page);
            if (pageResult.getTotal() == 0) {
                break;
            }
            for (IacUserDetailDTO iacUserDetailDTO : pageResult.getItemArr()) {
                doSendMsg(iacUserDetailDTO.getId(), adUserAuthority);
            }
        }
    }

    @Override
    public void notifyUserAuthorityChanged(UUID userId, IacAdUserAuthorityEnum adUserAuthority) {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(adUserAuthority, "adUserAuthority can not be null");
        NOTIFY_USER_AUTHORITY_CHANGED_THREAD_POOL.execute(() -> {
            try {
                doSendMsg(userId, adUserAuthority);
            } catch (Exception e) {
                LOGGER.error("发送用户[" + userId + "]ad权限变更消息失败", e);
            }
        });
    }

    private void notifyTerminal(IacUserDetailDTO cbbUserDetailDTO) {
        LOGGER.info("禁用用户主动推送用户状态，用户为[{}]", cbbUserDetailDTO.getUserName());
        userMgmtAPI.syncUserInfoToTerminal(cbbUserDetailDTO);
        LOGGER.info("禁用用户并退出终端会话，用户为[{}]", cbbUserDetailDTO.getUserName());
        userInfoAPI.userLogout(cbbUserDetailDTO.getId());
        // 通知web端用户退出
        userInfoAPI.notifyWebUserLogout(cbbUserDetailDTO.getId());
    }

    private boolean closeVm(IacUserDetailDTO cbbUserDetailDTO) {
        Assert.notNull(cbbUserDetailDTO, "cbbUserDetailDTO is null");
        UUID userId = cbbUserDetailDTO.getId();
        String userName = cbbUserDetailDTO.getUserName();
        // 关闭用户关联的桌面
        String logFlag = null;
        boolean isSuccess = true;
        final List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByUserId(userId);
        for (CloudDesktopDTO desktop : desktopList) {
            try {
                logFlag = isBlank(desktop.getDesktopName()) ? desktop.getId().toString() : desktop.getDesktopName();
                if (!Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.RUNNING.toString())
                        && !Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                    continue;
                }
                CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(desktop.getDesktopCategory());
                LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), desktop.getId());
                if (deskType == IDV) {
                    CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
                    shutdownDeskIDVDTO.setId(desktop.getId());
                    shutdownDeskIDVDTO.setIsForce(Boolean.FALSE);
                    shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
                    cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
                } else if (deskType == VDI) {
                    if (Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.SLEEP.toString())) {
                        cloudDesktopOperateAPI.start(new CloudDesktopStartRequest(desktop.getCbbId()));
                    }
                    cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(desktop.getId(), Boolean.FALSE));
                } else {
                    LOGGER.error("用户[{}]不支持对云桌面类型：[{}]，进行关闭", logFlag, deskType.name());
                }
            } catch (Exception e) {
                LOGGER.error(String.format("用户[%s]禁用成功，关闭桌面[%s]失败", userName, logFlag), e);
                isSuccess = false;
            }
        }

        return isSuccess;
    }

    private void doSendMsg(UUID userId, IacAdUserAuthorityEnum adUserAuthority) {
        // 根据userId获取用户绑定在线状态云桌面
        List<ViewUserDesktopEntity> runningDesktopList =
                viewDesktopDetailDAO.findViewDesktopEntitiesByUserIdAndDeskStateAndIsDelete(userId, CbbCloudDeskState.RUNNING.name(), false);
        if (CollectionUtils.isEmpty(runningDesktopList)) {
            LOGGER.debug("user[{}] can not found running desktop, send the message[{}] later", userId);
            return;
        }
        AdUserMessageSendDTO messageDTO = buildMessageMessageDTO(adUserAuthority);
        CbbGuesttoolMessageDTO guesttoolMessageDTO = buildSendMessageRequest(messageDTO);
        // 逐个向运行中的桌面发送消息
        for (ViewUserDesktopEntity viewDesktop : runningDesktopList) {
            guesttoolMessageDTO.setDeskId(viewDesktop.getCbbDesktopId());
            LOGGER.debug("发送用户消息, 请求内容{}", JSON.toJSONString(guesttoolMessageDTO));
            try {
                guestToolMessageAPI.syncRequest(guesttoolMessageDTO);
            } catch (Exception e) {
                LOGGER.error("用户[" + userId + "]消息发送失败", e);
            }
        }
    }

    private static CbbGuesttoolMessageDTO buildSendMessageRequest(AdUserMessageSendDTO messageSendDTO) {
        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setBody(JSON.toJSONString(messageSendDTO));
        dto.setCmdId(RcdcGuestToolCmdKey.RCDC_GT_CMD_ID_AD_USER);
        dto.setPortId(RcdcGuestToolCmdKey.RCDC_GT_PORT_ID_AD_USER);
        LOGGER.info("CbbGuesttoolChannelDoSendRequest:" + JSON.toJSONString(dto));
        return dto;
    }

    private static AdUserMessageSendDTO buildMessageMessageDTO(IacAdUserAuthorityEnum adUserAuthority) {
        AdUserMessageSendDTO messageSendDTO = new AdUserMessageSendDTO();
        messageSendDTO.setCode(0);
        messageSendDTO.setMessage("");
        AdUserMessageSendDTO.Body body = new AdUserMessageSendDTO.Body();
        body.setAdUserAuthority(adUserAuthority.getAdUserAuthority());
        messageSendDTO.setContent((body));
        LOGGER.info("AdUserMessageSendDTO:" + JSON.toJSONString(messageSendDTO));
        return messageSendDTO;
    }
}
