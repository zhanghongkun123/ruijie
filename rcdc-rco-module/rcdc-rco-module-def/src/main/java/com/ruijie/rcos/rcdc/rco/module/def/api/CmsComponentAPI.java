package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: CmsComponentAPI
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
public interface CmsComponentAPI {

    /**
     * 获取CMS启用情况
     *
     * @return 响应
     * @throws BusinessException 业务异常
     */
    String getCmsComponent() throws BusinessException;

    /**
     * 初始化CmApp
     */
    void initCmApp();


}
