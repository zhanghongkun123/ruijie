package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.batch;

import java.util.Iterator;
import java.util.UUID;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import org.springframework.util.Assert;

;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.common.batch.AbstractI18nAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.request.RcaObjectStrategyDetailRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/4 11:41
 *
 * @author zhangsiming
 */
public class DeleteRcaMainStrategyBatchHandler extends AbstractI18nAwareBatchTaskHandler<RcaObjectStrategyDetailRequest> {

    private RcaMainStrategyAPI rcaMainStrategyAPI;

    private PermissionHelper permissionHelper;
    
    private UUID adminId;

    public DeleteRcaMainStrategyBatchHandler(RcaMainStrategyAPI rcaMainStrategyAPI, PermissionHelper permissionHelper,
            Iterator<? extends BatchTaskItem> batchTaskItemIterator, UUID adminId) {
        super(batchTaskItemIterator);
        Assert.notNull(rcaMainStrategyAPI, "rcaMainStrategyAPI can not be null");
        this.rcaMainStrategyAPI = rcaMainStrategyAPI;
        this.permissionHelper = permissionHelper;
        this.adminId = adminId;
    }

    @Override
    protected void innerProcessItem(I18nBatchTaskItem<RcaObjectStrategyDetailRequest> batchTaskItem) throws BusinessException {
        UUID strategyId = batchTaskItem.getItemID();

        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setId(strategyId);
        RcaMainStrategyDTO mainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);

        if (!permissionHelper.isAllGroupPermission(this.adminId) && !permissionHelper.hasDataPermission(this.adminId,
                String.valueOf(mainStrategyDTO.getId()), AdminDataPermissionType.APP_MAIN_STRATEGY)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_RCA_HAS_NO_DATA_PERMISSION);
        }

        rcaMainStrategyAPI.deleteRcaMainStrategy(strategyId);
        permissionHelper.deleteByPermissionDataId(String.valueOf(strategyId));
    }


}
