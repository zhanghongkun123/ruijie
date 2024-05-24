package com.ruijie.rcos.rcdc.rco.module.impl.commonupgrade.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto.GuideImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.commonupgrade.service.CommonUpgradeService;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 通用升级service
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/17
 *
 * @author chenl
 */
@Service
public class CommonUpgradeServiceImpl implements CommonUpgradeService {


    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;



    /**
     * 查询低版本的镜像模板列表
     *
     * @return GuideImageTemplateDTO
     * @throws BusinessException 业务异常
     */
    @Override
    public List<GuideImageTemplateDTO> getLowImageTemplateVersion() throws BusinessException {

        List<GuideImageTemplateDTO> guideImageTemplateDTOList = new ArrayList<>();
        List<CbbImageTemplateDTO> cbbImageTemplateDTOVersionList = cbbImageTemplateMgmtAPI.findLowGuestToolVersionImageTemplate();
        cbbImageTemplateDTOVersionList.forEach(cbbImageTemplateDTO -> {
            GuideImageTemplateDTO guideImageTemplateDTO = new GuideImageTemplateDTO();
            guideImageTemplateDTO.setImageTemplateId(cbbImageTemplateDTO.getId());
            guideImageTemplateDTO.setImageTemplateName(cbbImageTemplateDTO.getImageTemplateName());
            guideImageTemplateDTOList.add(guideImageTemplateDTO);
        });
        return guideImageTemplateDTOList;
    }
}
