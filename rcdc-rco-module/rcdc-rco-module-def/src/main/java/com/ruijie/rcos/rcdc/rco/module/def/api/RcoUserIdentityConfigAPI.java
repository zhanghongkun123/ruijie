package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UpdateUserConfigNotifyContentDTO;

/**
 * Description: 用户身份验证配置API接口
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public interface RcoUserIdentityConfigAPI {

    /**
     * 用户编辑个人信息通知shine
     * 
     * @param notifyContentDTO 包装信息
     */
    void editUserIdentityConfigNotifyShine(UpdateUserConfigNotifyContentDTO notifyContentDTO);

}
