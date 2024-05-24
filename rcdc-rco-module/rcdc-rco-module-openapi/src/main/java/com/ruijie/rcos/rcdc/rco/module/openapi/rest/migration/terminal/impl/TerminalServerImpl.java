package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.impl;

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_RCO_TERMINAL_EXISTS;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_RCO_TERMINAL_USER_BINDED;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGlobalVmMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.GlobalVmMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.TerminalVmModeTypeEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IDVCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserTerminalRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.mtool.dto.SyncUpgradeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.MtoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.TerminalServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalGroupInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.TerminalTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.TerminalTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.common.AbstractServerImpl;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.terminal.ImportTerminalService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@Service
public class TerminalServerImpl extends AbstractServerImpl implements TerminalServer {

    private final Logger logger = LoggerFactory.getLogger(TerminalServerImpl.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI;

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private ImportTerminalService importTerminalService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Autowired
    private DesktopAPI desktopAPI;

    // 业务锁前缀-终端组
    private final static String PREFIX_LOCK_GROUP = "TERMINALSERVERIMPL_GROUP";

    // 业务锁前缀-终端
    private final static String PREFIX_LOCK_NAME = "TERMINALSERVERIMPL_NAME";

    /**
     * 公用终端无用户名,使用 "--" 占位
     */
    private static final String USER_NAME = "--";

    // 终端开始处理流程
    private static final Integer STATUS_STARTING = 0;

    // 终端流程成功处理
    private static final Integer STATUS_SUCCESS = 100;

    // 通知 Shine 重试迁移流程
    private static final String RETRY_OLD_VERSION_UPGRADE = "retry_old_version_upgrade";

    @Override
    public void importTerminalGroup(ImportTerminalGroupRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");
        invoke(request, PREFIX_LOCK_GROUP + request.getId(), () -> doImportTerminalGroup(request));
    }

    @Override
    public void importTerminal(ImportTerminalRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");
        invoke(request, PREFIX_LOCK_NAME + request.getId(), () -> {

            try {
                doImportTerminal(request);
            } catch (Exception ex) {
                String remark = (ex instanceof BusinessException) ? ((BusinessException) ex).getKey() : StringUtils.EMPTY;
                saveTerminalMapping(request.getTerminalMac(), RestErrorCodeMapping.convert(ex).getKey(), remark);
                throw ex;
            }

            saveTerminalMapping(request.getTerminalMac(), RestErrorCode.RCDC_OPEN_API_SUCCESS, "");
        });
    }

    @Override
    public TerminalTaskResponse selectTerminalTask(TerminalTaskRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");

        try {
            // 判断终端数据是否导入升级流程
            SystemBusinessMappingDTO systemBusinessMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                    SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL, request.getTerminalMac());
            if (systemBusinessMapping == null) {
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_UNKNOWN);
            }

            // 终端初始化判断
            systemBusinessMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                    SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_STAT, request.getTerminalMac());
            if (systemBusinessMapping == null) {
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_UNKNOWN);
            }

            // 结果状态转换
            SyncUpgradeDTO syncUpgradeDTO = JSON.parseObject(systemBusinessMapping.getContext(), SyncUpgradeDTO.class);

            if (syncUpgradeDTO.getStatus() == SyncUpgradeDTO.SyncUpgradeStatus.STATUS_IMPORT && syncUpgradeDTO.getShineCode() != 0) {
                // 新平台终端数据导入失败
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_IMPORT_FAILED, syncUpgradeDTO.getShineCode());
            } else if (syncUpgradeDTO.getStatus() == SyncUpgradeDTO.SyncUpgradeStatus.STATUS_IMPORT
                    && Objects.equals(syncUpgradeDTO.getShineCode(), STATUS_STARTING)) {
                // 新平台终端数据导入成功
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_IMPORTED, syncUpgradeDTO.getShineCode());
            } else if (syncUpgradeDTO.getStatus() == SyncUpgradeDTO.SyncUpgradeStatus.STATUS_INIT) {
                // 新平台终端连接过 RCDC，但客户端未配置成功
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_CONNECTED);
            } else if (syncUpgradeDTO.getStatus() == SyncUpgradeDTO.SyncUpgradeStatus.STATUS_DEAL
                    && Objects.equals(syncUpgradeDTO.getShineCode(), STATUS_STARTING)) {
                // 新平台终端连接过 RCDC，并且客户端还在处理中
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_CONNECTED);
            } else if (syncUpgradeDTO.getStatus() == SyncUpgradeDTO.SyncUpgradeStatus.STATUS_DEAL
                    && !Objects.equals(syncUpgradeDTO.getShineCode(), STATUS_SUCCESS)) {
                // 新平台终端连接过 RCDC，并且客户端配置失败，回复终端上传的状态码
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_FAILED, syncUpgradeDTO.getShineCode());
            } else {
                // 新平台终端连接过 RCDC，并且客户端配置成功
                return new TerminalTaskResponse(TerminalTaskResponse.TaskStatus.STATUS_SUCCESS);
            }

        } catch (Exception ex) {
            logger.error("业务查询报错", ex);
            throw new BusinessException(RestErrorCode.RCDC_CODE_SYSTEM_CONFIG_ERROR, ex);
        }
    }

    // 导入终端业务逻辑
    private void doImportTerminal(ImportTerminalRequest request) throws BusinessException {

        try {
            // 终端导入校验 & 请求转换
            ImportTerminalInfoDTO infoDTO = importTerminalService.validateAndConvertTerminalRequest(request);

            // 创建终端实体
            importTerminalService.saveTerminalInfo(request);

            // 修改终端分组
            logger.info("导入终端 [{}] 修改分组 [{}]", request.getTerminalMac(), infoDTO.getNewTerminalGroupId());
            importTerminalService.modifyTerminalGroup(request.getTerminalMac(), request.getTerminalName(), infoDTO.getNewTerminalGroupId());

            if ((request.getIdvTerminalMode() == IdvTerminalModeEnums.PERSONAL && request.getUserId() != null)
                    || request.getIdvTerminalMode() == IdvTerminalModeEnums.PUBLIC) {
                // 处理云桌面流程
                dealDesktopData(request, infoDTO);
            }

            // 记录审计日志
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_TERMINAL_ADD_IDV_TERMINAL_SUCCESS_LOG, request.getTerminalMac().toUpperCase());

        } catch (BusinessException ex) {

            // 处理桌面导入重入的场景
            if (BusinessException.isSameBusinessException(ex, RCDC_RCO_TERMINAL_USER_BINDED)
                    || BusinessException.isSameBusinessException(ex, RCDC_RCO_TERMINAL_EXISTS)) {
                logger.info("终端 [{}] 属于旧平台的终端导入重入", request.getTerminalMac());
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_TERMINAL_ADD_IDV_TERMINAL_SUCCESS_LOG, ex,
                        request.getTerminalMac().toUpperCase());
                // 通知 shine 重试迁移
                notifyShineRetryUpgrade(request.getTerminalMac());
                return;
            }

            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_TERMINAL_ADD_IDV_TERMINAL_FAILED_LOG, ex,
                    request.getTerminalMac().toUpperCase(), ex.getI18nMessage());
            throw ex;
        }

        // 如果导入G3+IDV+WIN10终端则更新对应的全局虚机运行策略
        updateGlobalVmModeForG3();

        // 通知 shine 重试迁移
        notifyShineRetryUpgrade(request.getTerminalMac());

        // 保存终端映射
        SystemBusinessMappingDTO systemBusinessMappingDTO = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL, request.getTerminalMac());
        if (systemBusinessMappingDTO == null) {
            systemBusinessMappingDTO = new SystemBusinessMappingDTO();
            systemBusinessMappingDTO.setSystemType(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL);
            systemBusinessMappingDTO.setBusinessType(SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL);
            systemBusinessMappingDTO.setSrcId(request.getTerminalMac());
        }
        systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMappingDTO);
    }

    /**
     * 通知 shine 重试迁移
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void notifyShineRetryUpgrade(String terminalMac) {

        try {
            CbbShineMessageRequest request = CbbShineMessageRequest.create(RETRY_OLD_VERSION_UPGRADE, terminalMac);
            request.setContent(new JSONObject());
            cbbTranspondMessageHandlerAPI.asyncRequest(request, new CbbTerminalCallback() {
                @Override
                public void success(String terminalMac, CbbShineMessageResponse msg) {
                    Assert.notNull(terminalMac, "terminalMac cannot be null");
                    Assert.notNull(msg, "msg cannot be null");
                    logger.info("terminalMac [{}] 通知 shine 重试迁移成功", terminalMac);
                }

                @Override
                public void timeout(String terminalMac) {
                    Assert.notNull(terminalMac, "terminalId cannot be null!");
                    logger.warn("terminalMac [{}] 通知 shine 重试迁移超时", terminalMac);
                }
            });
        } catch (BusinessException ex) {
            logger.error(String.format("terminalMac [%s] 通知 shine 重试迁移失败", terminalMac), ex);
        }
    }

    /**
     * 处理终端数据
     *
     * @param request 外部请求
     * @param infoDTO 内部数据集合
     * @throws BusinessException 业务异常
     */
    private void dealDesktopData(ImportTerminalRequest request, ImportTerminalInfoDTO infoDTO) throws BusinessException {

        if (infoDTO.getNewImageId() == null || infoDTO.getNewStrategyId() == null) {
            logger.info("导入终端 [{}] 对应特性未开启，无法创建云桌面", request.getTerminalMac());
            return;
        }

        // 创建终端桌面
        IDVCloudDesktopDTO idvCloudDesktopDTO = new IDVCloudDesktopDTO();
        idvCloudDesktopDTO.setTerminalId(request.getTerminalMac());
        idvCloudDesktopDTO.setIdvTerminalMode(request.getIdvTerminalMode());
        idvCloudDesktopDTO.setTerminalGroupId(infoDTO.getNewTerminalGroupId());
        idvCloudDesktopDTO.setStrategyId(infoDTO.getNewStrategyId());
        idvCloudDesktopDTO.setImageId(infoDTO.getNewImageId());
        idvCloudDesktopDTO.setUserId(infoDTO.getNewUserId());
        idvCloudDesktopDTO.setUserName(request.getUserName());
        idvCloudDesktopDTO.setTerminalMac(request.getTerminalMac());
        CreateDesktopResponse createDesktopResponse = userDesktopMgmtAPI.createIDV(idvCloudDesktopDTO);
        logger.info("导入终端 [{}] 创建云桌面 [{}] 成功", request.getTerminalMac(), createDesktopResponse.getId());

        //更新IDV桌面的计算机名
        if (StringUtils.isNotBlank(request.getComputerName())) {
            cbbIDVDeskMgmtAPI.updateIDVDeskComputerName(createDesktopResponse.getId(), request.getComputerName());
            logger.info("导入终端 [{}] 修改云桌面 [{}] 计算机名 [{}] 成功", request.getTerminalMac(),
                    createDesktopResponse.getId(), request.getComputerName());
        }

        // 记录审计日志
        // addSystemRecordLog(request, createDesktopResponse);

        // 编辑终端设置
        UserTerminalRequest userTerminalRequest = new UserTerminalRequest();
        userTerminalRequest.setDeskId(createDesktopResponse.getId());
        userTerminalRequest.setUserId(infoDTO.getNewUserId());
        userTerminalRequest.setUserName(request.getUserName());
        userTerminalRequest.setTerminalId(request.getTerminalMac());
        userTerminalRequest.setIdvTerminalMode(request.getIdvTerminalMode());
        userTerminalMgmtAPI.editTerminalSetting(userTerminalRequest);
        logger.info("导入终端 [{}] 编辑终端设置 [{}] 成功", request.getTerminalMac(), createDesktopResponse.getId());


        // 通知 UWS 桌面新增
        uwsDockingAPI.notifyDesktopAdd(createDesktopResponse.getId());
    }

    // 创建云桌面成功记录审计日志
    @SuppressWarnings("unused")
    private void addSystemRecordLog(ImportTerminalRequest request, CreateDesktopResponse createDesktopResponse) {

        final UUID deskId = createDesktopResponse.getId();
        final String desktopName = createDesktopResponse.getDesktopName();
        try {
            DesktopOpLogDTO desktopOpLogRequest = new DesktopOpLogDTO();
            desktopOpLogRequest.setDesktopId(deskId);
            desktopOpLogRequest.setDesktopName(desktopName);
            desktopOpLogRequest.setSourceIp(request.getTerminalIp());
            // 获取云桌面名称
            String name = request.getIdvTerminalMode() == IdvTerminalModeEnums.PUBLIC ? USER_NAME : request.getUserName();
            desktopOpLogRequest.setDetail(LocaleI18nResolver.resolve(MtoolBusinessKey.RCDC_OPENAPI_CLOUDDESKTOP_CREATE_SUC_LOG, name, desktopName));
            desktopOpLogRequest.setEventName(DesktopOpEvent.CREATE);
            desktopOpLogRequest.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);
            desktopOpLogRequest.setTerminalId(request.getTerminalMac());
            desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
        } catch (Exception ex) {
            logger.error("记录创建IDV云桌面[id:{}]日志出现异常:{}", deskId, ex);
        }
    }

    // 增加流程记录
    private void saveTerminalMapping(String terminalId, String code, String remark) {

        SystemBusinessMappingDTO systemBusinessMappingDTO = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_STAT, terminalId);
        if (systemBusinessMappingDTO == null) {
            systemBusinessMappingDTO = new SystemBusinessMappingDTO();
            systemBusinessMappingDTO.setSystemType(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL);
            systemBusinessMappingDTO.setBusinessType(SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_STAT);
            systemBusinessMappingDTO.setSrcId(terminalId);
        }

        SyncUpgradeDTO syncUpgradeDTO = new SyncUpgradeDTO();
        syncUpgradeDTO.setStatus(SyncUpgradeDTO.SyncUpgradeStatus.STATUS_IMPORT);
        syncUpgradeDTO.setShineCode(Integer.valueOf(code));
        syncUpgradeDTO.setRemark(remark);
        systemBusinessMappingDTO.setContext(JSON.toJSONString(syncUpgradeDTO));
        systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMappingDTO);
    }

    // 导入终端组业务逻辑
    private void doImportTerminalGroup(ImportTerminalGroupRequest request) throws BusinessException {

        try {
            // 终端组导入校验 & 请求转换
            ImportTerminalGroupInfoDTO groupInfoDTO = importTerminalService.validateAndConvertTerminalGroupRequest(request);

            // 调用终端组导入接口
            IdvCreateTerminalGroupRequest createTerminalGroupRequest = importTerminalService.buildTerminalGroupRequest(request);

            if (groupInfoDTO.getTerminalGroupId() != null) {
                logger.info("终端组 [{}] 已存在，重新编辑", groupInfoDTO.getTerminalGroupId());
                IdvEditTerminalGroupRequest editTerminalGroupRequest = new IdvEditTerminalGroupRequest();
                editTerminalGroupRequest.setId(groupInfoDTO.getTerminalGroupId());
                BeanUtils.copyProperties(createTerminalGroupRequest, editTerminalGroupRequest);
                userTerminalGroupMgmtAPI.editIdvTerminalGroup(editTerminalGroupRequest);
            } else {
                logger.info("终端组 [{}] 不存在，重新创建", createTerminalGroupRequest.getGroupName());
                UUID terminalGroupId = userTerminalGroupMgmtAPI.createIdvTerminalGroup(createTerminalGroupRequest);
                groupInfoDTO.setTerminalGroupId(terminalGroupId);
            }

            // 保存映射关系
            SystemBusinessMappingDTO systemBusinessMappingDTO = groupInfoDTO.getSystemBusinessMappingDTO();
            if (systemBusinessMappingDTO == null) {
                systemBusinessMappingDTO = new SystemBusinessMappingDTO();
                systemBusinessMappingDTO.setSystemType(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL);
                systemBusinessMappingDTO.setBusinessType(SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_GROUP);
                systemBusinessMappingDTO.setSrcId(request.getId().toString());
            }
            systemBusinessMappingDTO.setDestId(groupInfoDTO.getTerminalGroupId().toString());
            systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMappingDTO);

            // 更新白名单的应用配置
            importTerminalService.dealWifiWriteList(groupInfoDTO.getTerminalGroupId(), request.getNeedApplyToSubgroup(),
                    createTerminalGroupRequest.getWifiWhitelistDTOList());

            // 记录审计日志
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_SUCCESS_LOG, request.getName());

        } catch (BusinessException ex) {
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_TERMINAL_GROUP_ADD_IDV_TERMINAL_GROUP_FAIL_LOG, ex,
                    request.getName(), ex.getI18nMessage());
            throw ex;
        }
    }

    private void updateGlobalVmModeForG3() {
        boolean existG3IdvWin10 = desktopAPI.existDesktopByTerminalGlobalVmMode(TerminalVmModeTypeEnum.G3_IDV_WIN10);
        CbbGlobalVmMode g3GlobalVmMode = cbbGlobalStrategyMgmtAPI.getGlobalVmModeByTerminalVmModeType(TerminalVmModeTypeEnum.G3_IDV_WIN10);
        // 如果原来就是模拟就不需要更新
        if (existG3IdvWin10 && GlobalVmMode.EMULATION == g3GlobalVmMode.getVmMode()) {
            logger.info("存在G3+IDV+WIN10终端，且全局G3_IDV_WIN10桌面运行策略为:EMULATION, 进行策略更新");
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_TERMINAL_UPDATE_GLOBAL_VM_MODE_FOR_G3_SUCCESS_LOG,
                    TerminalVmModeTypeEnum.G3_IDV_WIN10.toString());
            cbbGlobalStrategyMgmtAPI.updateGlobalVmMode(TerminalVmModeTypeEnum.G3_IDV_WIN10, GlobalVmMode.PASSTHROUGH);
        }
    }
}
