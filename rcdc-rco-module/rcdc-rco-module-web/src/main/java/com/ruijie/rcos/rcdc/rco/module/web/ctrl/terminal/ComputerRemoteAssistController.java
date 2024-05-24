package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerRemoteAssistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopRemoteAssistDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.validation.RecycleBinValidation;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * Description: 远程协助web控制器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月24日
 *
 * @author artom
 */
@Api(tags = "PC终端远程协助")
@Controller
@RequestMapping("/rco/computer/remoteAssist")
@EnableCustomValidate(validateClass = RecycleBinValidation.class)
public class ComputerRemoteAssistController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRemoteAssistController.class);

    @Autowired
    private ComputerRemoteAssistMgmtAPI remoteAssistInquire;

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * @api {POST} /rco/computer/remoteAssist/assistRequest 发起远程协助请求
     * @apiName 发起远程协助请求
     * @apiGroup 远程协助
     * @apiDescription 发起远程协助请求
     *
     * @apiParam (请求体字段说明) {String} id PC终端Id
     * @apiParam (请求体字段说明) {String} module 计算机名
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "id": "a257cf6f-2589-45f0-aa20-8ada46483091",
     *      "module": "rco/computer"
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content": null,
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *      "content":null,
     *      "message":"发起远程协助失败，失败原因：向PC发起远程协助询问失败，请核对用户是否正确打开桌面小助手",
     *      "msgArgArr":["向PC发起远程协助询问失败，请核对用户是否正确打开桌面小助手"],
     *      "msgKey":"rcdc_rco_desktop_remote_assist_fail",
     *      "status":"ERROR"
     * }
     */

    /**
     * * 发起远程协助请求
     *
     * @param request        id array web request
     * @param session        会话
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
        ComputerInfoResponse computerInfoResponse = checkComputer(request);
        String desktopName = computerInfoResponse.getMac();
        try {
            remoteAssistInquire.remoteAssistInquire(new RemoteAssistRequest(request.getId(), session.getUserId(), session.getUserName()));
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_ASSIST_REQUEST_SUC_LOG, desktopName);
            return DefaultWebResponse.Builder.success();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_ASSIST_REQUEST_FAIL_LOG, desktopName, e.getI18nMessage());
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_REMOTE_ASSIST_FAIL, e, e.getI18nMessage());
        }
    }



    /**
     * @api {POST} /rco/computer/remoteAssist/assistStop  取消/停止远程协助
     * @apiName 取消/停止远程协助
     * @apiGroup 远程协助
     * @apiDescription 取消/停止远程协助
     *
     * @apiParam (请求体字段说明) {String} id PC终端Id
     * @apiParam (请求体字段说明) {String} module 计算机名
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "id": "a257cf6f-2589-45f0-aa20-8ada46483091",
     *      "module": "rco/computer"
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content": null,
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     */

    /**
     * * 取消/停止远程协助
     *
     * @param request        id array web request
     * @param session        会话
     * @return web response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/assistStop")
    public DefaultWebResponse assistStop(IdWebRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(session, "session must not be null");

        UUID userId = session.getUserId();
        ComputerInfoResponse computerInfoResponse = checkComputer(request);
        String desktopName = computerInfoResponse.getMac();
        try {
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_ASSIST_STOP_SUC_LOG, desktopName);
            RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest();
            remoteAssistRequest.setDeskId(request.getId());
            remoteAssistRequest.setAdminId(userId);
            remoteAssistInquire.cancelRemoteAssist(remoteAssistRequest);
            return DefaultWebResponse.Builder.success();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_COMPUTER_ASSIST_STOP_FAIL_LOG, desktopName, e.getI18nMessage());
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_ASSIST_STOP_FAIL_LOG, e, e.getI18nMessage());
        }
    }

    private ComputerInfoResponse checkComputer(IdWebRequest request) throws BusinessException {
        ComputerInfoResponse computerInfoResponse =
                computerBusinessAPI.getComputerInfoByComputerId(new ComputerIdRequest(request.getId()));
        String computerIdentification = StringUtils.hasText(computerInfoResponse.getMac()) ?
                computerInfoResponse.getMac() : computerInfoResponse.getIp();
        if (computerInfoResponse.getType() == ComputerTypeEnum.THIRD) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_NOT_SUPPORT_THIRD_PARTY_OP, computerIdentification);
        }
        return computerInfoResponse;
    }
    /**
     * @api {POST} /rco/computer/remoteAssist/pollingState  定时查询状态
     * @apiName 定时查询状态
     * @apiGroup 远程协助
     * @apiDescription 定时查询状态
     *
     * @apiParam (请求体字段说明) {String} id PC终端Id
     * @apiParam (请求体字段说明) {String} module 计算机名
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "id": "a257cf6f-2589-45f0-aa20-8ada46483091",
     *      "module": "rco/computer"
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {JSON} content.assistInfo 远程信息
     * @apiSuccess (响应字段说明) {JSON} content.assistInfo.ip 服务器ip
     * @apiSuccess (响应字段说明) {JSON} content.assistInfo.password 密码
     * @apiSuccess (响应字段说明) {JSON} content.assistInfo.port 端口
     * @apiSuccess (响应字段说明) {JSON} content.assistInfo.token PC终端Id
     * @apiSuccess (响应字段说明) {String="WAITING","AGREE","REJECT","START_FAIL","STOP_USER",
     * "STOP_ADMIN","BUSY","STOP_TIMEOUT","TOKEN_INVALID","DATA_ILLEGAL","IN_REMOTE_ASSIST",
     * "RESPONSE_TIMEOUT","FINISH" } content.assistState 远程状态
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "assistInfo":null,
     *          "assistState":"WAITING"
     *      },
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "assistInfo":{
     *              "ip":"172.28.84.107",
     *              "password":"{5357b05",
     *              "port":5700,
     *              "token":"a257cf6f-2589-45f0-aa20-8ada46483091"
     *          },
     *          "assistState":"AGREE"
     *      },
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     */

    /**
     * * https请求定时查询状态
     *
     * @param request        id array web request
     * @param session        会话
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
        CloudDesktopRemoteAssistDTO remoteAssistDTO = remoteAssistInquire.queryRemoteAssistInfo(remoteAssistRequest);
        return DefaultWebResponse.Builder.success(remoteAssistDTO);
    }


    /**
     * @api {POST} /rco/computer/remoteAssist/createVncSuc  成功创建VNC窗口连接通知
     * @apiName 成功创建VNC窗口连接通知
     * @apiGroup 远程协助
     * @apiDescription 成功创建VNC窗口连接通知
     *
     * @apiParam (请求体字段说明) {String} id PC终端Id（定时查询接口状态响应字段content.assistInfo.token）
     *
     * @apiParamExample {json} 请求体示例
     * {
     *      "id": "a257cf6f-2589-45f0-aa20-8ada46483091",
     * }
     *
     * @apiSuccess (响应字段说明) {JSON} content 响应数据
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     *
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content": null,
     *      "message":null,
     *      "msgArgArr":null,
     *      "msgKey":null,
     *      "status":"SUCCESS"
     * }
     *
     * @apiErrorExample {json} 异常响应
     * {
     *     "content": null,
     *     "message": "系统内部错误，请联系管理员",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_internal_error",
     *     "status": "ERROR"
     * }
     */
    /**
     * * 成功创建VNC窗口连接通知
     *
     * @param request        id array web request
     * @param sessionContext sessionContext
     * @return web response
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/createVncSuc")
    public DefaultWebResponse createVncSuc(IdWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        RemoteAssistRequest remoteAssistRequest = new RemoteAssistRequest();
        remoteAssistRequest.setDeskId(request.getId());
        remoteAssistRequest.setAdminId(sessionContext.getUserId());
        remoteAssistInquire.createVncChannelResult(remoteAssistRequest);
        return DefaultWebResponse.Builder.success();
    }

}
