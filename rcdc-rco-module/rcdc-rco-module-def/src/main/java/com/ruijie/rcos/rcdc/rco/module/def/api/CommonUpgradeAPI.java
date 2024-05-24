package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto.GuideImageTemplateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月15日
 *
 * @author ljm
 */
public interface CommonUpgradeAPI {


    /**
     * 查询低版本的镜像模板列表
     *
     * @return GuideImageTemplateDTO
     * @throws BusinessException 业务异常
     */
    List<GuideImageTemplateDTO> getLowImageTemplateVersion() throws BusinessException;
}
