package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.io.File;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AuthChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeletedUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RepeatStartVmWebclientNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ThemeConfigInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ThemeFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.WebclientNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.WebclientNotifyAction;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.WebClientProducerAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalBackgroundAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogoAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBackgroundImageInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalLogoInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Webclient通知 API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/23 11:08
 *
 * @author lihengjing
 */
public class WebclientNotifyAPIImpl implements WebclientNotifyAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebclientNotifyAPIImpl.class);

    @Autowired
    private WebClientProducerAPI webClientProducerAPI;

    @Autowired
    private CbbTerminalLogoAPI cbbTerminalLogoAPI;

    @Autowired
    private CbbTerminalBackgroundAPI cbbTerminalBackgroundAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Override
    public void notifyThemeChange() {
        ThemeConfigInfoDTO themeConfigInfoDTO = null;
        try {
            themeConfigInfoDTO = getThemeConfigInfoDTO();
            LOGGER.info("通知webclient主题策略变更为：{}", themeConfigInfoDTO);
            webClientProducerAPI.notifyThemeInfo(themeConfigInfoDTO);
        } catch (Exception e) {
            LOGGER.error("通知webclient主题策略变更发生异常, themeConfigInfoDTO={}", JSONObject.toJSONString(themeConfigInfoDTO), e);
        }
    }

    private ThemeConfigInfoDTO getThemeConfigInfoDTO() throws BusinessException {
        ThemeConfigInfoDTO themeConfigInfoDTO = new ThemeConfigInfoDTO();
        CbbTerminalLogoInfoDTO cbbLogoInfo = cbbTerminalLogoAPI.getLogoInfo();
        ThemeFileInfoDTO logoInfo = new ThemeFileInfoDTO();
        if (StringUtils.isEmpty(cbbLogoInfo.getLogoPath())) {
            logoInfo.setIsDefault(true);
        } else {
            logoInfo.setImagePath(cbbLogoInfo.getLogoPath());
            logoInfo.setImageName(new File(cbbLogoInfo.getLogoPath()).getName());
            logoInfo.setMd5(cbbLogoInfo.getMd5());
            logoInfo.setIsDefault(false);
        }
        themeConfigInfoDTO.setLogoInfo(logoInfo);
        CbbTerminalBackgroundImageInfoDTO cbbBackgroundImageInfo = cbbTerminalBackgroundAPI.getBackgroundImageInfo();
        ThemeFileInfoDTO backgroundInfo = new ThemeFileInfoDTO();
        if (StringUtils.isEmpty(cbbBackgroundImageInfo.getImagePath())) {
            backgroundInfo.setIsDefault(true);
        } else {
            BeanUtils.copyProperties(cbbBackgroundImageInfo, backgroundInfo);
            backgroundInfo.setIsDefault(false);
        }
        themeConfigInfoDTO.setBackgroundInfo(backgroundInfo);
        return themeConfigInfoDTO;
    }

    @Override
    public void notifyRemoteAssistState(Boolean isLocalRequest, RemoteAssistStateDTO remoteAssistStateDTO) {
        Assert.notNull(isLocalRequest, "isLocalRequest is null.");
        Assert.notNull(remoteAssistStateDTO, "remoteAssistStateDTO is null.");
        try {
            if (BooleanUtils.isTrue(isLocalRequest) && rccmManageService.isUnifiedLogin()) {
                // 本地发起请求、并且统一登录可用 走RCenter进行所有集群分发
                broadcastWebclientNotify(WebclientNotifyAction.NOTIFY_REMOTE_ASSIST, (JSONObject) JSON.toJSON(remoteAssistStateDTO));
            } else {
                // 非本地发起请求 或者 统一登录不可用时， 走本地通知webclient
                // 可能会收到其他集群的云桌面 消息（因为此时跨集群访问桌面了）， 不要校验云桌面ID是否属于自己
                webClientProducerAPI.notifyRemoteAssistanceInfo(remoteAssistStateDTO);
            }
        } catch (Exception e) {
            LOGGER.error("通知webclient远程协助状态变更发生异常，isLocalRequest={}, remoteAssistStateDTO={}", isLocalRequest,
                    JSONObject.toJSONString(remoteAssistStateDTO), e);
        }

    }

    @Override
    public void notifyTerminalDesktopIsRobbed(Boolean isLocalRequest, RepeatStartVmWebclientNotifyDTO repeatStartVmWebclientNotifyDTO) {
        Assert.notNull(isLocalRequest, "isLocalRequest is null.");
        Assert.notNull(repeatStartVmWebclientNotifyDTO, "repeatStartVmWebclientNotifyDTO is null.");
        try {
            if (BooleanUtils.isTrue(isLocalRequest) && rccmManageService.isUnifiedLogin()) {
                // 本地发起请求、并且统一登录可用 走RCenter进行所有集群分发
                broadcastWebclientNotify(WebclientNotifyAction.NOTIFY_TERMINAL_DESKTOP_IS_ROBBED,
                        (JSONObject) JSON.toJSON(repeatStartVmWebclientNotifyDTO));
            } else {
                // 非本地发起请求 或者 统一登录不可用时， 走本地通知webclient
                // 可能会收到其他集群的云桌面 消息（因为此时跨集群访问桌面了）， 不要校验云桌面ID是否属于自己
                webClientProducerAPI.notifyTerminalDesktopIsRobbed(repeatStartVmWebclientNotifyDTO);
            }
        } catch (Exception e) {
            LOGGER.error("通知webclient通知桌面被抢占发生异常，isLocalRequest={}, repeatStartVmWebclientNotifyDTO={}", isLocalRequest,
                    JSONObject.toJSONString(repeatStartVmWebclientNotifyDTO), e);
        }
    }

    @Override
    public void notifyUserDeleted(Boolean isLocalRequest, DeletedUserInfoDTO deletedUserInfoDTO) {
        Assert.notNull(isLocalRequest, "isLocalRequest is null.");
        Assert.notNull(deletedUserInfoDTO, "userInfoDTO is null.");
        try {
            if (BooleanUtils.isTrue(isLocalRequest) && rccmManageService.isUnifiedLogin()) {
                // 本地发起请求、并且统一登录可用 走RCenter进行所有集群分发
                // 等删除用户通知RCenter时，RCenter会做相应的回调
                broadcastWebclientNotify(WebclientNotifyAction.NOTIFY_USER_IS_DELETED, (JSONObject) JSON.toJSON(deletedUserInfoDTO));
            } else {
                // 非本地发起请求 或者 统一登录不可用时， 走本地通知webclient
                webClientProducerAPI.notifyUserDeletedMessage(deletedUserInfoDTO);
            }
        } catch (Exception e) {
            LOGGER.error("通知webclient通知用户已经被删除异常，isLocalRequest={}, deletedUserInfoDTO={}", isLocalRequest,
                    JSONObject.toJSONString(deletedUserInfoDTO), e);
        }
    }

    @Override
    public void notifyRefreshLoginPageInfo(AuthChangeDTO iacAuthTypeDTO) {
        Assert.notNull(iacAuthTypeDTO, "iacAuthTypeDTO is null.");

        try {
            webClientProducerAPI.notifyRefreshLoginPageInfo(iacAuthTypeDTO);
        } catch (Exception e) {
            LOGGER.error("通知webclient登录认证方式变更异常，loginPageInfoResultDTO={}",
                    JSONObject.toJSONString(iacAuthTypeDTO), e);
        }
    }

    private void broadcastWebclientNotify(String action, JSONObject requestBody) {
        WebclientNotifyRequest webclientNotifyRequest = new WebclientNotifyRequest();
        webclientNotifyRequest.setRequestBody(requestBody);
        webclientNotifyRequest.setAction(action);
        LOGGER.info("开始向RCenter推送跨集群通知:[{}]", JSON.toJSONString(webclientNotifyRequest));
        rccmManageAPI.broadcastWebclientNotify(webclientNotifyRequest);
    }


}
