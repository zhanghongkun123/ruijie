package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalWakeUpAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ShineConfigFullSystemDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.TerminalIdMappingUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.OfflineLoginSettingWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.TerminalNetworkDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbGetNetworkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCollectLogStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalStartMode;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;

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
@RequestMapping("/rco/terminal/idv")
@EnableCustomValidate(enable = false)
public class IDVTerminalOperateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDVTerminalOperateController.class);

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

    private static final String TAR_GZ_SUFFIX = "tar.gz";

    private static final String SYMBOL_SPOT = ".";

    @Autowired
    private TerminalWakeUpAPI terminalWakeUpAPI;

    /**
     * 关闭终端
     *
     * @param request        终端id请求参数对象
     * @param builder        批量任务创建对象
     * @return 返回成功或失败
     * @throws BusinessException 业务异常
     */
    @ApiOperation("关闭终端")
    @RequestMapping(value = "shutdown", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，关闭终端"})})

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
    @ApiOperation("重启")
    @RequestMapping(value = "restart", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，重启"})})

    @EnableAuthority
    public DefaultWebResponse restartTerminal(TerminalIdArrWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
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
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("修改终端管理员密码")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，修改终端管理员密码"})})
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public DefaultWebResponse changePassword(EditAdminPwdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");

        String pwd;
        try {
            pwd = AesUtil.descrypt(request.getPwd(), RedLineUtil.getRealAdminRedLine());
        } catch (Exception e) {
            LOGGER.error("解密失败，失败原因：", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }
        CbbChangePasswordDTO changePwdRequest = new CbbChangePasswordDTO();
        changePwdRequest.setPassword(pwd);
        try {
            terminalOperatorAPI.changePassword(changePwdRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_PWD_SUCCESS_LOG);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑终端管理员密码失败", e);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_PWD_FAIL_LOG, e, e.getI18nMessage());
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
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，收集日志"})})
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
    @ApiOperation("获取终端收集日志状态")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，获取终端收集日志状态"})})
    @RequestMapping(value = "getCollectLog", method = RequestMethod.POST)
    public DefaultWebResponse getCollectLog(TerminalIdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为null");

        CbbTerminalCollectLogStatusDTO response;
        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
        try {
            response = cbbTerminalLogAPI.getCollectLog(request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("获取终端[{}]收集日志状态失败", terminalDTO.getTerminalName(), e);
            throw new BusinessException(TerminalBusinessKey.RCDC_IDV_TERMINAL_COLLECT_LOG_FAIL_LOG, e, terminalDTO.getTerminalName(),
                    e.getI18nMessage());
        }

        if (CbbCollectLogStateEnums.DONE == response.getState()) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_IDV_TERMINAL_COLLECT_LOG_SUCCESS_LOG, terminalDTO.getTerminalName());
        } else if (CbbCollectLogStateEnums.FAULT == response.getState()) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_IDV_TERMINAL_COLLECT_LOG_RESULT_FAIL_LOG, terminalDTO.getTerminalName());
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
    @ApiOperation("下载终端收集日志状态")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，下载终端收集日志状态"})})
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
    @ApiOperation("离线登录设置")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，离线登录设置"})})
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
    @ApiOperation("请求离线登录设置")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，请求离线登录设置"})})
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
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，批量清空D盘"})})
    @RequestMapping(value = "diskClear", method = RequestMethod.POST)

    @EnableAuthority
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
        DiskClearBatchTaskHandler handler =
                new DiskClearBatchTaskHandler(iterator, terminalOperatorAPI, auditLogAPI, idMap, cbbTerminalOperatorAPI);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_ITEM_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_CLEAR_DISK_ITEM_DESC).registerHandler(handler).start();
        return CommonWebResponse.success(result);

    }

    /**
     * idv终端初始化
     *
     * @param request        请求参数
     * @param builder        批量任务创建对象
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("IDV初始化")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，IDV初始化"})})
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> idvInit(TerminalIdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {

        Assert.notNull(request, "TerminalIdWebRequest不能为null");
        Assert.notNull(builder, "builder不能为null");

        String[] terminalIdArr = request.getIdArr();
        userTerminalMgmtAPI.verifyTerminalIdExist(terminalIdArr);
        Assert.notNull(terminalIdArr, "terminalIdArr不能为null");

        Map<UUID, String> idMap = new HashMap<>();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(terminalIdArr).distinct().map(terminalId -> {
            UUID itemId = UUID.randomUUID();
            idMap.put(itemId, terminalId);
            return DefaultBatchTaskItem.builder().itemId(itemId).itemName(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_ITEM_NAME, new String[]{})
                    .build();
        }).iterator();
        InitIdvBatchTaskHandler handler = new InitIdvBatchTaskHandler(iterator, idMap, userTerminalMgmtAPI, auditLogAPI,
                Boolean.parseBoolean(String.valueOf(request.getRetainImage())));
        handler.setCbbTerminalOperatorAPI(cbbTerminalOperatorAPI);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_ITEM_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_ITEM_DESC).registerHandler(handler).start();

        return CommonWebResponse.success(result);
    }


    /**
     * 开启系统盘自动扩容
     *
     * @param request 请求
     * @param builder 批处理Builder
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("开启系统盘自动扩容")
    @RequestMapping(value = "fullSystemDisk/edit", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editTerminalFullSystemDisk(EditTerminalFullSystemDiskRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(request.getIdArr());
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_ITEM_NAME, new String[]{}).build()).iterator();
        EditFullSystemDiskHandler handler = new EditFullSystemDiskHandler(idMap, iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setUserTerminalMgmtAPI(userTerminalMgmtAPI);
        // 终端操作API类
        handler.setCbbTerminalOperatorAPI(cbbTerminalOperatorAPI);
        handler.setConfigFullSystemDiskDTO(new ShineConfigFullSystemDiskDTO(request.getEnableFullSystemDisk(), request.getForceDeleteLocalDisk()));
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_ITEM_DESC).registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 获取终端详细网络配置
     *
     * @param request 终端id请求
     * @return resp
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取终端网络配置信息")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，获取终端网络配置信息"})})
    @RequestMapping(value = "/network/detail")
    public DefaultWebResponse getTerminalNetworkDetail(TerminalNetworkDetailWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getId());
        TerminalNetworkDetailVO terminalNetworkDetailVO = new TerminalNetworkDetailVO();
        terminalNetworkDetailVO.setIpAddr(cbbTerminalBasicInfoDTO.getIp());
        terminalNetworkDetailVO.setMask(cbbTerminalBasicInfoDTO.getSubnetMask());
        terminalNetworkDetailVO.setGateway(cbbTerminalBasicInfoDTO.getGateway());
        terminalNetworkDetailVO.setDns(cbbTerminalBasicInfoDTO.getMainDns());
        terminalNetworkDetailVO.setDnsBack(cbbTerminalBasicInfoDTO.getSecondDns());
        CbbGetNetworkModeEnums ipMode = cbbTerminalBasicInfoDTO.getGetIpMode();
        terminalNetworkDetailVO.setAutoDhcp(ipMode == CbbGetNetworkModeEnums.AUTO);
        CbbGetNetworkModeEnums dnsMode = cbbTerminalBasicInfoDTO.getGetDnsMode();
        terminalNetworkDetailVO.setAutoDns(dnsMode == CbbGetNetworkModeEnums.AUTO);
        return DefaultWebResponse.Builder.success(terminalNetworkDetailVO);
    }

    /**
     * 单台编辑IDV终端网络配置
     *
     * @param request        请求参数
     * @param builder        批量任务创建对象
     * @return resp
     * @throws BusinessException 业务异常
     */
    @ApiOperation("单台编辑IDV终端网络配置")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，单台编辑终端网络配置"})})
    @RequestMapping(value = "editNetwork", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editIDVTerminalNetwork(EditTerminalNetworkWebRequest request,
                                                                           BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "editTerminalNetworkWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        // 判断是否IDV或TCI终端
        String terminalId = request.getId();
        Boolean isTerminalIDVOrTCI = isTerminalIDVOrTCI(terminalId);
        if (!isTerminalIDVOrTCI) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_NOT_IDV_OR_TCI, terminalId);
        }
        DefaultBatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_ITEM_NAME)).build();
        EditTerminalNetworkBatchTaskHandler handler = new EditTerminalNetworkBatchTaskHandler(request, batchTaskItem, auditLogAPI);
        handler.setCbbTerminalOperatorAPI(cbbTerminalOperatorAPI);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_ITEM_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_EDIT_NETWORK_ITEM_DESC).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 设置终端开机模式
     * @param request 请求
     * @param builder 任务
     * @return 结果
     * @throws BusinessException 异常
     */
    @ApiOperation("设置终端开机模式")
    @RequestMapping(value = "configStartMode")
    @EnableAuthority
    public DefaultWebResponse configVOIStartMode(ConfigVOIStartModeRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request cant be null");
        Assert.notNull(builder, "builder cant be null");

        final Iterator<ConfigStartModeBatchTaskItem> iterator = Stream.of(request.getIdArr()).map(id -> {
            ConfigStartModeBatchTaskItem item = new ConfigStartModeBatchTaskItem();
            item.setItemID(UUID.randomUUID());
            item.setItermName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE));
            item.setTerminalId(id);
            return item;
        }).iterator();

        ConfigStartModeBatchTaskHandler handler = new ConfigStartModeBatchTaskHandler(iterator, auditLogAPI,
                userTerminalMgmtAPI, cbbTerminalOperatorAPI);
        handler.setTerminalStartMode(request.getTerminalStartMode());
        String i18StartMode = request.getTerminalStartMode() == CbbTerminalStartMode.TC
                ? LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_TERMINAL_FAST_MODE)
                : LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_TERMINAL_COMPATIBLE_MODE);
        handler.setStartMode(i18StartMode);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_START_MODE).registerHandler(handler).enableParallel().start();
        return DefaultWebResponse.Builder.success(result);

    }

    /**
     * 批量编辑IDV终端IP
     *
     * @param request        请求参数
     * @param builder        批量任务创建对象
     * @return resp
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量编辑IDV终端IP")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，批量编辑终端IP"})})
    @RequestMapping(value = "editIp", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editIDVTerminalIp(EditTerminalIpWebRequest request,
                                                                      BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "editTerminalIpWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        String[] idArr = request.getIdArr();// 终端idArr数组
        for (String terminalId : idArr) {
            Boolean isTerminalIDVOrTCI = isTerminalIDVOrTCI(terminalId);
            if (!isTerminalIDVOrTCI) {
                throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_BATCH_NOT_IDV_OR_TCI);
            }
        }

        BatchTaskSubmitResult result = execEditTerminalIpBatchTaskItem(request, builder);
        return CommonWebResponse.success(result);
    }

    private Boolean isTerminalIDVOrTCI(String terminalId) throws BusinessException {
        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
        CbbTerminalPlatformEnums terminalType = terminalDTO.getPlatform();
        return CbbTerminalPlatformEnums.IDV == terminalType || CbbTerminalPlatformEnums.VOI == terminalType;
    }

    /**
     * 全部编辑IDV终端IP
     *
     * @param request        请求参数
     * @param builder        批量任务创建对象
     * @return resp
     * @throws BusinessException 业务异常
     */
    @ApiOperation("全部编辑IDV终端IP")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0, descriptions = {"分级分权-区分权限，全部编辑终端IP"})})
    @RequestMapping(value = "editIpAll", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> editIDVTerminalIpAll(EditTerminalIpAllWebRequest request,
                                                                         BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "editTerminalIpAllWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        // 获取终端idArr
        UUID terminalGroupId = request.getTerminalGroupId();
        List<String> onlineTerminalIdList = cbbTerminalOperatorAPI.getOnlineGroupTerminalIdIDVTCI(terminalGroupId);
        if (CollectionUtils.isEmpty(onlineTerminalIdList)) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_NO_OPERATE_TERMINAL);
        }
        String[] idArr = new String[onlineTerminalIdList.size()];
        onlineTerminalIdList.toArray(idArr);

        EditTerminalIpWebRequest editTerminalIpWebRequest = new EditTerminalIpWebRequest();
        editTerminalIpWebRequest.setIdArr(idArr);
        editTerminalIpWebRequest.setAutoDhcp(request.getAutoDhcp());
        editTerminalIpWebRequest.setMask(request.getMask());
        editTerminalIpWebRequest.setGateway(request.getGateway());

        BatchTaskSubmitResult result = execEditTerminalIpBatchTaskItem(editTerminalIpWebRequest, builder);
        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult execEditTerminalIpBatchTaskItem(EditTerminalIpWebRequest request,
            BatchTaskBuilder builder) throws BusinessException {
        final Iterator<EditTerminalIpBatchTaskItem> iterator = Stream.of(request.getIdArr()).map(id -> {
            EditTerminalIpBatchTaskItem batchTaskItem = new EditTerminalIpBatchTaskItem();
            batchTaskItem.setItemId(UUID.randomUUID());
            batchTaskItem.setItemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_ITEM_NAME));
            batchTaskItem.setTerminalId(id);
            batchTaskItem.setAutoDhcp(request.getAutoDhcp());
            batchTaskItem.setMask(request.getMask());
            batchTaskItem.setGateway(request.getGateway());
            return batchTaskItem;
        }).iterator();

        EditTerminalIpBatchTaskHandler handler = new EditTerminalIpBatchTaskHandler(iterator, auditLogAPI);
        handler.setCbbTerminalOperatorAPI(cbbTerminalOperatorAPI);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_ITEM_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_EDIT_IP_ITEM_DESC).registerHandler(handler).start();
        return result;
    }

    /**
     * 远程唤醒终端
     *
     * @param request 请求
     * @param builder 批量任务创建对象
     * @return 唤醒结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("终端开机")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"终端开机"})})
    @RequestMapping(value = "wakeUp", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse wakeUpTerminal(TerminalIdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(request.getIdArr());
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_ITEM_NAME, new String[]{}).build()).iterator();
        WakeUpTerminalHandler handler = new WakeUpTerminalHandler(idMap, iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbTerminalOperatorAPI(terminalOperatorAPI);
        handler.setTerminalWakeUpAPI(terminalWakeUpAPI);
        String taskDesc = request.getIdArr().length > 1 ? TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_BATCH_ITEM_DESC
                : TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_SINGLE_ITEM_DESC;
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_TASK_NAME).setTaskDesc(taskDesc)
                .registerHandler(handler).enableParallel().start();

        return DefaultWebResponse.Builder.success(result);
    }

}
