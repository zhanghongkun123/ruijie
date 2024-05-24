package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;


import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyThirdPartyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateComputerRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.ComputerBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ImportComputerBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/20
 *
 * @author wjp
 */
public class ImportComputerBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportComputerBatchTaskHandler.class);


    private BaseAuditLogAPI auditLogAPI;

    private ComputerBusinessAPI computerBusinessAPI;

    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    private IacUserMgmtAPI cbbUserAPI;

    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    /**
     * 分组层级分隔符
     */
    private static final String GROUP_SPILT = "/";


    public ImportComputerBatchTaskHandler(ImportComputerBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.computerBusinessAPI = request.getComputerBusinessAPI();
        this.cbbTerminalGroupMgmtAPI = request.getCbbTerminalGroupMgmtAPI();
        this.cbbUserAPI = request.getCbbUserAPI();
        this.cbbThirdPartyDeskStrategyMgmtAPI = request.getCbbThirdPartyDeskStrategyMgmtAPI();

    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        CreateComputerBatchTaskItem item = (CreateComputerBatchTaskItem) batchTaskItem;
        ImportComputerDTO dto = item.getImportComputerDTO();
        try {
            // 导入数据
            importComputer(dto);
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_SUCCESS, dto.getIp());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_SUCCESS).msgArgs(new String[] {dto.getIp()}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_FAIL, dto.getIp(), e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_FAIL).msgArgs(new String[] {dto.getIp(), e.getI18nMessage()}).build();
        }
    }

    private void importComputer(ImportComputerDTO dto) throws BusinessException {
        //检测IP
        CreateComputerRequest createComputerRequest = checkData(dto);
        UUID groupId;
        if (StringUtils.isBlank(dto.getGroupNames())) {
            // 分组为空返回默认未分组id
            groupId = CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID;
        } else {
            // 创建用户分组
            groupId = cbbTerminalGroupMgmtAPI.importGroup(dto.getGroupNames().split(GROUP_SPILT));
        }
        createComputerRequest.setIp(dto.getIp());
        createComputerRequest.setName(dto.getName());
        createComputerRequest.setTerminalGroupId(groupId);
        createComputerRequest.setRemark(dto.getRemark());
        createComputerRequest.setState(ComputerStateEnum.WAIT_TUBE);
        computerBusinessAPI.saveComputer(createComputerRequest);

    }

    private CreateComputerRequest checkData(ImportComputerDTO dto) throws BusinessException {
        ComputerInfoResponse computerInfoResponse = computerBusinessAPI.getComputerInfoByIp(dto.getIp());
        CreateComputerRequest createComputerRequest = new CreateComputerRequest();
        if (computerInfoResponse != null) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_CREATE_TERMINAL_IP_EXIST);
        }
        //检测策略
        CbbDeskStrategyThirdPartyDTO deskStrategyThirdPartyDTO = cbbThirdPartyDeskStrategyMgmtAPI.
                getDeskStrategyThirdPartyByName(dto.getDeskStrategy());
        if (deskStrategyThirdPartyDTO == null) {
            throw new BusinessException(ComputerBusinessKey.RCDC_RCO_COMPUTER_IMPORT_THIRD_PARTY_NOT_EXIT);
        }
        //检测用户
        IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(dto.getUserName());
        if (userDetailDTO == null) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_INVALID_RECOVER_NOT_EXIT);
        } else {
            createComputerRequest.setUserId(userDetailDTO.getId());
        }
        return createComputerRequest;
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, ComputerBusinessKey.RCDC_RCO_IMPORT_COMPUTER_BATCH_TASK_SUCCESS);
    }
}
