package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月10日
 * 
 * @author zouqi
 */
public interface AutoTrialLicenseAPI {

    /**
     ** 自动授权api
     * @throws BusinessException 业务异常
     */
    
    void autoTrialLicense() throws BusinessException;

    /**
     * IDV终端自动授权
     * @throws BusinessException 业务异常
     */
    void idvAutoTrialLicense() throws BusinessException;

    /**
     * VOI终端自动授权
     * @throws BusinessException 业务异常
     */
    void voiAutoTrialLicense() throws BusinessException;

    /**
     * 系统升级后 VOI终端自动授权
     * @throws BusinessException 业务异常
     */
    void systemUpVoiAutoTrialLicense() throws BusinessException;

    /**
     * 生成MINI服务器内置证书
     *
     * @throws BusinessException 业务异常
     */
    void generateMiniInternalLicense() throws BusinessException;
}
