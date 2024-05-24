package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: EST Client 管理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.12.29
 *
 * @author linhj
 */
public interface EstClientMgmtAPI {

    /**
     * 查询 EST Client 最大限制数量
     *
     * @return 配置限制
     * @throws BusinessException 无法获取部署模式
     */
    int estClientLimit() throws BusinessException;

}
