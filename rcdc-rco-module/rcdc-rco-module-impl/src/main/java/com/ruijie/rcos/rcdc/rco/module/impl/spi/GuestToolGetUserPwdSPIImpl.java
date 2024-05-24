package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbSyncLoginAccountPermissionEnums;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserPasswordGtInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.UserPasswordMessageEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Description: GT 用户密码获取
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/23
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(GuestToolCmdId.GT_GET_USER_PWD_CMD_ID)
public class GuestToolGetUserPwdSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GuestToolGetUserPwdSPIImpl.class);

    @Autowired
    private UserNotifyAPI userNotifyAPIImpl;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private UserDesktopMgmtAPI desktopMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");

        CbbGuesttoolMessageDTO messageDTO = request.getDto();

        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.GT_GET_USER_PWD_CMD_ID));
        dto.setPortId(messageDTO.getPortId());
        dto.setDeskId(messageDTO.getDeskId());
        UserPasswordGtInfoDTO userPasswordGtInfoDTO = new UserPasswordGtInfoDTO();

        CloudDesktopDetailDTO desktopInfo;
        try {
            desktopInfo = desktopMgmtAPI.getDesktopDetailById(messageDTO.getDeskId());
        } catch (Exception ex) {
            LOGGER.error("桌面id不存在，桌面id: {}", messageDTO.getDeskId());
            userPasswordGtInfoDTO.setCode(UserPasswordMessageEnum.MESSAGE_DESK_NOT_EXIST.getCode());
            userPasswordGtInfoDTO.setMessage(UserPasswordMessageEnum.MESSAGE_DESK_NOT_EXIST.getMessage());
            dto.setBody(JSON.toJSONString(userPasswordGtInfoDTO));
            return dto;
        }

        if (StringUtils.isEmpty(desktopInfo.getUserId())) {
            LOGGER.error("桌面不存在用户信息不存在，桌面id: {}", messageDTO.getDeskId());
            userPasswordGtInfoDTO.setCode(UserPasswordMessageEnum.MESSAGE_USER_NOT_EXIST.getCode());
            userPasswordGtInfoDTO.setMessage(UserPasswordMessageEnum.MESSAGE_USER_NOT_EXIST.getMessage());
            dto.setBody(JSON.toJSONString(userPasswordGtInfoDTO));
            return dto;
        }

        RcoViewUserEntity userInfo = userServiceImpl.getUserInfoById(desktopInfo.getUserId());
        if (userInfo == null) {
            LOGGER.error("用户信息id不存在，用户id: {}", desktopInfo.getUserId());
            userPasswordGtInfoDTO.setCode(UserPasswordMessageEnum.MESSAGE_USER_NOT_EXIST.getCode());
            userPasswordGtInfoDTO.setMessage(UserPasswordMessageEnum.MESSAGE_USER_NOT_EXIST.getMessage());
            dto.setBody(JSON.toJSONString(userPasswordGtInfoDTO));
            return dto;
        }

        userPasswordGtInfoDTO.setCode(UserPasswordMessageEnum.MESSAGE_SUCCESS.getCode());
        userPasswordGtInfoDTO.setMessage(UserPasswordMessageEnum.MESSAGE_SUCCESS.getMessage());
        UserPasswordGtInfoDTO.Content content = new UserPasswordGtInfoDTO.Content();
        content.setPassword(StringUtils.isEmpty(userInfo.getPassword()) ? "" : userInfo.getPassword());
        content.setUserName(userInfo.getUserName());
        content.setChangeUserName(false);
        content.setAutoLogin(false);
        content.setUserGroup(CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue());

        // 仅普通vdi桌面和静态池vdi桌面，需要关注自动登录相关参数
        if (CbbCloudDeskType.VDI.name().equals(desktopInfo.getDeskType())
                && (DesktopPoolType.COMMON.name().equals(desktopInfo.getDesktopPoolType())
                || DesktopPoolType.STATIC.name().equals(desktopInfo.getDesktopPoolType()))) {
            CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(desktopInfo.getDesktopStrategyId());
            content.setChangeUserName(deskStrategy.getDesktopSyncLoginAccount() == null ?
                    Boolean.FALSE : deskStrategy.getDesktopSyncLoginAccount());
            content.setAutoLogin(deskStrategy.getDesktopSyncLoginPassword() == null ?
                    Boolean.FALSE : deskStrategy.getDesktopSyncLoginPassword());
            content.setUserGroup(deskStrategy.getDesktopSyncLoginAccountPermission() == null ?
                    CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue() :
                    deskStrategy.getDesktopSyncLoginAccountPermission().getValue());
        }
        userPasswordGtInfoDTO.setContent(content);
        dto.setBody(JSON.toJSONString(userPasswordGtInfoDTO));
        LOGGER.info("返回的消息体为： {}", dto);
        return dto;
    }

}
