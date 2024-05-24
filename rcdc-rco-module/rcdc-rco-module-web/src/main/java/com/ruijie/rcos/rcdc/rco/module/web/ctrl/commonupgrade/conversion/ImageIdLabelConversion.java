package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.conversion;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description:  <br>
 * Copyright: Copyright (c) 2023 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2023/12/5 <br>
 *
 * @author fang
 */
@Service
public class ImageIdLabelConversion extends AbstractlIdLabelConversion<UUID> {

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Override
    protected String getLabel(UUID id) throws BusinessException {
        CbbImageTemplateDTO cbbImageTemplateDTO = imageTemplateMgmtAPI.getCbbImageTemplateDTO(id);
        return cbbImageTemplateDTO.getImageTemplateName();
    }
}
