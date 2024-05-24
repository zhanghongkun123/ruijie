package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopConfigService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: shine请求查询用户信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月13日
 *
 * @author brq
 */
@DispatcherImplemetion(ShineAction.GET_USER_INFO)
public class GetUserInfoSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserInfoSPIImpl.class);

    /**
     * 用户名
     */
    private static final String USER_NAME = "userName";

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserDesktopConfigService userDesktopConfigService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        Assert.hasText(request.getData(), "报文data不能为空");

        LOGGER.info("接收到终端获取用户信息请求，请求参数为:[" + request.getData() + "]");
        JSONObject dataJson = JSONObject.parseObject(request.getData());
        String userName = dataJson.getString(USER_NAME);
        UserInfoDTO userInfoDTO;
        try {
            userInfoDTO = buildUserInfoDTO(cbbUserAPI.getUserByName(userName), request);
        } catch (BusinessException e) {
            LOGGER.error("获取用户[" + userName + "]失败", e);
            response(request, CommonMessageCode.CODE_ERR_OTHER, null);
            return;
        }

        if (userInfoDTO == null) {
            LOGGER.error("用户[" + userName + "]不存在，查询用户信息失败");
            response(request, CommonMessageCode.CODE_ERR_OTHER, null);
            return;
        }

        response(request, CommonMessageCode.SUCCESS, userInfoDTO);
    }

    private UserInfoDTO buildUserInfoDTO(IacUserDetailDTO userDetailDTO, CbbDispatcherRequest request) {
        if (userDetailDTO == null) {
            // 用户信息不存在返回null
            return null;
        }
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(userDetailDTO, userInfoDTO);
        userInfoDTO.setName(userDetailDTO.getUserName());
        userInfoDTO.setState(userDetailDTO.getUserState());
        // 获取终端实体
        UserDesktopConfigDTO userDesktopConfigDTO = null;
        CbbTerminalBasicInfoDTO terminal = null;
        try {
            terminal = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            if (terminal.getTerminalPlatform() == CbbTerminalPlatformEnums.IDV) {
                userDesktopConfigDTO = userDesktopConfigService.getUserDesktopConfig(userDetailDTO.getId(), UserCloudDeskTypeEnum.IDV);
            } else if (terminal.getTerminalPlatform() == CbbTerminalPlatformEnums.VOI) {
                userDesktopConfigDTO = userDesktopConfigService.getUserDesktopConfig(userDetailDTO.getId(), UserCloudDeskTypeEnum.VOI);
            }
        } catch (BusinessException e) {
            LOGGER.error("获终端信息失败", e);
            this.response(request, CommonMessageCode.FAIL_CODE, userInfoDTO);
        }
        // 判断用户是否开启IDV|VOI特性  即使没有策略与镜像的配置关系，也是为空返回给shine
        //  由于shine 已确认 目前有使用到两个值，即使为空也会给前端提示，但是没有说明IDV|VOI类型 暂不修改
        if (userDesktopConfigDTO != null) {
            userInfoDTO.setIdvDesktopImageId(userDesktopConfigDTO.getImageTemplateId());
            userInfoDTO.setIdvDesktopStrategyId(userDesktopConfigDTO.getStrategyId());
        }

        return userInfoDTO;

    }

    private void response(CbbDispatcherRequest request, Integer code, UserInfoDTO userInfoDTO) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, userInfoDTO);
        } catch (Exception e) {
            LOGGER.error("终端{}获取用户信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
