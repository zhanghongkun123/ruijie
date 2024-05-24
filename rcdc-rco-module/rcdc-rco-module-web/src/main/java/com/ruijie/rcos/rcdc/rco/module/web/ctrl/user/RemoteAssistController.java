package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerRemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserLoginRecordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.RemoteAssistPlatformEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.RemoteAssistInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.web.dto.RemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.RemoteAssistStrategyService;
import com.ruijie.rcos.rcdc.rco.module.web.validation.RecycleBinValidation;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 远程协助web控制器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月24日
 *
 * @author artom
 */
@Controller
@RequestMapping("/rco/user/remoteAssist")
@EnableCustomValidate(validateClass = RecycleBinValidation.class)
public class RemoteAssistController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAssistController.class);

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private RemoteAssistMgmtAPI remoteAssistInquire;

    @Autowired
    private ComputerRemoteAssistMgmtAPI computerRemoteAssistMgmtAPI;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private List<RemoteAssistStrategyService> assistStrategyServiceList;

    @Autowired
    private UserLoginRecordAPI userLoginRecordAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * * 发起远程协助请求
     *
     * @param request id array web request
     * @param session 会话
     * @return web response
     * @throws BusinessException 业务异常
     */
    @ApiOperation("远程协助")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/assistRequest")

    @EnableAuthority
    public DefaultWebResponse assistRequest(IdWebRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(session, "session must not be null");
        String desktopName = obtainDesktopName(request.getId());
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(request.getId());
        try {

            RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest(request.getId(), session.getUserId(), session.getUserName());
            boolean isAutoAgree = desktopAPI.isRemoteAssistAutoAgree(request.getId());
            // 多会话桌面池桌面暂不支持远程协助
            CloudDesktopDetailDTO desktopDetailById = cloudDesktopMgmtAPI.getDesktopDetailById(request.getId());
            if (CbbDesktopSessionType.MULTIPLE == desktopDetailById.getSessionType()) {
                if (desktopDetailById.getImageUsage() == ImageUsageTypeEnum.APP) {
                    throw new BusinessException(UserBusinessKey.RCDC_RCO_MULTI_RCA_HOST_ASSIST, desktopDetailById.getDesktopName());
                } else {
                    throw new BusinessException(UserBusinessKey.RCDC_RCO_MULTI_DESKTOP_ASSIST, desktopDetailById.getDesktopName());
                }
            }
            remoteAssistRequest.setAutoAgree(isAutoAgree);
            remoteAssistInquire.applyRemoteAssist(remoteAssistRequest);
            userLoginRecordAPI.deleteRemoteAssistanceCache(remoteAssistRequest.getDeskId().toString());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_ASSIST_REQUEST_SUC_LOG, desktopType, desktopName);
            return DefaultWebResponse.Builder.success();
        } catch (BusinessException e) {
            String i18nMessage =
                    desktopType.equals(Constants.APP_CLOUD_DESKTOP) ? e.getI18nMessage().replace(Constants.CLOUD_DESKTOP, Constants.APP_CLOUD_DESKTOP)
                            : e.getI18nMessage();
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_ASSIST_REQUEST_FAIL_LOG, desktopType, desktopName, i18nMessage);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_REMOTE_ASSIST_FAIL, e, i18nMessage);
        }
    }


    /**
     * * 取消/停止远程协助
     *
     * @param request id array web request
     * @param session 会话
     * @return web response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/assistStop")
    public DefaultWebResponse assistStop(IdWebRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(session, "session must not be null");

        UUID userId = session.getUserId();
        String desktopName = obtainDesktopName(request.getId());
        String desktopType = userDesktopMgmtAPI.getImageUsageByDeskId(request.getId());
        try {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_ASSIST_STOP_SUC_LOG, desktopType, desktopName);
            RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest();
            remoteAssistRequest.setDeskId(request.getId());
            remoteAssistRequest.setAdminId(userId);
            remoteAssistInquire.cancelRemoteAssist(remoteAssistRequest);
            return DefaultWebResponse.Builder.success();
        } catch (BusinessException e) {
            String i18nMessage =
                    desktopType.equals(Constants.APP_CLOUD_DESKTOP) ? e.getI18nMessage().replace(Constants.CLOUD_DESKTOP, Constants.APP_CLOUD_DESKTOP)
                            : e.getI18nMessage();
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_ASSIST_STOP_FAIL_LOG, desktopType, desktopName, i18nMessage);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_ASSIST_STOP_FAIL_LOG, e, desktopType, desktopName, i18nMessage);
        }
    }

    private String obtainDesktopName(UUID desktopId) {
        String desktopName = desktopId.toString();
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(desktopId);
            desktopName = cloudDesktopDetailDTO.getDesktopName();
        } catch (BusinessException e) {
            LOGGER.error("获取远程协助桌面名称失败", e);
        }
        return desktopName;
    }

    /**
     * * https请求定时查询状态
     *
     * @param request id array web request
     * @param session 会话
     * @return web response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/pollingState")
    public DefaultWebResponse pollingState(IdWebRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(session, "session must not be null");

        RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest();
        remoteAssistRequest.setDeskId(request.getId());
        remoteAssistRequest.setAdminId(session.getUserId());
        CloudDesktopRemoteAssistDTO dto = remoteAssistInquire.queryRemoteAssistInfo(remoteAssistRequest);
        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * * 成功创建 EST 窗口通知
     *
     * @param request id array web request
     * @param sessionContext sessionContext
     * @return web response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/estConnectSuccess")
    public DefaultWebResponse estConnectSuccess(RemoteAssistInfoRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest();
        remoteAssistRequest.setDeskId(request.getId());
        remoteAssistRequest.setAdminId(sessionContext.getUserId());

        if (RemoteAssistPlatformEnum.PC.getPlatform().equals(request.getPlatform())) {
            computerRemoteAssistMgmtAPI.createVncChannelResult(remoteAssistRequest);
        } else {
            remoteAssistInquire.createVncChannelResult(remoteAssistRequest);
        }
        return DefaultWebResponse.Builder.success();
    }

    /**
     * est client 请求获取远程信息
     *
     * @param request id web request
     * @param session 会话
     * @return web response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getConnectInfo")
    public DefaultWebResponse getConnectInfo(RemoteAssistInfoRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(session, "session must not be null");

        RemoteAssistStrategyService remoteAssistStrategyService = obtainService(request.getComponent());
        if (Objects.isNull(remoteAssistStrategyService)) {
            return DefaultWebResponse.Builder.success();
        }
        RemoteAssistDTO remoteAssistDTO = new RemoteAssistDTO(request.getId(), session.getUserId());
        CloudDesktopRemoteAssistDTO assistInfo = remoteAssistStrategyService.queryVncUrl(remoteAssistDTO);
        return DefaultWebResponse.Builder.success(assistInfo);
    }

    /**
     * 保活接口
     * 查询当前远程状态
     *
     * @param request /
     * @param session /
     * @return DefaultWebResponse
     * @throws BusinessException /
     */
    @RequestMapping(value = "/queryState")
    public DefaultWebResponse queryState(RemoteAssistInfoRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(session, "session must not be null");


        RemoteAssistStrategyService remoteAssistStrategyService = obtainService(request.getComponent());
        if (Objects.isNull(remoteAssistStrategyService)) {
            return DefaultWebResponse.Builder.success();
        }
        RemoteAssistDTO remoteAssistDTO = new RemoteAssistDTO(request.getId(), session.getUserId());
        RemoteAssistStateDTO assistStateDTO = remoteAssistStrategyService.queryState(remoteAssistDTO);
        return DefaultWebResponse.Builder.success(assistStateDTO);
    }

    private RemoteAssistStrategyService obtainService(String component) {
        return assistStrategyServiceList.stream().filter(handler -> handler.isNeedHandle(component)).findFirst().orElseThrow(null);
    }
}
