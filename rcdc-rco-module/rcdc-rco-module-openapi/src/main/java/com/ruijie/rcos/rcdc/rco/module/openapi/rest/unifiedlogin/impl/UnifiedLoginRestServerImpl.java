package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserUnifiedLoginAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.QueryDesktopItemDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedChangePwdResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedLoginResultDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.UnifiedLoginRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request.UnifiedChangePwdRestRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request.UnifiedLoginRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request.UserDesktopQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response.UnifiedChangePwdRestResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response.UnifiedLoginRestServerResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response.UserDesktopQueryResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalVersionAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;

/**
 *
 * Description: rcdc统一登录openApi
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
@Service
public class UnifiedLoginRestServerImpl implements UnifiedLoginRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiedLoginRestServerImpl.class);

    private static final int CLUSTER_VALIDATE_FAIL = -500;

    private static final List<String> OS_TYPE_LIST = Arrays.asList(CbbTerminalTypeEnums.APP_ANDROID.getOsType(),
            CbbTerminalTypeEnums.APP_MACOS.getOsType(), CbbTerminalTypeEnums.APP_IOS.getOsType());

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserUnifiedLoginAPI userUnifiedLoginAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbTerminalVersionAPI cbbTerminalVersionAPI;

    @Override
    public UnifiedLoginRestServerResponse unifiedUserLoginValidate(UnifiedLoginRestServerRequest request) {
        Assert.notNull(request, "request must not be null here!");
        LOGGER.info("统一登录openApi，request: [{}]", JSON.toJSONString(request));

        Boolean isValidatePass = userUnifiedLoginAPI.validateUnifiedLoginAuth(request.getClusterId());
        if (Boolean.FALSE.equals(isValidatePass)) {
            LOGGER.error("rcdc统一登录节点信息校验不通过， request：{}", JSON.toJSONString(request));
            return new UnifiedLoginRestServerResponse(CLUSTER_VALIDATE_FAIL);
        }

        try {
            CbbDispatcherRequest dispatcherRequest = new CbbDispatcherRequest();
            BeanUtils.copyProperties(request.getDispatcherRequest(), dispatcherRequest);
            dispatcherRequest.setNewConnection(request.getDispatcherRequest().getIsNewConnection());

            UnifiedLoginResultDTO loginResultDTO = userUnifiedLoginAPI.unifiedUserLoginValidate(dispatcherRequest);

            UnifiedLoginRestServerResponse response = new UnifiedLoginRestServerResponse();
            BeanUtils.copyProperties(loginResultDTO, response);
            return response;
        } catch (Exception e) {
            LOGGER.error(String.format("rcdc统一登录openAPI处理异常， request：%s", JSON.toJSONString(request)), e);
            return new UnifiedLoginRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public UnifiedLoginRestServerResponse collectUserLoginValidate(UnifiedLoginRestServerRequest request) {
        Assert.notNull(request, "request must not be null here!");
        LOGGER.info("统一登录openApi，request: [{}]", JSON.toJSONString(request));

        Boolean isValidatePass = userUnifiedLoginAPI.validateUnifiedLoginAuth(request.getClusterId());
        if (Boolean.FALSE.equals(isValidatePass)) {
            LOGGER.error("rcdc统一登录节点信息校验不通过， request：{}", JSON.toJSONString(request));
            return new UnifiedLoginRestServerResponse(CLUSTER_VALIDATE_FAIL);
        }

        try {
            CbbDispatcherRequest dispatcherRequest = new CbbDispatcherRequest();
            BeanUtils.copyProperties(request.getDispatcherRequest(), dispatcherRequest);
            dispatcherRequest.setNewConnection(request.getDispatcherRequest().getIsNewConnection());

            UnifiedLoginResultDTO loginResultDTO = userUnifiedLoginAPI.collectUserLoginValidate(dispatcherRequest);

            UnifiedLoginRestServerResponse response = new UnifiedLoginRestServerResponse();
            BeanUtils.copyProperties(loginResultDTO, response);
            return response;
        } catch (Exception e) {
            LOGGER.error(String.format("rcdc统一登录openAPI处理异常， request：%s", JSON.toJSONString(request)), e);
            return new UnifiedLoginRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public UserDesktopQueryResponse listUserDesktop(UserDesktopQueryRequest request) {
        Assert.notNull(request, "request must not be null here!");
        Assert.hasText(request.getUserName(), "request.userName must not be null here!");
        Assert.hasText(request.getTerminalId(), "request.terminalId must not be null here!");

        // 1.判断是否开启了统一登录，clusterId是否一致
        UserDesktopQueryResponse response = new UserDesktopQueryResponse();
        // 2.获取客户端版本信息
        response.setClusterVersion(OS_TYPE_LIST.contains(request.getTerminalOsType()) ? "" : getClientVersion(request));
        Boolean isValidatePass = userUnifiedLoginAPI.validateUnifiedLoginAuth(request.getClusterId());
        if (Boolean.FALSE.equals(isValidatePass)) {
            LOGGER.error("rcdc统一登录获取云桌面，节点信息校验不通过， request：{}", JSON.toJSONString(request));
            response.setResultCode(CLUSTER_VALIDATE_FAIL);
            return response;
        }

        // 3.获取云桌面列表
        IacUserDetailDTO cbbUserInfoDTO = null;
        try {
            cbbUserInfoDTO = cbbUserAPI.getUserByName(request.getUserName());
        } catch (BusinessException e) {
            LOGGER.error("获取用户[" + request.getUserName() + "]失败,查询桌面列表失败",e);
            response.setResultCode(CommonMessageCode.CODE_ERR_OTHER);
            response.setDesktopList(Collections.emptyList());
            return response;
        }
        if (cbbUserInfoDTO == null) {
            LOGGER.error("用户[" + request.getUserName() + "]不存在,查询桌面列表失败");
            response.setResultCode(CommonMessageCode.CODE_ERR_OTHER);
            response.setDesktopList(Collections.emptyList());
            return response;
        }

        // 查询用户绑定的云桌面（不包括回收站中的桌面）
        List<CloudDesktopDTO> userDesktopList = userDesktopMgmtAPI.listUserVDIDesktop(cbbUserInfoDTO, request.getTerminalId());
        if (!CollectionUitls.isEmpty(userDesktopList)) {
            response.setDesktopList(userDesktopList.stream().map(QueryDesktopItemDTO::convertToItem).collect(Collectors.toList()));
        } else {
            response.setDesktopList(Collections.emptyList());
        }

        response.setResultCode(CommonMessageCode.SUCCESS);
        return response;
    }

    @Override
    public UnifiedChangePwdRestResponse unifiedChangePwdValidate(UnifiedChangePwdRestRequest request) {
        Assert.notNull(request, "request must not be null here!");
        LOGGER.info("统一修改密码openApi，request: [{}]", JSON.toJSONString(request));

        Boolean isValidatePass = userUnifiedLoginAPI.validateUnifiedLoginAuth(request.getClusterId());
        if (Boolean.FALSE.equals(isValidatePass)) {
            LOGGER.error("rcdc统一登录修改密码节点信息校验不通过， request：[{}]", JSON.toJSONString(request));
            return new UnifiedChangePwdRestResponse(CLUSTER_VALIDATE_FAIL);
        }

        try {
            CbbDispatcherRequest dispatcherRequest = new CbbDispatcherRequest();
            BeanUtils.copyProperties(request.getDispatcherRequest(), dispatcherRequest);
            dispatcherRequest.setNewConnection(request.getDispatcherRequest().getIsNewConnection());

            UnifiedChangePwdResultDTO dto = userUnifiedLoginAPI.unifiedChangePwdValidate(dispatcherRequest);

            UnifiedChangePwdRestResponse response = new UnifiedChangePwdRestResponse();
            BeanUtils.copyProperties(dto, response);

            return response;
        } catch (Exception e) {
            LOGGER.error("rcdc统一登录openAPI处理异常， request：[{}]", JSON.toJSONString(request), e);
            return new UnifiedChangePwdRestResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    private String getClientVersion(UserDesktopQueryRequest request) {
        String version = "";
        if (StringUtils.isEmpty(request.getCpuArch()) || StringUtils.isEmpty(request.getTerminalOsType())) {
            LOGGER.warn("终端信息不完整获取版本信息失败, request[{}]", JSON.toJSONString(request));
            return version;
        }

        CbbCpuArchType cbbCpuArchType = CbbCpuArchType.convert(request.getCpuArch());
        version = cbbTerminalVersionAPI.getTerminalVersion(cbbCpuArchType, request.getTerminalOsType());
        LOGGER.info("终端[{}]获取版本信息为[{}]", request.getTerminalId(), version);
        return version;
    }
}
