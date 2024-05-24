package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 修改桌面池分配用户数据批处理handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/07/26
 *
 * @author linke
 */
public class UpdateDesktopPoolUserBatchHandler extends AbstractBatchTaskHandler {

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    private UpdatePoolBindObjectDTO bindObjectDTO;

    private BaseAuditLogAPI auditLogAPI;

    private UUID[] groupIdArr;

    private String name;

    public UpdateDesktopPoolUserBatchHandler(UpdatePoolBindObjectDTO bindObjectDTO, UUID[] groupIdArr, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        this.bindObjectDTO = bindObjectDTO;
        this.groupIdArr = groupIdArr;
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.desktopPoolUserMgmtAPI = SpringBeanHelper.getBean(DesktopPoolUserMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        UUID desktopPoolId = taskItem.getItemID();
        name = desktopPoolId.toString();
        try {
            CbbDesktopPoolDTO desktopPool = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
            name = desktopPool.getName();
            checkBeforeUpdate(desktopPool);

            // 如果不是超管需要把非权限内的用户组加回来
            checkGroupAuthAndAddDefaultGroup(desktopPoolId, bindObjectDTO);

            desktopPoolUserMgmtAPI.updatePoolBindObject(bindObjectDTO);

            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_ITEM_SUCCESS_DESC, name);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[]{name}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_ITEM_FAIL_DESC, name,
                    e.getI18nMessage());
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_ITEM_FAIL_DESC, e, name, e.getI18nMessage());
        }
    }

    private void checkBeforeUpdate(CbbDesktopPoolDTO desktopPool) throws BusinessException {
        if (desktopPool.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UNAVAILABLE_UPDATE_OBJ_FAIL,
                    desktopPool.getId().toString(), desktopPool.getPoolState().name());
        }
    }

    private void checkGroupAuthAndAddDefaultGroup(UUID desktopPoolId, UpdatePoolBindObjectDTO bindObjectDTO) {
        if (ArrayUtils.isEmpty(groupIdArr)) {
            return;
        }
        List<DesktopPoolUserDTO> userGroupList = desktopPoolUserMgmtAPI.listDesktopPoolUser(desktopPoolId, IacConfigRelatedType.USERGROUP);
        if (CollectionUtils.isEmpty(userGroupList)) {
            return;
        }
        List<UUID> poolBindGroupIdList = userGroupList.stream().map(DesktopPoolUserDTO::getRelatedId).collect(Collectors.toList());
        List<UUID> selectedGroupIdList = Optional.ofNullable(bindObjectDTO.getSelectedGroupIdList()).orElse(Lists.newArrayList());
        Set<UUID> authGroupIdSet = Sets.newHashSet(groupIdArr);
        // 把非本管理员的组ID，都加进去
        selectedGroupIdList.addAll(poolBindGroupIdList.stream().filter(id -> !authGroupIdSet.contains(id)).collect(Collectors.toList()));
        selectedGroupIdList = selectedGroupIdList.stream().distinct().collect(Collectors.toList());
        bindObjectDTO.setSelectedGroupIdList(selectedGroupIdList);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_TASK_SUCCESS)
                    .msgArgs(new String[]{name}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_UPDATE_BIND_OBJ_TASK_FAIL)
                    .msgArgs(new String[]{name}).build();
        }
    }
}
