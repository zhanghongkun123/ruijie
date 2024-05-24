package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaHostSessionDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.WatermarkDisplayField;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWatermarkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.CbbWatermarkMsgDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.GuestToolWatermarkMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayFieldDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */
@Service
public class WatermarkMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkMessageSender.class);

    /** 下发水印配置信息给guesttool线程池 */
    private static final ExecutorService SEND_RCA_HOST_WATER_MARK_MESSAGE_THREAD_POOL =
            ThreadExecutors.newBuilder("send_rca_host_watermark_message_thread").maxThreadNum(20).queueSize(1).build();

    @Autowired
    private CbbWatermarkMgmtAPI cbbWatermarkMgmtAPI;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private CbbGuestToolMessageAPI cbbGuestToolMessageAPI;

    @Autowired
    private WatermarkMessageParser<WatermarkDisplayContentDTO> watermarkMessageParser;

    /**
     * 组装GT消息，并发送
     *
     * @param cloudDesktopDetailDTO cloudDesktopDetailDTO
     * @throws BusinessException 业务异常
     */
    public void send(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        Assert.notNull(cloudDesktopDetailDTO, "cloudDesktopDetailDTO cannot be null");
        CbbWatermarkConfigDTO watermarkConfigDTO = cbbWatermarkMgmtAPI.getWatermarkConfig();
        if (null == watermarkConfigDTO) {
            throw new BusinessException(BusinessKey.RCDC_RCO_WATERMARK_GET_RUNNING_DESKTOP_NOT_EXIST);
        }
        sendWatermarkMessage(cloudDesktopDetailDTO, watermarkConfigDTO);
    }

    /**
     * 指定水印配置信息和云桌面信息组装GT消息，并发送
     *
     * @param cloudDesktop    cloudDesktopDetailDTO
     * @param watermarkConfig CbbWatermarkConfigDTO
     * @throws BusinessException 业务异常
     */
    public void sendWatermarkMessage(CloudDesktopDetailDTO cloudDesktop, CbbWatermarkConfigDTO watermarkConfig) throws BusinessException {
        Assert.notNull(cloudDesktop, "cloudDesktop cannot be null");
        Assert.notNull(watermarkConfig, "watermarkConfig cannot be null");

        CbbWatermarkDisplayConfigDTO configDTO = watermarkConfig.getDisplayConfig();

        WatermarkFieldMappingValueDTO mappingValue = getMappingValueDTO(cloudDesktop, watermarkConfig.getDisplayContent());
        WatermarkDisplayContentDTO displayContentDTO = watermarkMessageParser.parse(mappingValue, watermarkConfig.getDisplayContent());
        WatermarkDisplayFieldDTO displayFieldDTO = JSON.parseObject(watermarkConfig.getDisplayContent(), WatermarkDisplayFieldDTO.class);
        boolean hasOpenUserName = Arrays.stream(displayFieldDTO.getFieldArr())
                .anyMatch(item -> StringUtils.equals(item, WatermarkDisplayField.USER_NAME.name()));
        // 组装GT消息
        if (cloudDesktop.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            // 多会话 获取每个用户单独发送
            List<HostUserEntity> userEntityList = hostUserService.findByDeskId(cloudDesktop.getId());
            GuestToolWatermarkMessageDTO guestToolMsg;
            CbbWatermarkMsgDTO msgRequest;
            for (HostUserEntity hostUserEntity : userEntityList) {
                guestToolMsg = warpGuestToolMessage(configDTO, displayContentDTO);
                guestToolMsg.setEnable(watermarkConfig.getEnable());
                guestToolMsg.setEnableDarkWatermark(watermarkConfig.getEnableDarkWatermark());
                guestToolMsg.setMultiSessionId(hostUserEntity.getUserId().toString());
                // 有开启显示用户名称时才去查询
                if (hasOpenUserName) {
                    IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(hostUserEntity.getUserId());
                    if (userDetail != null) {
                        guestToolMsg.setUserName(userDetail.getUserName());
                    }
                }
                msgRequest = new CbbWatermarkMsgDTO(cloudDesktop.getId(), JSON.toJSONString(guestToolMsg));
                cbbWatermarkMgmtAPI.sendWatermarkMessagesToGuestTool(msgRequest);
            }
        } else {
            GuestToolWatermarkMessageDTO guestToolMsg = warpGuestToolMessage(configDTO, displayContentDTO);
            guestToolMsg.setEnable(watermarkConfig.getEnable());
            guestToolMsg.setEnableDarkWatermark(watermarkConfig.getEnableDarkWatermark());
            CbbWatermarkMsgDTO msgRequest = new CbbWatermarkMsgDTO(cloudDesktop.getId(), JSON.toJSONString(guestToolMsg));
            cbbWatermarkMgmtAPI.sendWatermarkMessagesToGuestTool(msgRequest);
        }
    }

    /**
     * 指定水印配置信息和云桌面信息组装GT消息，并发送
     *
     * @param rcaHostDesktopDTO    rcaHostDesktopDTO
     * @param multiSessionId    只传单个会话
     * @param watermarkConfig CbbWatermarkConfigDTO
     * @throws BusinessException 业务异常
     */
    public void sendWatermarkMessage(RcaHostDesktopDTO rcaHostDesktopDTO,
                                     @Nullable UUID multiSessionId, @Nullable CbbWatermarkConfigDTO watermarkConfig) throws BusinessException {
        Assert.notNull(rcaHostDesktopDTO, "rcaHostDesktopDTO cannot be null");

        // 没传使用全局配置
        if (Objects.isNull(watermarkConfig)) {
            watermarkConfig = cbbWatermarkMgmtAPI.getWatermarkConfig();
            if (null == watermarkConfig) {
                throw new BusinessException(BusinessKey.RCDC_RCO_GLOBAL_WATERMARK_CONFIG_NOT_EXIST);
            }
        }

        CbbWatermarkDisplayConfigDTO configDTO = watermarkConfig.getDisplayConfig();

        CloudDesktopDetailDTO cloudDesktop = new CloudDesktopDetailDTO();
        BeanUtils.copyProperties(rcaHostDesktopDTO, cloudDesktop);
        cloudDesktop.setId(rcaHostDesktopDTO.getCbbId());
        WatermarkFieldMappingValueDTO mappingValue = getMappingValueDTO(cloudDesktop, watermarkConfig.getDisplayContent());
        WatermarkDisplayContentDTO displayContentDTO = watermarkMessageParser.parse(mappingValue, watermarkConfig.getDisplayContent());
        WatermarkDisplayFieldDTO displayFieldDTO = JSON.parseObject(watermarkConfig.getDisplayContent(), WatermarkDisplayFieldDTO.class);

        boolean hasOpenUserName = Arrays.stream(displayFieldDTO.getFieldArr())
                .anyMatch(item -> StringUtils.equals(item, WatermarkDisplayField.USER_NAME.name()));

        GuestToolWatermarkMessageDTO guestToolMsg = warpGuestToolMessage(configDTO, displayContentDTO);
        guestToolMsg.setEnable(watermarkConfig.getEnable());
        guestToolMsg.setEnableDarkWatermark(watermarkConfig.getEnableDarkWatermark());

        if (RcaEnum.HostSessionType.MULTIPLE == rcaHostDesktopDTO.getRcaHostSessionType()) {
            List<RcaHostSessionDTO> sessionList = rcaHostSessionAPI.listByHostIdIn(Lists.newArrayList(rcaHostDesktopDTO.getRcaHostId()));
            if (Objects.nonNull(multiSessionId)) {
                // 主动请求，单个下发
                boolean isMatchSession = sessionList.stream().anyMatch(rcaHostSessionDTO -> rcaHostSessionDTO.getUserId().equals(multiSessionId));
                if (!isMatchSession) {
                    LOGGER.error("应用主机{}下不存在会话{}", rcaHostDesktopDTO.getId(), multiSessionId);
                    return;
                } else {
                    guestToolMsg.setMultiSessionId(multiSessionId.toString());
                    // 有开启显示用户名称时才去查询
                    if (hasOpenUserName) {
                        UserInfoDTO userInfoDTO = userMgmtAPI.getUserInfoById(multiSessionId);
                        if (userInfoDTO != null) {
                            guestToolMsg.setUserName(userInfoDTO.getUserName());
                        }
                    }
                    CbbWatermarkMsgDTO msgRequest = new CbbWatermarkMsgDTO(cloudDesktop.getId(), JSON.toJSONString(guestToolMsg));
                    LOGGER.info("发送多会话VDI应用主机水印功能rca host deskId:[{}], user id: [{}]",
                            rcaHostDesktopDTO.getCbbId(), multiSessionId);
                    cbbWatermarkMgmtAPI.sendWatermarkMessagesToGuestTool(msgRequest);
                }
            } else {
                for (RcaHostSessionDTO sessionDTO: sessionList) {
                    if (RcaEnum.SessionStatus.ONLINE == sessionDTO.getSessionStatus()) {
                        guestToolMsg.setMultiSessionId(sessionDTO.getUserId().toString());
                        // 有开启显示用户名称时才去查询
                        if (hasOpenUserName) {
                            UserInfoDTO userInfoDTO = userMgmtAPI.getUserInfoById(sessionDTO.getUserId());
                            if (userInfoDTO != null) {
                                guestToolMsg.setUserName(userInfoDTO.getUserName());
                            }
                        }
                        CbbWatermarkMsgDTO msgRequest = new CbbWatermarkMsgDTO(cloudDesktop.getId(), JSON.toJSONString(guestToolMsg));
                        LOGGER.info("发送多会话VDI应用主机水印功能rca host deskId:[{}], session id[{}], user id: [{}]",
                                rcaHostDesktopDTO.getCbbId(), sessionDTO.getId(), sessionDTO.getUserId());
                        cbbWatermarkMgmtAPI.sendWatermarkMessagesToGuestTool(msgRequest);
                    }
                }
            }
        } else {
            CbbWatermarkMsgDTO msgRequest = new CbbWatermarkMsgDTO(cloudDesktop.getId(), JSON.toJSONString(guestToolMsg));
            LOGGER.info("发送单会话VDI应用主机水印功能rca host deskId:[{}]", rcaHostDesktopDTO.getCbbId());
            cbbWatermarkMgmtAPI.sendWatermarkMessagesToGuestTool(msgRequest);
        }
    }

    /**
     * 指定水印配置信息和云桌面信息组装GT消息，并发送
     *
     * @param rcaHostDTO    rcaHostDTO
     * @param multiSessionId    只传单个会话
     * @param watermarkConfig CbbWatermarkConfigDTO
     * @throws BusinessException 业务异常
     */
    public void sendWatermarkMessage(RcaHostDTO rcaHostDTO, @Nullable UUID multiSessionId,
                                     @Nullable CbbWatermarkConfigDTO watermarkConfig) throws BusinessException {
        Assert.notNull(rcaHostDTO, "rcaHostDTO cannot be null");

        // 没传使用全局配置
        if (Objects.isNull(watermarkConfig)) {
            watermarkConfig = cbbWatermarkMgmtAPI.getWatermarkConfig();
            if (null == watermarkConfig) {
                throw new BusinessException(BusinessKey.RCDC_RCO_GLOBAL_WATERMARK_CONFIG_NOT_EXIST);
            }
        }

        CbbWatermarkDisplayConfigDTO configDTO = watermarkConfig.getDisplayConfig();
        WatermarkFieldMappingValueDTO mappingValue = getMappingValueDTO(rcaHostDTO, watermarkConfig.getDisplayContent());
        WatermarkDisplayContentDTO displayContentDTO = watermarkMessageParser.parse(mappingValue, watermarkConfig.getDisplayContent());
        WatermarkDisplayFieldDTO displayFieldDTO = JSON.parseObject(watermarkConfig.getDisplayContent(), WatermarkDisplayFieldDTO.class);

        boolean hasOpenUserName = Arrays.stream(displayFieldDTO.getFieldArr())
                .anyMatch(item -> StringUtils.equals(item, WatermarkDisplayField.USER_NAME.name()));

        GuestToolWatermarkMessageDTO guestToolMsg = warpGuestToolMessage(configDTO, displayContentDTO);
        guestToolMsg.setEnable(watermarkConfig.getEnable());
        guestToolMsg.setEnableDarkWatermark(watermarkConfig.getEnableDarkWatermark());

        List<RcaHostSessionDTO> sessionList = rcaHostSessionAPI.listByHostIdIn(Lists.newArrayList(rcaHostDTO.getId()));
        if (RcaEnum.HostSessionType.MULTIPLE == rcaHostDTO.getHostSessionType()) {
            if (Objects.nonNull(multiSessionId)) {
                // 主动请求，单个下发
                boolean isMatchSession = sessionList.stream().anyMatch(rcaHostSessionDTO -> rcaHostSessionDTO.getUserId().equals(multiSessionId));
                if (!isMatchSession) {
                    LOGGER.error("应用主机{}下不存在会话{}", rcaHostDTO.getId(), multiSessionId);
                    return;
                } else {
                    guestToolMsg.setMultiSessionId(multiSessionId.toString());
                    // 有开启显示用户名称时才去查询
                    if (hasOpenUserName) {
                        UserInfoDTO userInfoDTO = userMgmtAPI.getUserInfoById(multiSessionId);
                        if (userInfoDTO != null) {
                            guestToolMsg.setUserName(userInfoDTO.getUserName());
                        }
                    }
                    CbbWatermarkMsgDTO msgRequest = new CbbWatermarkMsgDTO(rcaHostDTO.getId(), JSON.toJSONString(guestToolMsg));
                    LOGGER.info("发送多会话第三方应用主机水印功能rca host deskId:[{}], user id: [{}]",
                            rcaHostDTO.getId(), multiSessionId);
                    sendRcaHostWaterMarkConfig(msgRequest);
                }
            } else {
                for (RcaHostSessionDTO sessionDTO: sessionList) {
                    CbbWatermarkMsgDTO msgRequest;
                    if (RcaEnum.SessionStatus.ONLINE == sessionDTO.getSessionStatus()) {
                        guestToolMsg.setMultiSessionId(sessionDTO.getUserId().toString());
                        // 有开启显示用户名称时才去查询
                        if (hasOpenUserName) {
                            UserInfoDTO userInfoDTO = userMgmtAPI.getUserInfoById(sessionDTO.getUserId());
                            if (userInfoDTO != null) {
                                guestToolMsg.setUserName(userInfoDTO.getUserName());
                            }
                        }
                        msgRequest = new CbbWatermarkMsgDTO(rcaHostDTO.getId(), JSON.toJSONString(guestToolMsg));
                        LOGGER.info("发送多会话第三方应用主机水印功能rca host:[{}], session id[{}], user id: [{}]",
                                rcaHostDTO.getId(), sessionDTO.getId(), sessionDTO.getUserId());
                        sendRcaHostWaterMarkConfig(msgRequest);
                    }
                }
            }
        } else {
            if (sessionList.size() > 0) {
                if (hasOpenUserName && sessionList.get(0) != null) {
                    UserInfoDTO userInfoDTO = userMgmtAPI.getUserInfoById(sessionList.get(0).getUserId());
                    if (userInfoDTO != null) {
                        guestToolMsg.setUserName(userInfoDTO.getUserName());
                    }
                }
            }
            CbbWatermarkMsgDTO msgRequest = new CbbWatermarkMsgDTO(rcaHostDTO.getId(), JSON.toJSONString(guestToolMsg));
            LOGGER.info("发送单会话第三方应用主机水印功能rca host:[{}]", rcaHostDTO.getId());
            sendRcaHostWaterMarkConfig(msgRequest);
        }
    }

    private void sendRcaHostWaterMarkConfig(CbbWatermarkMsgDTO msgRequest) {
        CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO = cbbWatermarkMgmtAPI.generateGuesttoolMessageDTO(msgRequest);
        SEND_RCA_HOST_WATER_MARK_MESSAGE_THREAD_POOL.execute(() -> {
            try {
                cbbGuestToolMessageAPI.asyncRequest(cbbGuesttoolMessageDTO);
            } catch (BusinessException e) {
                LOGGER.error("send rca host agent watermark config message to guesttool has error", e);
            }
        });
    }

    private WatermarkFieldMappingValueDTO getMappingValueDTO(CloudDesktopDetailDTO cloudDesktopDetailDTO, String displayContent) {
        WatermarkFieldMappingValueDTO mappingValue = new WatermarkFieldMappingValueDTO();
        WatermarkDisplayFieldDTO displayFieldDTO = JSON.parseObject(displayContent, WatermarkDisplayFieldDTO.class);
        mappingValue.setDeskIp(cloudDesktopDetailDTO.getDesktopIp());
        mappingValue.setUserName(cloudDesktopDetailDTO.getUserName());
        mappingValue.setDeskName(cloudDesktopDetailDTO.getDesktopName());
        mappingValue.setDeskMac(cloudDesktopDetailDTO.getDesktopMac());
        mappingValue.setCustomContent(displayFieldDTO.getCustomContent());

        return mappingValue;
    }

    private WatermarkFieldMappingValueDTO getMappingValueDTO(RcaHostDTO rcaHostDTO, String displayContent) {
        WatermarkFieldMappingValueDTO mappingValue = new WatermarkFieldMappingValueDTO();
        WatermarkDisplayFieldDTO displayFieldDTO = JSON.parseObject(displayContent, WatermarkDisplayFieldDTO.class);
        mappingValue.setDeskIp(rcaHostDTO.getIp());
        mappingValue.setUserName(rcaHostDTO.getName());
        mappingValue.setDeskName(rcaHostDTO.getName());
        mappingValue.setDeskMac(rcaHostDTO.getMacAddress());
        mappingValue.setCustomContent(displayFieldDTO.getCustomContent());

        return mappingValue;
    }

    private GuestToolWatermarkMessageDTO warpGuestToolMessage(CbbWatermarkDisplayConfigDTO config, WatermarkDisplayContentDTO displayContent) {
        GuestToolWatermarkMessageDTO message = new GuestToolWatermarkMessageDTO();
        BeanUtils.copyProperties(config, message);
        message.setDeskName(displayContent.getDeskName());
        message.setDeskIp(displayContent.getDeskIp());
        message.setDeskMac(displayContent.getDeskMac());

        message.setUserName(displayContent.getUserName());
        message.setCustomContent(displayContent.getCustomContent());
        return message;
    }
}
