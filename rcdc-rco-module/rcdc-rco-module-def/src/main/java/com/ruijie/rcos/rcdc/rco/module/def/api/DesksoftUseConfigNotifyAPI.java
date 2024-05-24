package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesksoftUseConfigDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: CMC 软件上报配置
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/4
 *
 * @author Jarman
 */

public interface DesksoftUseConfigNotifyAPI {


    /**
     *
     * @param parameter 更新配置并且通知所有桌面
     */
    void notifyAllDesk(String parameter);


    /**
     *
     * @param parameter 更新CMC 软件上报配置
     * @throws BusinessException 异常
     */
    void updateConfig(String parameter) throws BusinessException;

    /**
     *
     * @param parameter 更新CMC 软件上报配置 不通知桌面
     * @throws BusinessException 异常
     */
    void updateUserConfigNotNotifyDesk(String parameter) throws BusinessException;

    /**
     * 查询指定参数的值
     *
     * @return UserGroupDesktopConfigDTO
     */
    DesksoftUseConfigDTO getGlobalCmcStrategy();

}
