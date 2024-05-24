package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.batchtask;

import static com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_NULL_NAME;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.SoftwareControlBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.dto.ImportSoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request.ImportSoftwareBatchTaskHandlerRequest;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */
public class ImportSoftwareBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportSoftwareBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    /* 导入目标组*/
    private UUID targetGroupId;

    private Set<UUID> resolvedGroupIdSet;

    public ImportSoftwareBatchTaskHandler(ImportSoftwareBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.softwareControlMgmtAPI = request.getSoftwareControlMgmtAPI();
        this.targetGroupId = request.getTargetGroupId();
        this.resolvedGroupIdSet = new HashSet<>();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        CreateSoftwareBatchTaskItem item = (CreateSoftwareBatchTaskItem) batchTaskItem;
        ImportSoftwareDTO dto = item.getImportSoftwareDTO();
        try {
            // 检查软件分组
            UUID groupId;
            if (targetGroupId == null) {
                groupId = checkSoftwareGroup(dto);
            } else {
                groupId = targetGroupId;
            }
            resolvedGroupIdSet.add(groupId);

            if (!SOFTWARE_IMPORT_SOFTWARE_NULL_NAME.equals(dto.getSoftwareName().trim())) {
                // 导入软件数据
                importSoftware(groupId, dto);
            }
            auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_SUCCESS_LOG, new String[]{dto.getSoftwareName()});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_FAIL_LOG,
                    new String[]{dto.getSoftwareName(), e.getI18nMessage()});
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_FAIL, e, dto.getSoftwareName(), e.getI18nMessage());
        }

        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_SUCCESS_LOG).msgArgs(new String[]{dto.getSoftwareName()}).build();
    }

    /**
     * 校验软件组
     *
     * @param dto ImportSoftwareDTO
     * @return
     * @throws BusinessException
     */
    private UUID checkSoftwareGroup(ImportSoftwareDTO dto) {
        SoftwareGroupTypeEnum groupType = null;
        if (StringUtils.isBlank(dto.getSoftwareGroupName())
                || SoftwareControlConstants.DEFAULT_SOFTWARE_GROUP_NAME.equals(dto.getSoftwareGroupName())
                || SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_TYPE_DEFAULT.equals(dto.getSoftwareGroupType().trim())
        ) {
            groupType = SoftwareGroupTypeEnum.DEFAULT;
        } else if (groupType == null || SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_TYPE_CUSTOM.equals(dto.getSoftwareGroupType().trim())) {
            groupType = SoftwareGroupTypeEnum.CUSTOM;
        }
        SoftwareGroupDTO softwareGroupDTO = new SoftwareGroupDTO();
        softwareGroupDTO.setName(dto.getSoftwareGroupName());
        softwareGroupDTO.setDescription(dto.getSoftwareGroupDesc());
        softwareGroupDTO.setGroupType(groupType);
        return softwareControlMgmtAPI.getSoftwareGroupIdIfNotExistCreate(softwareGroupDTO);
    }

    /**
     * @param groupId UUID
     * @param dto     ImportSoftwareDTO
     * @throws BusinessException
     */
    private void importSoftware(UUID groupId, ImportSoftwareDTO dto) throws BusinessException {
        checkSoftwareCountLimit();
        SoftwareDTO parentSoftware = dto.convertToSoftwareDTO();
        checkDuplicationSoftwareName(parentSoftware.getName());

        if (!parentSoftware.getDirectoryFlag()) {
            //处理正常软件的导入
            createNormalSoftware(groupId, parentSoftware);
        } else {
            checkDuplicationSoftwareName(parentSoftware.getName());
            parentSoftware.setGroupId(groupId);
            //处理绿色软件的导入
            if (!CollectionUtils.isEmpty(dto.getChildrenList())) {
                dto.getChildrenList().forEach((child) -> {
                    SoftwareDTO childSoftwareDTO = child.convertToSoftwareDTO();
                    childSoftwareDTO.setParentId(parentSoftware.getId());
                    childSoftwareDTO.setGroupId(groupId);
                    childSoftwareDTO.setTopLevelFile(false);
                    softwareControlMgmtAPI.createSoftware(childSoftwareDTO);
                });
                softwareControlMgmtAPI.createSoftware(parentSoftware);
            } else {
                throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GREEN_SOFTWARE_IS_EMPTY);
            }
        }
    }

    /**
     * @param request SoftwareDTO
     * @param groupId 软件组id
     * @throws BusinessException
     */
    private void createNormalSoftware(UUID groupId, SoftwareDTO request) throws BusinessException {
        request.setGroupId(groupId);
        checkDuplicationSoftwareMd5(request);
        softwareControlMgmtAPI.createSoftware(request);
    }

    /**
     * @param request
     * @throws BusinessException
     */
    private void checkDuplicationSoftwareMd5(SoftwareDTO request) throws BusinessException {
        String groupName = "";
        String softwareName = "";
        SoftwareDTO softwareDTO;
        //校验软件MD5是否已存在。
        softwareDTO = softwareControlMgmtAPI.checkSoftwareMd5Duplication(request.getFileCustomMd5());
        if (softwareDTO != null) {
            softwareName = softwareDTO.getName();
            try {
                SoftwareGroupDTO softwareGroupDTO = softwareControlMgmtAPI.findSoftwareGroupById(softwareDTO.getGroupId());
                groupName = softwareGroupDTO.getName();
            } catch (BusinessException e) {
                LOGGER.error("软件分组不存在： software_group_id :  " + softwareDTO.getGroupId(), e.getMessage());
            }
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_MD5_HAS_DUPLICATION,
                    groupName, softwareName, request.getFileCustomMd5());
        }
    }

    /**
     * @param softwareName softwareName
     * @throws BusinessException
     */
    private void checkDuplicationSoftwareName(String softwareName) throws BusinessException {
        String groupName = "";
        //校验软件名称是否已存在。
        SoftwareDTO softwareDTO = softwareControlMgmtAPI.checkSoftwareNameDuplication(softwareName);
        if (softwareDTO != null) {
            softwareName = softwareDTO.getName();
            try {
                SoftwareGroupDTO softwareGroupDTO = softwareControlMgmtAPI.findSoftwareGroupById(softwareDTO.getGroupId());
                groupName = softwareGroupDTO.getName();
            } catch (BusinessException e) {
                LOGGER.error("软件分组不存在： software_group_id :  " + softwareDTO.getGroupId(), e.getMessage());
            }
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_NAME_HAS_DUPLICATION, groupName, softwareName);
        }
    }

    /**
     *
     * @throws BusinessException
     */
    private void checkSoftwareCountLimit() throws BusinessException {
        long softwareCount = softwareControlMgmtAPI.count();
        if (Constants.SOFTWARE_MAX_COUNT_LIMIT <= softwareCount) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_COUNT_LIMIT_ERROR,
                    String.valueOf(Constants.SOFTWARE_MAX_COUNT_LIMIT));
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 结束导入任务
        softwareControlMgmtAPI.finishAddSoftwareData();
        return buildDefaultFinishResult(successCount, failCount, SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_BATCH_TASK_SUCCESS);
    }
}
