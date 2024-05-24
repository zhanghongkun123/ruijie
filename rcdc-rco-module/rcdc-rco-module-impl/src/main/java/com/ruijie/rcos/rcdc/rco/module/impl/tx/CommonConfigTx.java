package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.EditCommonConfigDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 *
 * Description: 配置管理事务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public interface CommonConfigTx {

    /**
     * 修改大屏配置
     *
     * @param dtoArr 需要修改的配置项
     * @throws BusinessException 业务异常
     */
    void edit(EditCommonConfigDTO[] dtoArr) throws BusinessException;
}
