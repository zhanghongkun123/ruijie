package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolComputerMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolThirdPartyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolThirdPartyBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.CreatePoolComputerDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.CreateComputerDesktopBatchTaskHandlerRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/20 15:27
 *
 * @author yanlin
 */
public class CreatePoolThirdPartyDesktopBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePoolThirdPartyDesktopBatchTaskHandler.class);


    private BaseAuditLogAPI auditLogAPI;

    private DesktopPoolConfigDTO desktopPoolConfigDTO;

    private DesktopPoolBasicDTO desktopPoolDTO;


    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private Boolean hasSecondAdd;

    private DesktopPoolComputerMgmtAPI desktopPoolComputerMgmtAPI;

    private DesktopPoolThirdPartyMgmtAPI desktopPoolThirdPartyMgmtAPI;

    public CreatePoolThirdPartyDesktopBatchTaskHandler(CreateComputerDesktopBatchTaskHandlerRequest request,
                                                       Iterator<? extends BatchTaskItem> iterator, DesktopPoolConfigDTO desktopPoolConfigDTO) {
        super(iterator);
        this.desktopPoolConfigDTO = desktopPoolConfigDTO;
        this.desktopPoolDTO = request.getDesktopPoolDTO();

        this.auditLogAPI = request.getAuditLogAPI();
        this.desktopPoolMgmtAPI = request.getDesktopPoolMgmtAPI();
        this.cbbDesktopPoolMgmtAPI = request.getCbbDesktopPoolMgmtAPI();
        this.desktopPoolComputerMgmtAPI = request.getDesktopPoolComputerMgmtAPI();
        this.desktopPoolThirdPartyMgmtAPI = request.getDesktopPoolThirdPartyMgmtAPI();
        this.hasSecondAdd = request.getHasSecondAdd();

    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");
        CreateThirdPartyDesktopBatchTaskItem item = (CreateThirdPartyDesktopBatchTaskItem) batchTaskItem;
        ComputerDTO computerDTO = item.getComputerDTO();
        UUID deskId = computerDTO.getId();
        String poolName = desktopPoolDTO.getName();
        LOGGER.info("开始创建池[{}]中的桌面, deskId:{}", poolName, deskId);

        try {
            String desktopName = computerDTO.getName();
            LOGGER.info("开始创建池桌面, 云桌面名称{}", desktopName);

            CreateDesktopResponse createResponse = createPoolThirdPartyDesktop(deskId, desktopName, computerDTO, desktopPoolDTO);
            //添加终端与桌面池关系
            UpdatePoolThirdPartyBindObjectDTO updatePoolThirdPartyBindObjectDTO = new UpdatePoolThirdPartyBindObjectDTO();
            updatePoolThirdPartyBindObjectDTO.setPoolId(desktopPoolDTO.getId());
            updatePoolThirdPartyBindObjectDTO.setAddComputerByIdList(Collections.singletonList(deskId));
            desktopPoolThirdPartyMgmtAPI.updatePoolBindObject(updatePoolThirdPartyBindObjectDTO);

            if (Boolean.TRUE.equals(hasSecondAdd)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_COMPUTER_SUCCESS_LOG,
                        poolName, createResponse.getDesktopName());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_COMPUTER_SUCCESS_LOG)
                        .msgArgs(new String[]{poolName, createResponse.getDesktopName()}).build();
            } else {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_COMPUTER_CREATE_SUCCESS_LOG,
                        poolName, createResponse.getDesktopName());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_COMPUTER_CREATE_SUCCESS_LOG)
                        .msgArgs(new String[]{poolName, createResponse.getDesktopName()}).build();
            }
        } catch (Exception e) {
            String message;
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                message = ex.getI18nMessage();
            } else {
                message = e.getMessage();
            }
            LOGGER.error(String.format("桌面池[%s]创建PC终端[%s]失败", poolName, deskId), e);
            if (Boolean.TRUE.equals(hasSecondAdd)) {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_COMPUTER_FAIL_LOG, poolName, message);
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_COMPUTER_FAIL_LOG, e, poolName, message);
            } else {
                auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_COMPUTER_CREATE_FAIL_LOG, poolName, message);
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_COMPUTER_CREATE_FAIL_LOG, e, poolName, message);
            }


        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        try {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新桌面池[%s]信息失败", desktopPoolDTO.getId()), e);
        }
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_COMPUTER_BATCH_CREATE_TASK_RESULT);
    }


    private CreateDesktopResponse createPoolThirdPartyDesktop(UUID deskId, String desktopName, ComputerDTO computerDTO,
                                                              DesktopPoolBasicDTO desktopPoolDTO) throws BusinessException {
        CreatePoolComputerDesktopRequest request = new CreatePoolComputerDesktopRequest();
        request.setDesktopId(deskId);
        request.setDesktopName(desktopName);
        request.setPoolId(desktopPoolDTO.getId());
        request.setPoolName(desktopPoolDTO.getName());
        request.setStrategyId(desktopPoolDTO.getStrategyId());
        request.setPoolDeskType(DesktopPoolType.convertToPoolDeskType(desktopPoolDTO.getPoolModel()));
        request.setOsName(computerDTO.getOs());
        request.setAgentVersion(computerDTO.getAgentVersion());
        if (Objects.nonNull(desktopPoolConfigDTO)) {
            request.setSoftwareStrategyId(desktopPoolConfigDTO.getSoftwareStrategyId());
            request.setUserProfileStrategyId(desktopPoolConfigDTO.getUserProfileStrategyId());
        }
        return desktopPoolMgmtAPI.createThirdPartyDesktop(request);
    }
}
