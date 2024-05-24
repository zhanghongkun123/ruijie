package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedUserDesktopResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.RccmRestKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.RequestParamDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ChangeUserPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.UserLoginBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.*;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.RestUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.RemoteResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 *
 * Description: 用户登录多集群相关操作
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
@Service
public class UserLoginRccmOperationServiceImpl implements UserLoginRccmOperationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginRccmOperationServiceImpl.class);

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public boolean isUnifiedLoginOn(String terminalId) {
        Assert.hasText(terminalId, "terminalId不能为空");

        if (!rccmManageService.isUnifiedLogin()) {
            return false;
        }

        // 判断是否是软终端和VDI
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            CbbTerminalPlatformEnums platformEnums = terminalBasicInfoDTO.getTerminalPlatform();
            return CbbTerminalPlatformEnums.APP == platformEnums || CbbTerminalPlatformEnums.VDI == platformEnums;
        } catch (Exception e) {
            LOGGER.error(String.format(" 获取终端信息失败, terminalId[%s]", terminalId), e);
            return false;
        }
    }

    @Override
    public RccmUnifiedLoginResultDTO requestLoginValidateInRccm(CbbDispatcherRequest dispatcherRequest, String terminalId,
                                                            ShineLoginDTO shineLoginDTO) throws BusinessException {
        Assert.notNull(dispatcherRequest, "dispatcherRequest must not be null or empty");
        Assert.hasText(terminalId, "terminalId must not be null or empty");
        Assert.notNull(shineLoginDTO, "shineLoginDTO must not be null");
        LOGGER.info("向rccm发起登录验证, terminalId:{}; shineLoginDTO:{}", terminalId, JSON.toJSONString(shineLoginDTO));

        // 获取纳管信息
        RccmServerConfigDTO serverConfigDTO = rccmManageService.getRccmServerConfig();
        Assert.notNull(serverConfigDTO, "RCCM服务器配置信息RccmServerConfigDTO不能为null");

        // 构造请求参数
        RccmUnifiedLoginRequest request = new RccmUnifiedLoginRequest();
        BeanUtils.copyProperties(shineLoginDTO, request);

        // 本机在集群管理中心的节点ID
        request.setClusterId(serverConfigDTO.getClusterId());
        request.setRequestTime(new Date());
        request.setTerminalId(terminalId);
        request.setUserName(shineLoginDTO.getUserName());
        request.setDispatcherRequest(dispatcherRequest);

        // 构建参数
        RequestParamDTO<RccmUnifiedLoginRequest> requestParam =
                buildRequestParam(request, serverConfigDTO, RccmRestKey.USER_UNIFIED_LOGIN_PATH);
        // 请求r-center校验登录信息
        RccmUnifiedLoginResultDTO rccmUnifiedLoginResult = requestRccm(requestParam, RccmUnifiedLoginResultDTO.class);

        //当前所在集群不正常,shine直接走单集群
        if (CommonMessageCode.CLUSTER_NO_NORMAL == rccmUnifiedLoginResult.getAuthResultCode()) {
            throw new BusinessException(BusinessKey.CLUSTER_NO_EXISTS);
        }

        return rccmUnifiedLoginResult;
    }

    @Override
    public List<UnifiedUserDesktopResultDTO> requestUserVDIDesktopInRccm(String terminalId, Boolean canFoundTerminal) throws Exception {
        Assert.hasText(terminalId, "Param [terminalId] must not be null or empty");
        Assert.notNull(canFoundTerminal, "Param [canFoundTerminal] must not be null or empty");

        // 获取纳管信息
        RccmServerConfigDTO serverConfigDTO = rccmManageService.getRccmServerConfig();
        Assert.notNull(serverConfigDTO, "RCCM服务器配置信息RccmServerConfigDTO不能为null");

        CbbTerminalBasicInfoDTO terminalBasicInfoDTO;
        if (BooleanUtils.isTrue(canFoundTerminal)) {
            terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } else {
            // 网页版客户端terminalId是使用sessionID替代的（并未实现终端上报），终端表无对应数据
            terminalBasicInfoDTO = new CbbTerminalBasicInfoDTO();
            terminalBasicInfoDTO.setTerminalId(terminalId);
            terminalBasicInfoDTO.setCpuArch(CbbCpuArchType.X86_64);
            terminalBasicInfoDTO.setTerminalOsType(CbbTerminalTypeEnums.APP_WINDOWS.getOsType());
        }

        RccmUserDesktopRequestDTO rccmRequest = new RccmUserDesktopRequestDTO();
        rccmRequest.setClusterId(serverConfigDTO.getClusterId());
        rccmRequest.setRequestTime(new Date());
        rccmRequest.setTerminalId(terminalId);
        rccmRequest.setTerminalOsType(terminalBasicInfoDTO.getTerminalOsType());
        rccmRequest.setCpuArch(terminalBasicInfoDTO.getCpuArch().getArchName());

        // 构建参数
        RequestParamDTO<RccmUserDesktopRequestDTO> requestParam =
                buildRequestParam(rccmRequest, serverConfigDTO, RccmRestKey.USER_QUERY_USER_VDI_PATH);
        // 向r-center发起获取VDI列表请求
        RccmUserDesktopResultDTO rccmResultDTO = requestRccm(requestParam, RccmUserDesktopResultDTO.class);

        // 接口内部异常
        if (CommonMessageCode.CODE_ERR_OTHER == rccmResultDTO.getResultCode()) {
            throw new BusinessException(UserLoginBusinessKey.REQUEST_RCCM_USER_DESKTOP_ERROR, terminalId);
        }
        // 用户终端登录失效
        if (CommonMessageCode.NO_USER_LOGIN_CACHE == rccmResultDTO.getResultCode()) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCCM_NO_USER_LOGIN_CACHE, terminalId);
        }

        return rccmResultDTO.getDesktopList();
    }

    @Override
    public RccmUnifiedChangePwdResultDTO requestChangePwdInRccm(CbbDispatcherRequest request, String terminalId,
                                                                    ChangeUserPasswordDTO changeUserPasswordDTO) throws BusinessException {
        Assert.notNull(request, "request must not be null or empty");
        Assert.hasText(terminalId, "terminalId must not be null or empty");
        Assert.notNull(changeUserPasswordDTO, "changeUserPasswordDTO must not be null");
        LOGGER.info("向rccm发起修改密码验证, terminalId:[{}]; changeUserPasswordDTO:[{}]", terminalId, JSON.toJSONString(changeUserPasswordDTO));

        // 获取纳管信息
        RccmServerConfigDTO serverConfigDTO = rccmManageService.getRccmServerConfig();
        Assert.notNull(serverConfigDTO, "RCCM服务器配置信息RccmServerConfigDTO不能为null");

        // 构造请求参数
        RccmUnifiedChangePwdRequest rccmRequest = new RccmUnifiedChangePwdRequest();
        BeanUtils.copyProperties(changeUserPasswordDTO, rccmRequest);

        // 本机在集群管理中心的节点ID
        rccmRequest.setClusterId(serverConfigDTO.getClusterId());
        rccmRequest.setRequestTime(new Date());
        rccmRequest.setTerminalId(terminalId);
        rccmRequest.setUserName(changeUserPasswordDTO.getUserName());
        rccmRequest.setDispatcherRequest(request);

        // 构建参数
        RequestParamDTO<RccmUnifiedChangePwdRequest> requestParam =
                buildRequestParam(rccmRequest, serverConfigDTO, RccmRestKey.USER_UNIFIED_CHANGE_PWD_PATH);
        // 请求r-center校验登录信息
        RccmUnifiedChangePwdResultDTO rccmResultDTO = requestRccm(requestParam, RccmUnifiedChangePwdResultDTO.class);

        return rccmResultDTO;
    }

    private <T> RequestParamDTO<T> buildRequestParam(T requestBody, RccmServerConfigDTO serverConfigDTO, String path) {
        RequestParamDTO<T> requestParamDTO = new RequestParamDTO<>();
        requestParamDTO.setAccount(serverConfigDTO.getAccount());
        if (serverConfigDTO.getPassword() != null) {
            String decryptPwd = AesUtil.descrypt(serverConfigDTO.getPassword(), RedLineUtil.getRealAdminRedLine());
            requestParamDTO.setPwd(decryptPwd);
        }
        requestParamDTO.setIp(serverConfigDTO.getServerIp());
        requestParamDTO.setPort(serverConfigDTO.getGatewayPort());
        requestParamDTO.setPath(path);
        requestParamDTO.setRequestData(requestBody);

        return requestParamDTO;
    }

    private <T> T requestRccm(RequestParamDTO<?> requestParam, Class<T> resultClass) throws BusinessException {
        try {
            LOGGER.info("向r-center发送请求, 参数[{}]", JSON.toJSONString(requestParam));
            RemoteResponse<JSONObject> remoteResponse = restUtil.onceRequest(requestParam);
            LOGGER.info("向r-center发送请求, 返回结果[{}]", JSON.toJSONString(remoteResponse));
            return remoteResponse.getContent().getData().toJavaObject(resultClass);
        } catch (BusinessException e) {
            LOGGER.error(String.format("向r-center发送请求发生异常，参数[%s]", JSON.toJSONString(requestParam)), e);
            throw e;
        }
    }

}
