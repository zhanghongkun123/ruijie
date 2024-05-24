package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalSimplifyDeploymentConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalSimplifyDeploymentConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalSimplifyDeploymentUserDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.response.TerminalSimplifyDeploymentConfigResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 获取 终端极简部署 策略SPI
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/5 10:03
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.SYS_TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG)
public class GetTerminalSimplifyDeploymentSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTerminalSimplifyDeploymentSPIImpl.class);

    @Autowired
    private TerminalSimplifyDeploymentConfigAPI terminalSimplifyDeploymentConfigAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request must not null");
        TerminalSimplifyDeploymentConfigResponse simplifyDeploymentConfigResponse = new TerminalSimplifyDeploymentConfigResponse();
        //默认关闭极简模式
        simplifyDeploymentConfigResponse.setEnableTerminalSimplifyDeployment(Boolean.FALSE);
        try {
            // 查询策略
            TerminalSimplifyDeploymentConfigDTO terminalSimplifyDeploymentConfig =
                    terminalSimplifyDeploymentConfigAPI.getTerminalSimplifyDeploymentConfig();
            //设置极简模式开关
            simplifyDeploymentConfigResponse
                    .setEnableTerminalSimplifyDeployment(terminalSimplifyDeploymentConfig.getEnableTerminalSimplifyDeployment());
            // 开启终端极简模式开关
            if (Boolean.TRUE.equals(terminalSimplifyDeploymentConfig.getEnableTerminalSimplifyDeployment())) {
                // 查询用户信息
                TerminalSimplifyDeploymentUserDTO userInfo = JSON.parseObject(request.getData(), TerminalSimplifyDeploymentUserDTO.class);
                if (userInfo != null && StringUtils.isNotBlank(userInfo.getUserName())) {
                    IacUserDetailDTO cbbUserDetailDTO = userAPI.getUserByName(userInfo.getUserName());
                    // 用户信息不为空 ，进行查询
                    if (cbbUserDetailDTO != null) {
                        // 查询到 就设置用户信息
                        simplifyDeploymentConfigResponse.setUserName(cbbUserDetailDTO.getUserName());
                        // 构造 IDV桌面配置
                        buildIDVConfigDTO(simplifyDeploymentConfigResponse, cbbUserDetailDTO);
                        // 构造VOI桌面配置
                        buildVOIConfigDTO(simplifyDeploymentConfigResponse, cbbUserDetailDTO);
                    }
                }
            }
            // 返回给SHINE
            responseMessage(request, CommonMessageCode.SUCCESS, simplifyDeploymentConfigResponse);
        } catch (Exception e) {
            responseMessage(request, CommonMessageCode.CODE_ERR_OTHER, null);
            LOGGER.error("获取终端极简部署失败，失败原因：{}", e);
        }
    }

    private void responseMessage(CbbDispatcherRequest request, int code, TerminalSimplifyDeploymentConfigResponse response) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("响应终端[{}]终端极简部署为[{}]", request.getTerminalId(), JSON.toJSONString(response));
            }
            shineMessageHandler.responseContent(request, code, response);
        } catch (Exception e) {
            LOGGER.error(String.format("响应终端[{%s}]终端极简部署信息失败，失败原因：", request.getTerminalId()), e);
        }
    }


    /**
     * 构造VOI 云桌面配置
     *
     * @param detailWebResponse
     * @param cbbUserDetailDTO
     * @throws BusinessException
     */
    private void buildIDVConfigDTO(TerminalSimplifyDeploymentConfigResponse detailWebResponse, IacUserDetailDTO cbbUserDetailDTO) {
        //IDV 桌面配置不为空
        UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigAPI.getUserDesktopConfig(cbbUserDetailDTO.getId(), UserCloudDeskTypeEnum.IDV);
        if (userDesktopConfigDTO != null && null != userDesktopConfigDTO.getImageTemplateId() && null != userDesktopConfigDTO.getStrategyId()) {
            detailWebResponse.setIdvImageTemplateId(userDesktopConfigDTO.getImageTemplateId());
        }

    }

    /**
     * 构造VOI 云桌面配置
     *
     * @param detailWebResponse
     * @param cbbUserDetailDTO
     * @throws BusinessException
     */
    private void buildVOIConfigDTO(TerminalSimplifyDeploymentConfigResponse detailWebResponse, IacUserDetailDTO cbbUserDetailDTO) {
        //VOI 桌面配置不为空
        UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigAPI.getUserDesktopConfig(cbbUserDetailDTO.getId(), UserCloudDeskTypeEnum.VOI);
        if (userDesktopConfigDTO != null && null != userDesktopConfigDTO.getImageTemplateId() && null != userDesktopConfigDTO.getStrategyId()) {
            detailWebResponse.setVoiImageTemplateId(userDesktopConfigDTO.getImageTemplateId());
        }

    }
}
