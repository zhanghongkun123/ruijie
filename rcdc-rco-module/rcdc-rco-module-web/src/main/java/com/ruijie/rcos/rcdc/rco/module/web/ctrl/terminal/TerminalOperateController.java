package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.TerminalIdMappingUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.CloseTerminalBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DiskClearBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.InitIdvBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.RestartTerminalBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.EditAdminPwdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.OfflineLoginSettingWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalIdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalIdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.TerminalLogDownLoadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.OfflineLoginSettingWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbChangePasswordDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbOfflineLoginSettingDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalCollectLogStatusDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalLogFileInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCollectLogStateEnums;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/3
 *
 * @author Jarman
 */
@Api(tags = "终端操作")
@Controller
@RequestMapping("/rco/terminal")
@EnableCustomValidate(enable = false)
public class TerminalOperateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOperateController.class);

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbTerminalLogAPI cbbTerminalLogAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    private static final String TAR_GZ_SUFFIX = "tar.gz";

    private static final String SYMBOL_SPOT = ".";

    /**
     * 关闭终端
     *
     * @param request        终端id请求参数对象
     * @param builder        批量任务创建对象
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量关闭终端")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "shutdown", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> shutdownTerminal(TerminalIdArrWebRequest request,
                                                                     BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "TerminalIdArrWebRequest不能为null");
        Assert.notNull(builder, "builder不能为null");

        String[] terminalIdArr = request.getIdArr();
        if (terminalIdArr.length == 1) {
            return shutdownSingleTerminal(terminalIdArr[0]);
        } else {
            Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
            UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                    .itemName(TerminalBusinessKey.RCDC_TERMINAL_CLOSE_ITEM_NAME, new String[]{}).build()).iterator();
            CloseTerminalBatchTaskHandler handler = new CloseTerminalBatchTaskHandler(this.terminalOperatorAPI, idMap, iterator, auditLogAPI);
            handler.setCbbTerminalBasicInfoAPI(cbbTerminalOperatorAPI);
            BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_CLOSE_TASK_NAME, new String[]{})
                    .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_CLOSE_TASK_DESC, new String[]{}).registerHandler(handler).start();
            return CommonWebResponse.success(result);
        }
    }

    private CommonWebResponse<BatchTaskSubmitResult> shutdownSingleTerminal(String terminalId) {

        String terminalIdentification = terminalId;
        try {
            CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            terminalOperatorAPI.shutdown(terminalId);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CLOSE_SUCCESS_LOG, terminalIdentification);
            return CommonWebResponse.success(TerminalBusinessKey.RCDC_TERMINAL_CLOSE_SEND_SUCCESS, new String[]{});
        } catch (Exception e) {
            LOGGER.error("关闭终端失败：" + terminalIdentification, e);
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CLOSE_FAIL_LOG, terminalIdentification, ex.getI18nMessage());
                return CommonWebResponse.fail(TerminalBusinessKey.RCDC_TERMINAL_CLOSE_SEND_FAIL, new String[]{ex.getI18nMessage()});
            } else {
                throw new IllegalStateException("发送关闭终端命令异常，终端为[" + terminalIdentification + "]", e);
            }
        }
    }

    /**
     * 批量重启终端
     *
     * @param request        终端id请求参数对象
     * @param builder        批量任务创建对象
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量重启终端")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "restart", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse restartTerminal(TerminalIdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "TerminalIdWebRequest不能为null");
        Assert.notNull(builder, "builder不能为null");

        String[] terminalIdArr = request.getIdArr();
        if (terminalIdArr.length == 1) {
            return restartSingleTerminal(terminalIdArr[0]);
        } else {
            Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
            UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                    .itemName(TerminalBusinessKey.RCDC_TERMINAL_RESTART_ITEM_NAME, new String[]{}).build()).iterator();
            RestartTerminalBatchTaskHandler handler = new RestartTerminalBatchTaskHandler(this.terminalOperatorAPI, idMap, iterator, auditLogAPI);
            handler.setCbbTerminalBasicInfoAPI(cbbTerminalOperatorAPI);
            BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_RESTART_TASK_NAME, new String[]{})
                    .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_RESTART_TASK_DESC, new String[]{}).registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse restartSingleTerminal(String terminalId) throws BusinessException {
        String terminalIdentification = terminalId;
        try {
            CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            terminalOperatorAPI.restart(terminalId);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_RESTART_SUCCESS_LOG, terminalIdentification);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_RESTART_SEND_SUCCESS, new String[]{});
        } catch (Exception e) {
            LOGGER.error("重启终端失败：" + terminalIdentification, e);
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_RESTART_FAIL_LOG, terminalIdentification, ex.getI18nMessage());
                throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_RESTART_SEND_FAIL, ex, ex.getI18nMessage());
            } else {
                throw new IllegalStateException("重启终端异常，terminalId为[" + terminalIdentification + "]", e);
            }
        }
    }

    /**
     * 修改终端管理员密码
     *
     * @param request        请求参数
     * @param sessionContext 上下文
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @ApiVersions(@ApiVersion(value = Version.V3_1_1, descriptions = {"前端传递密码改为加密方式"}))
    @EnableAuthority
    public DefaultWebResponse changePassword(EditAdminPwdWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        if (!permissionHelper.roleIsSuperAdmin(sessionContext)) {
            // 不是超级管理员，则不能修改终端密码
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_PASSWORD_UPDATE_FOR_SYSADMIN);
        }

        String pwd = AesUtil.descrypt(request.getPwd(), RedLineUtil.getRealAdminRedLine());
        CbbChangePasswordDTO changePwdRequest = new CbbChangePasswordDTO();
        changePwdRequest.setPassword(pwd);
        try {
            terminalOperatorAPI.changePassword(changePwdRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_PWD_SUCCESS_LOG);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑终端管理员密码失败", e);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_PWD_FAIL_LOG, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 收集日志
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("收集日志")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "collectLog", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse collectLog(TerminalIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");

        cbbTerminalLogAPI.collectLog(request.getTerminalId());

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 获取终端收集日志状态
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "getCollectLog", method = RequestMethod.POST)
    public DefaultWebResponse getCollectLog(TerminalIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");

        CbbTerminalCollectLogStatusDTO response;
        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
        try {
            response = cbbTerminalLogAPI.getCollectLog(request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("获取终端[{}]收集日志状态失败", terminalDTO.getTerminalName(), e);
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COLLECT_LOG_FAIL_LOG, e, terminalDTO.getTerminalName(), e.getI18nMessage());
        }

        if (CbbCollectLogStateEnums.DONE == response.getState()) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COLLECT_LOG_SUCCESS_LOG, terminalDTO.getTerminalName());
        } else if (CbbCollectLogStateEnums.FAULT == response.getState()) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COLLECT_LOG_RESULT_FAIL_LOG, terminalDTO.getTerminalName());
        }

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 下载终端收集日志状态
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     * @throws IOException       io异常
     */
    @RequestMapping(value = "downloadLog", method = RequestMethod.GET)
    public DownloadWebResponse downloadLog(TerminalLogDownLoadWebRequest request) throws BusinessException, IOException {
        Assert.notNull(request, "request不能为null");

        CbbTerminalLogFileInfoDTO response = cbbTerminalLogAPI.getTerminalLogFileInfo(request.getLogName());

        // logFilePath=@String[/opt/ftp/terminal/log/20201209213115_172.20.113.184_58696cff3bdd_shine.tar.gz],
        // logFileName=@String[20201209213115_172.20.113.184_58696cff3bdd_shine.tar],
        // suffix=@String[gz]
        final File file = new File(response.getLogFilePath());
        final String fileNameWithoutSuffix = response.getLogFileName().substring(0, response.getLogFileName().lastIndexOf(SYMBOL_SPOT));
        return new DownloadWebResponse.Builder().setContentType("application/octet-stream").setName(fileNameWithoutSuffix, TAR_GZ_SUFFIX)
                .setFile(file).build();
    }

    private File getShineLog(CbbTerminalLogFileInfoDTO response) throws BusinessException {
        File shineLog = new File(response.getLogFilePath());
        if (shineLog.length() == 0) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_SHINE_ERROR_GET_SHINE_LOG_FAIL);
        }
        return shineLog;
    }

    /**
     * 离线登录设置
     *
     * @param request        请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "offlineLoginSetting", method = RequestMethod.POST)
    public DefaultWebResponse offlineLoginSetting(OfflineLoginSettingWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbOfflineLoginSettingDTO apiRequest = new CbbOfflineLoginSettingDTO(request.getOfflineAutoLocked().getDays());

        try {
            terminalOperatorAPI.idvOfflineLoginSetting(apiRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_OFFLINE_LOGIN_SETTING_SUCCESS_LOG);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException ex) {
            LOGGER.error("离线登录设置失败");
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_OFFLINE_LOGIN_SETTING_FAIL_LOG, ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_FAIL, ex, ex.getI18nMessage());
        }
    }

    /**
     * 请求离线登录设置
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/offlineLoginSetting/query", method = RequestMethod.POST)
    public DefaultWebResponse queryOfflineLoginSetting(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        String offlineLoginSetting = terminalOperatorAPI.queryOfflineLoginSetting();
        OfflineLoginSettingWebResponse offlineLoginSettingWebResponse = new OfflineLoginSettingWebResponse();
        offlineLoginSettingWebResponse.setOfflineAutoLocked(changeStringToEnum(offlineLoginSetting));
        return DefaultWebResponse.Builder.success(offlineLoginSettingWebResponse);
    }

    private OfflineAutoLockedEnum changeStringToEnum(String offlineLoginSetting) throws BusinessException {
        OfflineAutoLockedEnum offlineAutoLockedEnum = OfflineAutoLockedEnum.getByDays(Integer.valueOf(offlineLoginSetting));
        if (offlineAutoLockedEnum == null) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_OFFLINE_AUTO_LOCKED_ENUM_NOT_EXIST, offlineLoginSetting);
        }
        return offlineAutoLockedEnum;
    }

    /**
     * 清空idv终端数据盘
     *
     * @param request        请求参数
     * @param builder        入参
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量清空D盘")
    @RequestMapping(value = "diskClear", method = RequestMethod.POST)
    public CommonWebResponse<BatchTaskSubmitResult> idvDiskClear(TerminalIdArrWebRequest request,
                                                                 BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(builder, "builder不能为null");
        String[] terminalIdArr = request.getIdArr();
        userTerminalMgmtAPI.verifyTerminalIdExist(terminalIdArr);
        Assert.notNull(terminalIdArr, "terminalIdArr不能为null");

        Map<UUID, String> idMap = new HashMap<>();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(terminalIdArr).distinct().map(terminalId -> {
            UUID itemId = UUID.randomUUID();
            idMap.put(itemId, terminalId);
            return DefaultBatchTaskItem.builder().itemId(itemId).itemName(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_ITEM_NAME, new String[]{})
                    .build();
        }).iterator();
        DiskClearBatchTaskHandler handler = new DiskClearBatchTaskHandler(iterator, terminalOperatorAPI,
                auditLogAPI, idMap, cbbTerminalOperatorAPI);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_ITEM_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_ITEM_DESC).registerHandler(handler).start();
        return CommonWebResponse.success(result);

    }


    /**
     * vdi终端初始化
     *
     * @param request        请求参数
     * @param builder        批量任务创建对象
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("vdi终端初始化")
    @ApiVersions({@ApiVersion(value = Version.V3_2_221, descriptions = {"分级分权-区分权限，VDI终端初始化"})})
    @RequestMapping(value = "/vdi/init", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> vdiInit(TerminalIdArrWebRequest request,
                                                            BatchTaskBuilder builder) throws BusinessException {

        Assert.notNull(request, "TerminalIdWebRequest不能为null");
        Assert.notNull(builder, "builder不能为null");

        String[] terminalIdArr = request.getIdArr();
        validVdiTerminalInit(terminalIdArr);

        Map<UUID, String> idMap = new HashMap<>();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(terminalIdArr).distinct().map(terminalId -> {
            UUID itemId = UUID.randomUUID();
            idMap.put(itemId, terminalId);
            return DefaultBatchTaskItem.builder().itemId(itemId).itemName(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_ITEM_NAME, new String[]{})
                    .build();
        }).iterator();
        InitIdvBatchTaskHandler handler = new InitIdvBatchTaskHandler(iterator, idMap, userTerminalMgmtAPI, auditLogAPI, false);
        handler.setCbbTerminalOperatorAPI(cbbTerminalOperatorAPI);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_ITEM_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_ITEM_DESC).registerHandler(handler).start();

        return CommonWebResponse.success(result);
    }

    private void validVdiTerminalInit(String[] terminalIdArr) throws BusinessException {
        Assert.notEmpty(terminalIdArr, "terminalIdArr must not be null");

        List<CbbTerminalBasicInfoDTO> terminalBasicInfoDTOList = cbbTerminalOperatorAPI.findTerminalListByIdList(Arrays.asList(terminalIdArr));

        if (terminalIdArr.length != terminalBasicInfoDTOList.size()) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_INIT_HAS_EXIST_BE_DELETE);
        }

        if (terminalBasicInfoDTOList.stream().anyMatch(item -> item.getState() != CbbTerminalStateEnums.ONLINE)) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_INIT_HAS_EXIST_TERMINAL_NOT_ONLINE);
        }
    }
}
