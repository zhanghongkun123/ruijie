package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbSyncLoginAccountPermissionEnums;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserPasswordGtInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.UserPasswordMessageEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Description: 用户通知接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-14 16:58:00
 *
 * @author zjy
 */
public class UserNotifyAPIImpl implements UserNotifyAPI {


    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotifyAPIImpl.class);

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private UserDesktopMgmtAPI desktopMgmtAPI;

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    public void notifyUserPwdToGt(UUID userId) {
        Assert.notNull(userId, "userId not be empty");

        RcoViewUserEntity userInfo = userServiceImpl.getUserInfoById(userId);
        if (userInfo == null) {
            LOGGER.info("用户信息不存在，用户id: {}", userId.toString());
            return;
        }

        List<CloudDesktopDTO> desktopDTOList = desktopMgmtAPI.getAllDesktopByUserId(userInfo.getId());
        List<CloudDesktopDTO> onlineDesktopDTOList = desktopDTOList.stream().filter(item ->
                CbbCloudDeskState.RUNNING.name().equals(item.getDesktopState())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(onlineDesktopDTOList)) {
            LOGGER.info("用户无在线桌面，不发送gt通知，用户id: {}", userId.toString());
            return;
        }

        for (CloudDesktopDTO cloudDesktopDTO : onlineDesktopDTOList) {
            try {
                sendUserPwdUpdateToGt(cloudDesktopDTO.getId(), userInfo.getUserName(), userInfo.getPassword());
            } catch (BusinessException e) {
                LOGGER.info("密码变更通知发送到桌面发生异常，ex: ", e);
            }
        }
    }

    @Override
    public void notifyUserPwdToGtByDesktopId(UUID desktopId, String userName, String password) {
        Assert.notNull(desktopId, "desktopId cannot be null");
        Assert.hasText(userName, "userName not be empty");
        Assert.hasText(password, "password not be empty");

        try {
            sendUserPwdUpdateToGt(desktopId, userName, password);
        } catch (BusinessException e) {
            LOGGER.info("密码变更通知发送到桌面发生异常，ex: ", e);
        }
    }

    private void sendUserPwdUpdateToGt(UUID desktopId, String userName, String password) throws BusinessException {
        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.GT_GET_USER_PWD_CMD_ID));
        dto.setPortId(GuestToolCmdId.GT_GET_USER_PWD_PORT_ID);
        dto.setDeskId(desktopId);

        UserPasswordGtInfoDTO userPasswordGtInfoDTO = new UserPasswordGtInfoDTO();
        userPasswordGtInfoDTO.setCode(UserPasswordMessageEnum.MESSAGE_SUCCESS.getCode());
        userPasswordGtInfoDTO.setMessage(UserPasswordMessageEnum.MESSAGE_SUCCESS.getMessage());
        UserPasswordGtInfoDTO.Content content = new UserPasswordGtInfoDTO.Content();
        content.setUserName(userName);
        content.setPassword(password);
        content.setChangeUserName(false);
        content.setAutoLogin(false);
        content.setUserGroup(CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue());

        CloudDesktopDetailDTO desktopDetail = desktopMgmtAPI.getDesktopDetailById(desktopId);
        // 仅普通vdi桌面和静态池vdi桌面，需要关注自动登录相关参数
        if (CbbCloudDeskType.VDI.name().equals(desktopDetail.getDeskType())
                && (DesktopPoolType.COMMON.name().equals(desktopDetail.getDesktopPoolType())
                || DesktopPoolType.STATIC.name().equals(desktopDetail.getDesktopPoolType()))) {


            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(
                    desktopDetail.getDesktopImageId());
            if (CbbOsType.isDesktopSyncAccountOS(imageTemplateDetail.getOsType())) {
                CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(desktopDetail.getDesktopStrategyId());
                content.setChangeUserName(deskStrategy.getDesktopSyncLoginAccount() == null ?
                        Boolean.FALSE : deskStrategy.getDesktopSyncLoginAccount());
                content.setAutoLogin(deskStrategy.getDesktopSyncLoginPassword() == null ?
                        Boolean.FALSE : deskStrategy.getDesktopSyncLoginPassword());
                content.setUserGroup(deskStrategy.getDesktopSyncLoginAccountPermission() == null ?
                        CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue() :
                        deskStrategy.getDesktopSyncLoginAccountPermission().getValue());
            }
        }

        userPasswordGtInfoDTO.setContent(content);
        dto.setBody(JSON.toJSONString(userPasswordGtInfoDTO));
        LOGGER.info("发送密码变更通知消息内容为：{}", JSON.toJSONString(userPasswordGtInfoDTO));
        try {
            guestToolMessageAPI.asyncRequest(dto);
        } catch (Exception e) {
            LOGGER.error("发送密码变更通知到云桌面[" + desktopId + "]失败", e);
        }
    }
}
