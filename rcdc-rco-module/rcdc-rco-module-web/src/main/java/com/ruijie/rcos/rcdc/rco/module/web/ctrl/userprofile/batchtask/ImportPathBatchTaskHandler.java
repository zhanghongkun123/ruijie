package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileValidateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto.ImportUserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.ImportPathBatchTaskHandlerRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: 批量创建路径
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/26
 *
 * @author WuShengQiang
 */
public class ImportPathBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportPathBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserProfileMgmtAPI userProfileMgmtAPI;

    private String userName;

    private UserProfileValidateAPI userProfileValidateAPI;

    public ImportPathBatchTaskHandler(ImportPathBatchTaskHandlerRequest request) {
        super(request.getBatchTaskItemIterator());
        this.auditLogAPI = request.getAuditLogAPI();
        this.userProfileMgmtAPI = request.getUserProfileMgmtAPI();
        this.userName = request.getUserName();
    }

    public void setUserProfileValidateAPI(UserProfileValidateAPI userProfileValidateAPI) {
        this.userProfileValidateAPI = userProfileValidateAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        CreatePathBatchTaskItem item = (CreatePathBatchTaskItem) batchTaskItem;
        UserProfilePathDTO dto = item.getUserProfilePathDTO();
        try {
            LOGGER.debug("执行用户配置导入，导入对象：{}", JSONObject.toJSONString(dto));
            //检查子路径个数是否超出
            userProfileValidateAPI.validateUserProfileChildPath(dto.getChildPathArr(), null);

            // 检查路径分组
            UUID groupId = checkPathGroup(dto.getGroupName(), dto.getGroupDescribe());

            // 导入软件数据
            importPath(groupId, dto, userName);
            auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_SUCCESS, dto.getName());
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_FAIL, dto.getName(), e.getI18nMessage());
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_FAIL, e, dto.getName(), e.getI18nMessage());
        }
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_SUCCESS).msgArgs(new String[] {dto.getName()}).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 结束任务删除缓存标记
        userProfileMgmtAPI.finishAddPathData();
        return buildDefaultFinishResult(successCount, failCount, UserProfileBusinessKey.RCDC_RCO_IMPORT_PATH_BATCH_TASK_SUCCESS);
    }

    private UUID checkPathGroup(String groupName, String description) {
        UserProfilePathGroupDTO groupDTO = new UserProfilePathGroupDTO();
        groupDTO.setName(groupName);
        groupDTO.setDescription(description);
        return userProfileMgmtAPI.getPathGroupIdIfNotExistCreate(groupDTO);
    }

    private void importPath(UUID groupId, UserProfilePathDTO dto, String userName) throws BusinessException {
        dto.setGroupId(groupId);
        dto.setCreatorUserName(userName);
        userProfileMgmtAPI.createUserProfilePath(dto);
    }
}