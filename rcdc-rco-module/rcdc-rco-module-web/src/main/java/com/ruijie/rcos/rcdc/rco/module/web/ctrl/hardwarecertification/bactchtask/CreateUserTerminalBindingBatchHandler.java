package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.bactchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.common.batch.AbstractI18nAwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserMacBindingDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.Iterator;

/**
 * Description: 创建绑定关系批处理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21 19:24
 *
 * @author yxq
 */
public class CreateUserTerminalBindingBatchHandler extends AbstractI18nAwareBatchTaskHandler<UserMacBindingDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserTerminalBindingBatchHandler.class);

    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    public CreateUserTerminalBindingBatchHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    protected void innerProcessItem(I18nBatchTaskItem<UserMacBindingDTO> item) throws BusinessException {
        UserMacBindingDTO userMacBindingDTO = item.getItemData();
        LOGGER.info("创建绑定关系，请求：[{}]", JSON.toJSONString(userMacBindingDTO));
        userHardwareCertificationAPI.createUserMacBinding(userMacBindingDTO);
    }

    public void setUserHardwareCertificationAPI(UserHardwareCertificationAPI userHardwareCertificationAPI) {
        this.userHardwareCertificationAPI = userHardwareCertificationAPI;
    }

}
