package com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.cmscomponent.enums.CmsComponentEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: CMSComponentService
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-09-04
 *
 * @author wjp
 */
public interface CmsComponentService {

    /**
     * 初始化cm app
     */
    void initCmApp();

    /**
     * 查询CMS组件是否已启用
     * 
     * @return CMS组件启用情况
     * @throws BusinessException 业务异常
     */
    CmsComponentEnum getCMSComponent() throws BusinessException;

    /**
     * 获取CMS组件启用情况标识
     * 
     * @return FindParameterResponse响应
     */
    FindParameterResponse getCMSComponentFlag();

    /**
     * 更新CMS组件启用情况标识
     * 
     * @param cmsComponent CMS组件启用情况
     */
    void updateCMSComponentFlag(String cmsComponent);

    /**
     * 初始化CMS组件启用情况标识
     */
    void initCMSComponentFlag();

    /**
     * 查询CMS安装包是否存在
     * 
     * @return CMS安装包情况
     */
    Boolean hasCMSPackage();

    /**
     * 复制CMS安装包
     * 
     * @throws BusinessException 业务异常
     */
    void copyCMSPackage() throws BusinessException;

    /**
     * 初始化cm ISO
     */
    void initCmISO();


}
