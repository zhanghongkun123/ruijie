package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.peripheral.batch;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaPeripheralStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import org.springframework.util.Assert;

;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPeripheralStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.common.batch.AbstractI18nAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
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
public class DeleteRcaPeripheralStrategyBatchHandler extends AbstractI18nAwareBatchTaskHandler<Void> {

    private RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI;

    private PermissionHelper permissionHelper;
    
    private UUID adminId;
    
    public DeleteRcaPeripheralStrategyBatchHandler(RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI, PermissionHelper permissionHelper,
            Iterator<? extends BatchTaskItem> batchTaskItemIterator, UUID adminId) {
        super(batchTaskItemIterator);
        Assert.notNull(rcaPeripheralStrategyAPI, "rcaMainStrategyAPI can not be null");
        this.rcaPeripheralStrategyAPI = rcaPeripheralStrategyAPI;
        this.permissionHelper = permissionHelper;
        this.adminId = adminId;
    }

    @Override
    protected void innerProcessItem(I18nBatchTaskItem<Void> batchTaskItem) throws BusinessException {
        UUID strategyId = batchTaskItem.getItemID();

        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setId(strategyId);
        RcaPeripheralStrategyDTO rcaPeripheralStrategyDTO = rcaPeripheralStrategyAPI.getStrategyDetailBy(relationshipDTO);

        if (!permissionHelper.isAllGroupPermission(this.adminId) && !permissionHelper.hasDataPermission(this.adminId,
                String.valueOf(rcaPeripheralStrategyDTO.getId()), AdminDataPermissionType.APP_PERIPHERAL_STRATEGY)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_RCA_HAS_NO_DATA_PERMISSION);
        }

        rcaPeripheralStrategyAPI.deleteRcaPeripheralStrategy(strategyId);
        permissionHelper.deleteByPermissionDataId(String.valueOf(strategyId));
    }

}
