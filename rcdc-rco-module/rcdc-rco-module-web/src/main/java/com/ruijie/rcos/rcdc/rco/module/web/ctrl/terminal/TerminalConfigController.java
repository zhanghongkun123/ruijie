package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;


import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalConfigTipVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.UploadTerminalConfigBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalConfigImportStatusVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalConfigVersionInfoVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalConfigDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalConfigPreviewDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalConfigVersionDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.CbbTerminalConfigImportRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.CbbTerminalConfigPageQueryRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalConfigImportStatusEnums;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.annotation.FileUpload;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 终端硬件配置控制器
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/16
 *
 * @author luocl
 */
@Api(tags = "终端管理")
@Controller
@RequestMapping("/rco/terminal/config")
@EnableCustomValidate(enable = false)
public class TerminalConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalConfigController.class);

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;


    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Autowired
    private TerminalConfigAPI terminalConfigAPI;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 上传安装包。
     *
     * @param file 请求参数
     * @param taskBuilder 批处理任务
     * @param session 会话
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("上传终端配置文件")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V6_0_1, descriptions = {"终端配置包上传后处理"})})
    @RequestMapping("create")
    @EnableAuthority
    @FileUpload(uploadTarget = FilePathContants.SOFT_PATH)
    public DefaultWebResponse uploadDeskSoft(ChunkUploadFile file, BatchTaskBuilder taskBuilder, SessionContext session) throws BusinessException {
        Assert.notNull(file, "file is null");
        Assert.notNull(taskBuilder, "taskBuilder is null");

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_ITEM_NAME));

        UploadTerminalConfigBatchTaskHandler uploadTerminalConfigBatchTaskHandler =
                new UploadTerminalConfigBatchTaskHandler(file, batchTaskItem, auditLogAPI, terminalConfigAPI);

        BatchTaskSubmitResult result = taskBuilder
                .setTaskName(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_DESC, file.getFileName())
                .registerHandler(uploadTerminalConfigBatchTaskHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/list
     * @apiName /list
     * @apiGroup /rco/terminal/config
     * @apiDescription 获取终端信息
     * @apiParam (请求体字段说明) {Integer} page 页码
     * @apiParam (请求体字段说明) {Integer} limit 每页展示数量
     * @apiParam (请求体字段说明) {MatchArr} matchArr 查询条件数组
     * @apiParam (请求体字段说明) {String} matchArr.fieldName 条件字段名
     * @apiParam (请求体字段说明) {String} matchArr.matchRule 匹配规则
     * @apiParam (请求体字段说明) {String} matchArr.type 匹配类型
     * @apiParam (请求体字段说明) {String[]} matchArr.valueArr 匹配条件的值
     * @apiParam (请求体字段说明) {Sort} sortArr 排序条件数组
     * @apiParam (请求体字段说明) {String} sortArr.fieldName 排序字段
     * @apiParam (请求体字段说明) {String} sortArr.direction 顺序
     * @apiParamExample {json} 请求体示例
     * {
     *      "page": 0,
     *      "limit": 20,
     *      "matchArr": [],
     *      "sortArr": [],
     *      "needForceRefresh": false
     * }
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccess (响应字段说明) {String} itemArr.productId 终端产品ID
     * @apiSuccess (响应字段说明) {String} itemArr.productType 终端型号
     * @apiSuccess (响应字段说明) {String} itemArr.supportWorkMode 工作模式
     * @apiSuccess (响应字段说明) {String} itemArr.authMode 授权模式
     * @apiSuccess (响应字段说明) {String} itemArr.createTime 创建时间
     * @apiSuccessExample {json} 成功响应
     * {
     * "content": {
     *  "itemArr": [
     *  {
     *      "productId": "112233",
     *      "productType": "RG-CT6200",
     *      "supportWorkMode": "IDV",
     *      "authMode": "IDV",
     *      "skipAuthSn":""
     *  },
     *  {
     *      "productId": "112233",
     *      "productType": "RG-CT5200",
     *      "supportWorkMode": "IDV",
     *      "authMode": "IDV",
     *      "skipAuthSn":""
     *  }
     *],
     *    "total": 2
     * },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     *  }
     * @apiErrorExample {json} 异常响应
     * {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     * }
     */
    /**
     * 查询。
     *
     * @param request 分页查参数
     * @param session 会话
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询终端配置文件")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"终端硬件配置列表"})})
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<CbbTerminalConfigDTO>> list(CbbTerminalConfigPageQueryRequest request,
                                                                           SessionContext session) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(session, "sessionContext can not be null");
        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        PageQueryResponse<CbbTerminalConfigDTO> response = cbbTerminalConfigAPI.pageQuery(request);
        return CommonWebResponse.success(response);
    }


    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/preImport
     * @apiName /preImport
     * @apiGroup /rco/terminal/config
     * @apiParam (请求体字段说明) {String} filePath 文件路径
     * @apiParam (请求体字段说明) {String} fileName 文件名称
     * @apiParam (请求体字段说明) {String} fileMD5 文件md5值
     * @apiParam (请求体字段说明) {Long} fileSize 文件大小
     * @apiParam (请求体字段说明) {CustomData} customData 自定义内容
     * @apiParamExample {json} 请求体示例
     * {
     *      "filePath": ”/tmp/skyengine/upload/tmp/RG-Terminal_config_1.1.1.zip“,
     *      "fileName": "RG-Terminal_config_1.1.1",
     *      "fileMD5": "6e57b3d0ab62fc14e88f5bfb66712dfe",
     *      "fileSize": 12542154
     *}
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccess (响应字段说明) {String} itemArr.productId 终端产品ID
     * @apiSuccess (响应字段说明) {String} itemArr.productType 终端型号
     * @apiSuccess (响应字段说明) {String} itemArr.supportWorkMode 工作模式
     * @apiSuccess (响应字段说明) {String} itemArr.authMode 授权模式
     * @apiSuccess (响应字段说明) {String} itemArr.createTime 创建时间
     * @apiSuccessExample {json} 成功响应
     * {
     * "content": {
     *  "itemArr": [
     *  {
     *      "productId": "112233",
     *      "productType": "RG-CT6200",
     *      "supportWorkMode": "IDV",
     *      "authMode": "IDV",
     *      "skipAuthSn":""
     *  },
     *  {
     *      "productId": "112233",
     *      "productType": "RG-CT5200",
     *      "supportWorkMode": "IDV",
     *      "authMode": "IDV",
     *      "skipAuthSn":""
     *  }
     *],
     *    "total": 2
     * },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     *  }
     * @apiErrorExample {json} 异常响应
     * {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     * }
     */
    /**
     * 上传安装包。
     * @param request 操作日志
     * @param session 会话
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询待导入终端配置文件")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"预览终端硬件配置"})})
    @RequestMapping(value = "preImport/list", method = RequestMethod.POST)
    public DefaultPageResponse<CbbTerminalConfigPreviewDTO> getPreImportList(CbbTerminalConfigPageQueryRequest request,
                                                                             SessionContext session) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(session, "session can not be null");
        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        return cbbTerminalConfigAPI.pageQueryPreImportList(request);
    }


    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/import
     * @apiName /import
     * @apiGroup /rco/terminal/config
     * @apiParam (请求体字段说明) {String} filePath 文件路径
     * @apiParam (请求体字段说明) {String} fileName 文件名称
     * @apiParam (请求体字段说明) {String} fileMD5 文件md5值
     * @apiParam (请求体字段说明) {Long} fileSize 文件大小
     * @apiParam (请求体字段说明) {CustomData} customData 自定义内容
     * @apiParamExample {json} 请求体示例
     * {
     *      "filePath": ”/tmp/skyengine/upload/tmp/RG-Terminal_config_1.1.1.zip“,
     *      "fileName": "RG-Terminal_config_1.1.1",
     *      "fileMD5": "6e57b3d0ab62fc14e88f5bfb66712dfe",
     *      "fileSize": 12542154
     *}
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccess (响应字段说明) {String} itemArr.productId 终端产品ID
     * @apiSuccess (响应字段说明) {String} itemArr.productType 终端型号
     * @apiSuccess (响应字段说明) {String} itemArr.supportWorkMode 工作模式
     * @apiSuccess (响应字段说明) {String} itemArr.authMode 授权模式
     * @apiSuccess (响应字段说明) {String} itemArr.createTime 创建时间
     * @apiSuccessExample {json} 成功响应
     *{
     *      "content": {
     *      "hasError": true,
     *      "errorMsg": "桌面正在运行"
     *    },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     * }
     * @apiErrorExample {json} 异常响应
     *  {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     *}
     */
    /**
     * 导入终端配置文件
     * @param session 会话
     * @return CommonWebResponse
     */
    @ApiOperation("导入终端配置文件")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"导入终端配置文件"})})
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public  CommonWebResponse importConfig(SessionContext session)  {
        Assert.notNull(session, "session can not be null");

        try {
            adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
            terminalConfigAPI.importTerminalConfig();
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_SUCCESS);
            return CommonWebResponse.success(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_SUCCESS, new String[0]);
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_FAIL, ex.getI18nMessage());
            return CommonWebResponse.fail(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_FAIL, new String[] {ex.getI18nMessage()});
        }
    }

    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/exitImportProcess
     * @apiName /exitImportProcess
     * @apiGroup /rco/terminal/config
     * @apiParamExample {json} 请求体示例
     * {
     *}
     * @apiSuccessExample {json} 成功响应
     *{
     *      "content": {
     *    },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     * }
     * @apiErrorExample {json} 异常响应
     *  {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     *}
     */
    /**
     *
     * @param session 会话
     * @return CommonWebResponse
     * @throws BusinessException
     */
    /**
     * 结束导入
     * @param session 会话
     * @return  CommonWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("结束导入")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"导入终端配置文件"})})
    @RequestMapping(value = "exitImportProcess", method = RequestMethod.POST)
    public  CommonWebResponse exitImportProcess(SessionContext session) throws BusinessException {
        Assert.notNull(session, "session can not be null");
        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        cbbTerminalConfigAPI.exitImportProcess();
        return CommonWebResponse.success();
    }


    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/cancelRemind
     * @apiName /cancelRemind
     * @apiGroup /rco/terminal/config
     * @apiParamExample {json} 请求体示例
     * {
     *}
     * @apiSuccessExample {json} 成功响应
     *{
     *      "content": {
     *    },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     * }
     * @apiErrorExample {json} 异常响应
     *  {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     *}
     */
    /**
     * 不再提醒版本更新
     * @param session session
     * @return vo
     * @throws BusinessException 业务异常
     */
    @ApiOperation("不再提醒版本更新")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"导入终端配置文件"})})
    @RequestMapping(value = "cancelRemind", method = RequestMethod.POST)
    public  CommonWebResponse cancelRemind(SessionContext session) throws BusinessException {
        Assert.notNull(session, "session can not be null");
        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        cbbTerminalConfigAPI.cancelCheckTerminalConfigVersion();
        return CommonWebResponse.success();
    }




    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/getImportStatus
     * @apiName /getImportStatus
     * @apiGroup /rco/terminal/config
     * @apiParamExample {json} 请求体示例
     * {
     *}
     *
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccess (响应字段说明) {String} content.status 状态
     * @apiSuccessExample {json} 成功响应
     *{
     *      "content": {
     *        "status" : "IMPORTING"
     *    },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     * }
     * @apiErrorExample {json} 异常响应
     *  {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     *}
     */
    /**
     *
     * @param session session
     * @return res
     * @throws BusinessException e
     */
    @ApiOperation("获取导入状态")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"导入终端配置文件"})})
    @RequestMapping(value = "getImportStatus", method = RequestMethod.POST)
    public  CommonWebResponse getImportStatus(SessionContext session) throws BusinessException {
        Assert.notNull(session, "session can not be null");
        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        CbbTerminalConfigImportStatusEnums status = cbbTerminalConfigAPI.getImportStats();
        TerminalConfigImportStatusVO vo = new TerminalConfigImportStatusVO();
        vo.setStatus(status.toString());
        return CommonWebResponse.success(vo);
    }



    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/checkTerminalConfigVersion
     * @apiName /checkTerminalConfigVersion
     * @apiGroup /rco/terminal/config
     * @apiParamExample {json} 请求体示例
     * {
     *}
     *
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccess (响应字段说明) {String} content.hasNew 是否有待更新版
     * @apiSuccess (响应字段说明) {String} content.sourceType 来源
     * @apiSuccess (响应字段说明) {String} content.fileName 文件名称
     * @apiSuccessExample {json} 成功响应
     *{
     *      "content": {
     *        "hasNew" : true,
     *        "fileName" : "RG-Terminal_config_1.1.0.zip",
     *        "sourceType": "SDP"
     *    },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     * }
     * @apiErrorExample {json} 异常响应
     *  {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     *}
     */
    /**
     *
     * @param session session
     * @return  CommonWebResponse
     * @throws BusinessException e
     */
    @ApiOperation("检查是否有待导入更新")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"导入终端配置文件"})})
    @RequestMapping(value = "checkTerminalConfigVersion", method = RequestMethod.POST)
    public  CommonWebResponse checkTerminalConfigVersion(SessionContext session) throws BusinessException {
        Assert.notNull(session, "session can not be null");
        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        return CommonWebResponse.success(cbbTerminalConfigAPI.checkTerminalConfigVersion());
    }

    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/preImport
     * @apiName /preImport
     * @apiGroup /rco/terminal/config
     * @apiParamExample {json} 请求体示例
     * {
     *}
     *
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccess (响应字段说明) {String} content.hasNew 是否有待更新版
     * @apiSuccess (响应字段说明) {String} content.sourceType 来源
     * @apiSuccess (响应字段说明) {String} content.fileName 文件名称
     * @apiSuccessExample {json} 成功响应
     *{
     *      "content": {
     *        "hasNew" : true,
     *        "fileName" : "RG-Terminal_config_1.1.0.zip",
     *        "sourceType": "SDP"
     *    },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     * }
     * @apiErrorExample {json} 异常响应
     *  {
     *      "content": null
     *      "message": "xxx",
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     *}
     */
    /**
     *
     * @param request req
     * @param session session
     * @return  CommonWebResponse
     * @throws BusinessException e
     */
    @ApiOperation("SDP导入新包时从界面跳转直接进行预览")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"导入终端配置文件"})})
    @RequestMapping(value = "preImport", method = RequestMethod.POST)
    public  CommonWebResponse preImport(CbbTerminalConfigImportRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(session, "session can not be null");
        Assert.notNull(request, "request can not be null");

        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        cbbTerminalConfigAPI.preImport(request);
        return CommonWebResponse.success();
    }


    /**
     * @apiIgnore
     * @api {POST} /rco/terminal/config/getVersionInfo
     * @apiName /getVersionInfo
     * @apiGroup /rco/terminal/config
     * @apiParamExample {json} 请求体示例
     * {
     *}
     *
     * @apiSuccess (响应字段说明) {Object} content 响应信息
     * @apiSuccess (响应字段说明) {String} message 提示信息
     * @apiSuccess (响应字段说明) {String[]} msgArgArr 消息参数填充
     * @apiSuccess (响应字段说明) {String} msgKey 消息参数填充
     * @apiSuccess (响应字段说明) {String} status 状态信息
     * @apiSuccess (响应字段说明) {String} content.version 当前配置版本
     * @apiSuccess (响应字段说明) {String} content.createTime 导入时间
     * @apiSuccessExample {json} 成功响应
     *{
     *      "content": {
     *           "version" : "1.1.0",
     *          "createTime" : "1699535662782"
     *      },
     *      "message": null,
     *      "msgArgArr": null,
     *      "msgKey": null,
     *      "status": "SUCCESS"
     * }
     * @apiErrorExample {json} 异常响应
     *  {
     *      "content": null
     *      "message": null
     *      "msgArgArr": "xxx",
     *      "msgKey": "xxx",
     *      "status": "ERROR"
     *}
     */
    /**
     *
     * @param session session
     * @return  CommonWebResponse
     * @throws BusinessException e
     */
    @ApiOperation("获取版本和导入时间信息")
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = {"获取版本和导入时间信息"})})
    @RequestMapping(value = "getVersionInfo", method = RequestMethod.POST)
    public  CommonWebResponse getVersionInfo(SessionContext session) throws BusinessException {
        Assert.notNull(session, "session can not be null");

        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        CbbTerminalConfigVersionDTO versionDTO = cbbTerminalConfigAPI.getVersionInfo();
        TerminalConfigVersionInfoVO vo = new TerminalConfigVersionInfoVO();
        vo.setVersion(versionDTO.getVersion());
        vo.setCreateTime(versionDTO.getCreateTime());
        return CommonWebResponse.success(vo);
    }

    /**
     *
     * @param session 会话
     * @return 结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取是否有非法终端在线")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"获取是否有非法终端在线"})})
    @RequestMapping(value = "getImportTerminalConfigMessage")
    public CommonWebResponse getImportTerminalConfigMessage(SessionContext session) throws BusinessException {
        Assert.notNull(session, "session can not be null");

        adminPermissionAPI.hasPermission(MenuType.TERMINAL_CONFIG, session.getUserId());
        TerminalConfigTipVO vo = new TerminalConfigTipVO();
        vo.setHasUnavailableTerminal(cbbTerminalConfigAPI.hasUnavailableTerminalExist());
        return CommonWebResponse.success(vo);
    }
}
