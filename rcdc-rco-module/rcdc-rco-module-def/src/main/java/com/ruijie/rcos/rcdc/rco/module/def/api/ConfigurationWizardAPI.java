package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetConfigurationWizardResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;


/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/1  <br>
 *
 * @author yyz
 */
public interface ConfigurationWizardAPI {

    /**
     * 获取配置向导信息
     *
     * @param request 分页参数
     * @return 分页信息
     */
    
    GetConfigurationWizardResponse getConfigurationWizard(GetConfigurationWizardRequest request);

    /**
     * 获取配置向导信息
     * 
     * @return 分页信息
     */
    GetConfigurationWizardResponse getConfigurationWizardResponse();
    /**
     * 设置配置向导
     *
     * @param request 设置参数
     * @return 默认值
     */
    
    DefaultResponse setConfigurationWizard(SetConfigurationWizardRequest request);

    /**
     * 标识当前版本为S2版本
     */
    void markAsS2Version();
}
