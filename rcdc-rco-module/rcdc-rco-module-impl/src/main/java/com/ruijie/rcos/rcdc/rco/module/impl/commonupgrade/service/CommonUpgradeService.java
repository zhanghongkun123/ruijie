package com.ruijie.rcos.rcdc.rco.module.impl.commonupgrade.service;

import com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto.GuideImageTemplateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 * Description: 通用升级service
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/17
 *
 * @author chenl
 */
public interface CommonUpgradeService {


    /**
     * 查询低版本的镜像模板列表
     *
     * @return GuideImageTemplateDTO
     * @throws BusinessException 业务异常
     */
    List<GuideImageTemplateDTO> getLowImageTemplateVersion() throws BusinessException;

}
