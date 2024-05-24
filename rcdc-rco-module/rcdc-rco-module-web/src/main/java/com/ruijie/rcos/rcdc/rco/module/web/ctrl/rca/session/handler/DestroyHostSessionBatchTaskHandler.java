package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.session.handler;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rco.module.common.batch.AbstractI18nAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 批量注销应用主机会话Handler
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/8/24
 *
 * @author lihengjing
 */
public class DestroyHostSessionBatchTaskHandler extends AbstractI18nAwareBatchTaskHandler<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestroyHostSessionBatchTaskHandler.class);

    private RcaHostSessionAPI rcaHostSessionAPI;

    public DestroyHostSessionBatchTaskHandler(Collection<? extends BatchTaskItem> batchTaskItemCollection, RcaHostSessionAPI rcaHostSessionAPI) {
        super(batchTaskItemCollection);
        this.rcaHostSessionAPI = rcaHostSessionAPI;
    }

    public DestroyHostSessionBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, RcaHostSessionAPI rcaHostSessionAPI) {
        super(batchTaskItemIterator);
        this.rcaHostSessionAPI = rcaHostSessionAPI;
    }

    @Override
    protected void innerProcessItem(I18nBatchTaskItem<Void> item) throws BusinessException {
        UUID sessionRecordId = item.getItemID();

        rcaHostSessionAPI.destroySession(sessionRecordId);
    }
}
