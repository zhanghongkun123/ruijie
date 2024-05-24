package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTemplateTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ImageTemplateTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTemplateTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTerminalService;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time:  2020/2/26
 *
 * @author songxiang
 */
@Service
public class ImageTerminalServiceImpl implements ImageTerminalService {

    @Autowired
    private ImageTemplateTerminalDAO imageTemplateTerminalDAO;

    @Override
    public DtoResponse<ImageTemplateTerminalDTO[]> queryEditedTerminalListOfImage(UUID imageId) {
        Assert.notNull(imageId, "imageId must not be null");
        List<ImageTemplateTerminalEntity> imageTerminalList = imageTemplateTerminalDAO.findByImageId(imageId);
        ImageTemplateTerminalDTO[] dtoArr = imageTerminalList.stream().map(entity -> {
            ImageTemplateTerminalDTO dto = new ImageTemplateTerminalDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toArray(ImageTemplateTerminalDTO[]::new);
        return DtoResponse.success(dtoArr);
    }

    @Override
    public DefaultResponse addEditedTerminalInfoOfImage(ImageTemplateTerminalDTO dto) {
        Assert.notNull(dto, "dto must not be null");
        ImageTemplateTerminalEntity entity = new ImageTemplateTerminalEntity();
        BeanUtils.copyProperties(dto, entity);
        imageTemplateTerminalDAO.save(entity);
        return DefaultResponse.Builder.success();
    }
}
