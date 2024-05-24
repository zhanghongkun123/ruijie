package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaMainStrategyWatermarkAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PublicBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.MultiSessionBaseRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月31日
 *
 * @author XiaoJiaXin
 */
@DispatcherImplemetion(Constants.SCREEN_WATERMARK_MESSAGE)
public class DesktopWatermarkSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopWatermarkSPIImpl.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaMainStrategyWatermarkAPI rcaMainStrategyWatermarkAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        final CbbGuesttoolMessageDTO dto = request.getDto();
        LOGGER.info("接收到GT请求屏幕水印的消息：{}", JSON.toJSONString(dto));

        if (handleRcaHostWatermark(request)) {
            return request.getDto();
        }

        CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(dto.getDeskId());

        if (desktopDetailDTO == null) {
            LOGGER.error("桌面[{}]不存在", dto.getDeskId());
            throw new BusinessException(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_NOT_EXIST, String.valueOf(dto.getDeskId()));
        }

        // 判断云桌面状态是否还处于RUNNING或START_UP
        boolean isDeskStateCorrect = isDesktopRunningOrStartUp(desktopDetailDTO.getDesktopState());
        if (!isDeskStateCorrect) {
            LOGGER.error("桌面[{}]未处于启动或运行中状态", dto.getDeskId());
            throw new BusinessException(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_STATE_IS_NOT_RUNNING);
        }

        if (desktopDetailDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            // 多会话要根据传入参数去查询具体用户名称
            return doSendMultiSessionDesk(request, dto, desktopDetailDTO);
        } else {
            return doSendDeskWatermarkConfig(request, desktopDetailDTO);
        }
    }

    private Boolean handleRcaHostWatermark(CbbGuestToolSPIReceiveRequest request) {
        Boolean isHandleSuccess = false;
        CbbGuesttoolMessageDTO dto = request.getDto();
        try {
            RcaHostDTO rcaHostDTO = rcaHostAPI.getById(dto.getDeskId());
            LOGGER.info("获取云应用水印, host id:{}, host name:{}", rcaHostDTO.getId(), rcaHostDTO.getName());
            UUID multiSessionId = null;
            if (dto.getBody().contains("multiSessionId")) {
                GuesttoolMessageContent requestBody = JSON.parseObject(dto.getBody(), GuesttoolMessageContent.class);
                MultiSessionBaseRequest baseRequest = JSON.parseObject(JSON.toJSONString(requestBody.getContent()), MultiSessionBaseRequest.class);
                multiSessionId = baseRequest.getMultiSessionId();
                LOGGER.warn("云应用主机[{}]请求水印配置multiSessionId[{}]", dto.getDeskId(), multiSessionId);
            }

            RcaAppPoolBaseDTO rcaAppPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(rcaHostDTO.getPoolId());
            RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
            relationshipDTO.setStrategyId(rcaAppPoolBaseDTO.getMainStrategyId());
            RcaMainStrategyDTO mainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
            rcaMainStrategyWatermarkAPI.handleNotifyWatermarkConfig(rcaHostDTO, multiSessionId, mainStrategyDTO);
            isHandleSuccess = true;
        } catch (BusinessException e) {
            LOGGER.error("获取云应用水印失败, id : {}", dto.getDeskId(), e);
        }
        return isHandleSuccess;
    }

    private CbbGuesttoolMessageDTO doSendMultiSessionDesk(CbbGuestToolSPIReceiveRequest request, CbbGuesttoolMessageDTO dto,
                                                          CloudDesktopDetailDTO desktopDetailDTO) throws BusinessException {
        GuesttoolMessageContent requestBody = JSON.parseObject(dto.getBody(), GuesttoolMessageContent.class);

        if (Objects.isNull(requestBody) || Objects.isNull(requestBody.getContent())) {
            LOGGER.warn("多会话桌面[{}]请求水印配置未传multiSessionId，参数[{}]", desktopDetailDTO.getId(), JSON.toJSONString(request));
            return doSendDeskWatermarkConfig(request, desktopDetailDTO);
        }
        MultiSessionBaseRequest baseRequest = JSON.parseObject(JSON.toJSONString(requestBody.getContent()), MultiSessionBaseRequest.class);
        if (Objects.isNull(baseRequest.getMultiSessionId())) {
            LOGGER.warn("多会话桌面[{}]请求水印配置未传multiSessionId，参数[{}]", desktopDetailDTO.getId(), JSON.toJSONString(request));
            return doSendDeskWatermarkConfig(request, desktopDetailDTO);
        }
        IacUserDetailDTO userDetailDTO = iacUserMgmtAPI.getUserDetail(baseRequest.getMultiSessionId());
        desktopDetailDTO.setUserName(userDetailDTO.getUserName());
        return doSendDeskWatermarkConfig(request, desktopDetailDTO);
    }

    private CbbGuesttoolMessageDTO doSendDeskWatermarkConfig(CbbGuestToolSPIReceiveRequest request, CloudDesktopDetailDTO desktopDetailDTO) {
        if (PublicBusinessKey.DEFAULT_EMPTY_USERNAME.equals(desktopDetailDTO.getUserName())) {
            // 公用终端显示--太难看了，不显示用户名
            desktopDetailDTO.setUserName("");
        }

        // 云桌面策略开启水印策略就发送云桌面策略的，未开启就发全局策略的
        deskStrategyAPI.sendDesktopStrategyWatermark(desktopDetailDTO);
        return request.getDto();
    }

    private boolean isDesktopRunningOrStartUp(String desktopState) {
        LOGGER.debug("桌面状态为：{} ", desktopState);
        if (desktopState.equals(CbbCloudDeskState.RUNNING.name())) {
            return true;
        }

        return desktopState.equals(CbbCloudDeskState.START_UP.name());
    }
}

