package com.ruijie.rcos.rcdc.rco.module.impl.service;

/**
 * Description: 安全认证配置相关
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/31
 *
 * @author linrenjian
 */
public interface CertifiedSecurityConfigService {


    /**
     * 更新是否通知IP 变更的提示
     * @param enableNotifyLoginTerminalChange 是否通知变更
     */
    void updateEnableNotifyLoginTerminalChange(String enableNotifyLoginTerminalChange);

}
