package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.authentication.ApplyRccpLoginTokenResponse;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DashboardStatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcdcTokenAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeletedUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RepeatStartVmWebclientNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageClusterVersionInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageForMasterClusterAllDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.rccm.RccmManageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.constants.WebclientNotifyAction;
import com.ruijie.rcos.rcdc.rco.module.def.enums.IacBusinessMapEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.OpenApiBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.RccmManageRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request.WebclientNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request.*;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: RccmManageRestServerImpl
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
@Service
public class RccmManageRestServerImpl implements RccmManageRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RccmManageRestServerImpl.class);

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private RcdcTokenAPI rcdcTokenAPI;

    @Autowired
    private DashboardStatisticsAPI dashboardStatisticsAPI;

    @Autowired
    private WebclientNotifyAPI webclientNotifyAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    @Override
    public RccmManageRestServerResponse notifyJoinManage(RccmManageRestServerRequest request) {
        Assert.notNull(request, "request is not null");
        RccmManageRequest rccmManageRequest = new RccmManageRequest();
        BeanUtils.copyProperties(request, rccmManageRequest);
        try {
            RccmManageResponse rccmManageResponse = rccmManageAPI.joinManage(rccmManageRequest);
            //根据返回值 进行判断结果 记录日志
            if (CommonMessageCode.SUCCESS == rccmManageResponse.getCode()) {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCCM_JOIN_MANAGE_SUCC, request.getServerIp());
            } else {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCCM_JOIN_MANAGE_ERROR, request.getServerIp(), rccmManageResponse.getMsg());
            }

            return new RccmManageRestServerResponse(rccmManageResponse);
        } catch (BusinessException e) {
            LOGGER.error(String.format("RCCM（服务器IP：%s）通知加入纳管发生异常。clusterId = %s", request.getServerIp(), request.getClusterId()), e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCCM_JOIN_MANAGE_ERROR, e, request.getServerIp(), e.getI18nMessage());
            return new RccmManageRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public RccmManageRestServerResponse notifyUnifiedLogin(RccmManageRestServerRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        RccmManageRequest rccmManageRequest = new RccmManageRequest();
        BeanUtils.copyProperties(request, rccmManageRequest);
        RccmManageResponse rccmManageResponse = rccmManageAPI.updateUnifiedLogin(rccmManageRequest);
        return new RccmManageRestServerResponse(rccmManageResponse);
    }

    @Override
    public RccmManageRestServerResponse notifyExitManage(RccmManageRestServerRequest request) {
        Assert.notNull(request, "request is not null");
        RccmManageRequest rccmManageRequest = new RccmManageRequest();
        BeanUtils.copyProperties(request, rccmManageRequest);
        try {
            RccmManageResponse rccmManageResponse = rccmManageAPI.exitManage(rccmManageRequest);
            // 根据返回值 进行判断结果 记录日志
            if (CommonMessageCode.SUCCESS == rccmManageResponse.getCode()) {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCCM_EXIST_MANAGE_SUCC, request.getServerIp());
            } else {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCCM_EXIST_MANAGE_ERROR, request.getServerIp(), rccmManageResponse.getMsg());
            }
            return new RccmManageRestServerResponse(rccmManageResponse);
        } catch (BusinessException e) {
            LOGGER.error(String.format("RCCM（服务器IP：%s）通知退出纳管发生异常。clusterId = %s", request.getServerIp(), request.getClusterId()), e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCCM_EXIST_MANAGE_ERROR, e, request.getServerIp(), e.getI18nMessage());
            return new RccmManageRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }

    }

    @Override
    public RccmManageRestServerResponse existingManage(RccmExistManageRestServerRequest request) {
        Assert.notNull(request, "request is not null");
        if (request.getClusterId() == null) {
            request.setClusterId("");
        }
        RccmManageResponse rccmManageResponse = rccmManageAPI.existingManage(request.getClusterId());
        RccmManageRestServerResponse commonRestServerResponse = new RccmManageRestServerResponse(rccmManageResponse.getCode());
        commonRestServerResponse.setMsg(rccmManageResponse.getMsg());
        return commonRestServerResponse;
    }

    @Override
    public RccmManageRestServerResponse testHealthState(RccmManageStateRestServerRequest request) {
        Assert.notNull(request, "request is not null");
        if (Objects.nonNull(request.getClusterVip())) {
            try {
                rccmManageAPI.validateRcdcVip(request.getClusterVip());
            } catch (BusinessException e) {
                // VIP与RCDC服务器的不一致
                RccmManageRestServerResponse commonRestServerResponse = new RccmManageRestServerResponse(CommonMessageCode.RCDC_VIP_ERROR);
                commonRestServerResponse.setMsg(e.getI18nMessage());
                return commonRestServerResponse;
            }
        }

        RccmManageStateRequest rccmManageStateRequest = new RccmManageStateRequest(request.getClusterId(), request.getServerIp());
        RccmManageResponse rccmManageResponse = rccmManageAPI.testHealthState(rccmManageStateRequest);
        RccmManageRestServerResponse commonRestServerResponse = new RccmManageRestServerResponse(rccmManageResponse.getCode());
        commonRestServerResponse.setMsg(rccmManageResponse.getMsg());
        return commonRestServerResponse;
    }

    @Override
    public ApplyRcdcLoginTokenResponse applyToken(VerifyAdminRestServerRequest request) {
        Assert.notNull(request, "request is not null");

        LOGGER.info("收到rcenter认证token请求[{}]", JSON.toJSONString(request));
        ApplyRcdcLoginTokenResponse rcdcLoginTokenResponse = new ApplyRcdcLoginTokenResponse();
        rcdcLoginTokenResponse.setCode(CommonMessageCode.SUCCESS);
        try {
            VerifyAdminRequest verifyAdminRequest = new VerifyAdminRequest();
            BeanUtils.copyProperties(request, verifyAdminRequest);
            String token = rcdcTokenAPI.applyToken(verifyAdminRequest);
            // 旧版本的时候还是把iacToken的值设置到token字段
            if (Objects.isNull(request.getSessionId())) {
                rcdcLoginTokenResponse.setToken(token);
            } else {
                rcdcLoginTokenResponse.setIacToken(token);
            }
            LOGGER.info("返回管理员[{}]token[{}]", request.getUserName(), token);
            return rcdcLoginTokenResponse;
        } catch (BusinessException e) {
            rcdcLoginTokenResponse.setCode(CommonMessageCode.CODE_ERR_OTHER);
            rcdcLoginTokenResponse.setMsg(e.getAttachment(UserTipContainer.Constants.USER_TIP, e.getI18nMessage()));
            return rcdcLoginTokenResponse;
        }

    }

    @Override
    public ApplyRccpLoginTokenResponse applyRccpToken() {
        try {
            ApplyRccpLoginTokenResponse applyRccpLoginTokenResponse = dashboardStatisticsAPI.applyRccpLoginToken(new CloudPlatformBaseRequest());
            return applyRccpLoginTokenResponse;
        } catch (BusinessException e) {
            LOGGER.error("获取rccpToken发生异常", e);
        }
        return new ApplyRccpLoginTokenResponse();
    }

    @Override
    public CommonRestServerResponse notifyPushUserToRccm() throws BusinessException {
        LOGGER.info("接收rccm推送用户通知");
        rccmManageAPI.pushAllUserToRccm();
        return new CommonRestServerResponse(CommonMessageCode.SUCCESS);
    }

    @Override
    public CommonRestServerResponse existUser(RccmExistUserRestServerRequest request) {
        Assert.notNull(request, "request is not null");
        boolean existUser = rccmManageAPI.existUser(request.getUsername());
        if (existUser) {
            return new CommonRestServerResponse(CommonMessageCode.SUCCESS);
        } else {
            return new CommonRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public CommonRestServerResponse updateVIP(RccmManageUpdateVipRestServerRequest request) {
        Assert.notNull(request, "request is not null");
        rccmManageAPI.updateRccmVIP(request.getClusterId(), request.getServerIp());
        return new CommonRestServerResponse(CommonMessageCode.SUCCESS);
    }

    @Override
    public LoginAdminAuthResponse loginAdminAuth(VerifyAdminRestServerRequest request) {
        Assert.notNull(request, "request is not null");
        VerifyAdminRequest verifyAdminRequest = new VerifyAdminRequest();
        BeanUtils.copyProperties(request, verifyAdminRequest);
        LoginAdminAuthResponse loginAdminAuthResponse = new LoginAdminAuthResponse(CommonMessageCode.SUCCESS);
        try {
            IacAdminDTO baseAdminDTO = rccmManageAPI.loginAdminAuth(verifyAdminRequest);
            loginAdminAuthResponse.setHasFirstTimeLoggedIn(baseAdminDTO.getHasFirstTimeLoggedIn());
            loginAdminAuthResponse.setNeedUpdatePassword(baseAdminDTO.getNeedUpdatePassword());
            loginAdminAuthResponse.setWeekPassword(baseAdminDTO.getWeakPassword());
            loginAdminAuthResponse.setPasswordRemindTimes(baseAdminDTO.getPasswordRemindTimes());
        } catch (BusinessException e) {
            loginAdminAuthResponse.setCode(CommonMessageCode.CODE_ERR_OTHER);
            String msg = IacBusinessMapEnum.mapIacErrorMsgToRcdcMsg(request.getUserName(), e);
            loginAdminAuthResponse.setMsg(msg);
            auditLogAPI.recordI18nLog(msg);
        }
        return loginAdminAuthResponse;
    }

    @Override
    public void notifyWebclient(WebclientNotifyRequest webclientNotifyRequest) throws BusinessException {
        Assert.notNull(webclientNotifyRequest, "webclientNotifyRequest is null.");
        String action = webclientNotifyRequest.getAction();
        Map<String, Object> requestBody = webclientNotifyRequest.getRequestBody();
        if (MapUtils.isNotEmpty(requestBody)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(requestBody);

            if (WebclientNotifyAction.NOTIFY_REMOTE_ASSIST.equals(action)) {
                RemoteAssistStateDTO remoteAssistStateDTO = jsonObject.toJavaObject(RemoteAssistStateDTO.class);
                // RCenter来源的请求
                webclientNotifyAPI.notifyRemoteAssistState(false, remoteAssistStateDTO);
            } else if (WebclientNotifyAction.NOTIFY_TERMINAL_DESKTOP_IS_ROBBED.equals(action)) {
                RepeatStartVmWebclientNotifyDTO repeatStartVmWebclientNotifyDTO = jsonObject.toJavaObject(RepeatStartVmWebclientNotifyDTO.class);
                // RCenter来源的请求
                webclientNotifyAPI.notifyTerminalDesktopIsRobbed(false, repeatStartVmWebclientNotifyDTO);
            } else if (WebclientNotifyAction.NOTIFY_USER_IS_DELETED.equals(action)) {
                DeletedUserInfoDTO deletedUserInfoDTO = jsonObject.toJavaObject(DeletedUserInfoDTO.class);
                // RCenter来源的请求
                webclientNotifyAPI.notifyUserDeleted(false, deletedUserInfoDTO);
            }
        }
    }

    @Override
    public UnifiedManageClusterVersionInfoResponse getVersionInfo() throws BusinessException {
        UnifiedManageClusterVersionInfoDTO unifiedManageClusterVersionInfoDTO = rccmManageAPI.getVersionInfo();
        UnifiedManageClusterVersionInfoResponse unifiedManageClusterVersionInfoResponse = new UnifiedManageClusterVersionInfoResponse();
        BeanUtils.copyProperties(unifiedManageClusterVersionInfoDTO, unifiedManageClusterVersionInfoResponse);
        return unifiedManageClusterVersionInfoResponse;
    }

    @Override
    public RccmManageRestServerResponse notifyStrategy(RccmUnifiedManageNotifyConfigRestServerRequest request) {
        Assert.notNull(request, "request is null.");
        LOGGER.info("接收统一管理同步模式入参:{}", JSON.toJSONString(request));
        RccmUnifiedManageNotifyConfigRequest unifiedManageNotifyConfigRequest = new RccmUnifiedManageNotifyConfigRequest();
        unifiedManageNotifyConfigRequest.setClusterId(request.getClusterId());
        unifiedManageNotifyConfigRequest.setClusterUnifiedManageStrategyDTOList(request.getClusterUnifiedManageStrategyDTOList());
        RccmManageResponse rccmManageResponse = rccmManageAPI.notifyStrategy(unifiedManageNotifyConfigRequest);
        return new RccmManageRestServerResponse(rccmManageResponse);
    }

    @Override
    public UnifiedManageForMasterClusterAllDataResponse collectAllData(UnifiedManageForMasterClusterAllDataRequest request) throws BusinessException {
        Assert.notNull(request, "UnifiedManageForMasterClusterAllDataRequest not be null");

        // CDC主动退出纳管时,会清理同步数据,rcenter可能收集到空的集合,同步给从集群
        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        if (Objects.isNull(rccmServerConfig) || StringUtils.isBlank(rccmServerConfig.getServerIp())) {
            throw new BusinessException(BusinessKey.RCDC_RCCM_MANAGE_CONFIG_NOT_EXIST);
        }

        // 只返回镜像、策略两种类型
        List<UnifiedManageForMasterClusterAllDataDTO> clusterAllDataDTOllDataDTOList =
                rccmManageAPI.collectAllDataByType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE, UnifiedManageFunctionKeyEnum.DESK_STRATEGY);

        UnifiedManageForMasterClusterAllDataResponse response = new UnifiedManageForMasterClusterAllDataResponse();
        response.setMasterClusterAllDataList(clusterAllDataDTOllDataDTOList);
        return response;
    }

    @Override
    public UnifiedBusinessInfoResponse getBusinessInfo(UUID unifiedDataId) throws BusinessException {
        Assert.notNull(unifiedDataId, "id is null.");

        UnifiedManageDataDTO unifiedManageDataDTO = rccmManageAPI.findByUnifiedManageDataId(unifiedDataId);
        return buildResponse(unifiedManageDataDTO, unifiedDataId);
    }

    private UnifiedBusinessInfoResponse buildResponse(UnifiedManageDataDTO unifiedManageDataDTO, UUID unifiedDataId) throws BusinessException {
        if (Objects.isNull(unifiedManageDataDTO)) {
            // 如果查不到信息，则返回空对象
            throw new BusinessException(OpenApiBusinessKey.RCDC_OPENAPI_UNIFIED_DATA_NOT_EXISTS, unifiedDataId.toString());
        }
        UnifiedBusinessInfoResponse response = new UnifiedBusinessInfoResponse();
        response.setBusinessId(unifiedManageDataDTO.getRelatedId());
        response.setFunctionKey(unifiedManageDataDTO.getRelatedType());
        return response;
    }
}
