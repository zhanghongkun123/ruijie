package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.batch;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPeripheralStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rco.module.common.batch.AbstractI18nAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.request.RcaObjectStrategyDetailRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/23 17:51
 *
 * @author zhangsiming
 */
public class AlterRcaStrategyBatchHandler extends AbstractI18nAwareBatchTaskHandler<RcaObjectStrategyDetailRequest> {

    private RcaMainStrategyAPI rcaMainStrategyAPI;

    private RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI;

    private UUID rcaMainStrategyId;

    private UUID rcaPeripheralStrategyId;


    public AlterRcaStrategyBatchHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator,
                                        RcaMainStrategyAPI rcaMainStrategyAPI, RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI,
                                        UUID rcaMainStrategyId, UUID rcaPeripheralStrategyId) {
        super(batchTaskItemIterator);
        this.rcaMainStrategyAPI = rcaMainStrategyAPI;
        this.rcaPeripheralStrategyAPI = rcaPeripheralStrategyAPI;
        this.rcaMainStrategyId = rcaMainStrategyId;
        this.rcaPeripheralStrategyId = rcaPeripheralStrategyId;
    }

    @Override
    protected void innerProcessItem(I18nBatchTaskItem<RcaObjectStrategyDetailRequest> item) throws BusinessException {
        RcaObjectStrategyDetailRequest applyObjectInfo = item.getItemData();
        RcaStrategyRelationshipDTO rcaStrategyRelationshipDTO = new RcaStrategyRelationshipDTO();
        BeanUtils.copyProperties(applyObjectInfo, rcaStrategyRelationshipDTO);
        if (rcaMainStrategyId != null) {
            rcaStrategyRelationshipDTO.setStrategyId(rcaMainStrategyId);
            rcaMainStrategyAPI.alterRcaMainStrategy(rcaStrategyRelationshipDTO);
        }
        if (rcaPeripheralStrategyId != null) {
            rcaStrategyRelationshipDTO.setStrategyId(rcaPeripheralStrategyId);
            rcaPeripheralStrategyAPI.alterRcaPeripheralStrategy(rcaStrategyRelationshipDTO);
        }
    }
}
