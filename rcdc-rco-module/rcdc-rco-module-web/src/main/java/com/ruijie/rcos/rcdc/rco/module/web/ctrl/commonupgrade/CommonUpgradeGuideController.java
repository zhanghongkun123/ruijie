package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade;

import com.ruijie.rcos.rcdc.rco.module.def.api.CommonUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto.GuideImageTemplateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Description: 升级包管理ctrl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月21日
 *
 * @author chenl
 */
@Controller
@RequestMapping("/rco/app")
public class CommonUpgradeGuideController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUpgradeGuideController.class);

    @Autowired
    private CommonUpgradeAPI commonUpgradeAPI;

    /**
     * 校验通用组件向导
     *
     * @return 响应数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/guide")
    public DefaultWebResponse guideInfo() throws BusinessException {
        List<GuideImageTemplateDTO> guideImageTemplateDTOList = commonUpgradeAPI.getLowImageTemplateVersion();
        return DefaultWebResponse.Builder.success(guideImageTemplateDTOList);
    }
}
