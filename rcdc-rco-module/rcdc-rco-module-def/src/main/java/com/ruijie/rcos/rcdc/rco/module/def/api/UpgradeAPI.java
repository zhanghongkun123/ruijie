package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.PromptVersionDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/13 19:24
 *
 * @author ketb
 */
public interface UpgradeAPI {

    /**
     * 查看提示升级版本列表
     * @return 版本列表
     * @throws BusinessException 业务异常
     */
    PromptVersionDTO queryVersionList() throws BusinessException;

    /**
     * 取消提示版本
     * @param pkgName 安装包名
     * @throws BusinessException 业务异常
     */
    void cancelPromptVersion(String pkgName) throws BusinessException;
}
