package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageCalcService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.ImageInfoVO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 终端获取 VOI 镜像列表
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.26
 *
 * @author linhj
 */
@DispatcherImplemetion(ImageDispatcherConstants.PERMISSION_IMAGE_INFO)
public class GetImageInfoHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetImageInfoHandlerSPIImpl.class);

    private static final String IMAGE_ID = "imageId";

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ImageCalcService imageCalcService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        JSONObject jsonObject = JSONObject.parseObject(request.getData());
        UUID imageId = UUID.fromString(jsonObject.getString(IMAGE_ID));

        LOGGER.info("终端请求镜像模板信息，terminalId={}, imageId={}", request.getTerminalId(), imageId);
        ImageInfoVO imageInfoVO = new ImageInfoVO();
        try {
            CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
            imageInfoVO.setImageId(cbbImageTemplateDetailDTO.getId());
            imageInfoVO.setImageName(cbbImageTemplateDetailDTO.getImageName());
            imageInfoVO.setOsType(cbbImageTemplateDetailDTO.getOsType());
            imageInfoVO.setSupportGoldenImage(cbbImageTemplateDetailDTO.isSupportGoldenImage());
            imageInfoVO.setDesktopRedirect(false);
            imageInfoVO.setCbbImageType(cbbImageTemplateDetailDTO.getCbbImageType());

            /* 优先查询云桌面策略系统盘大小，查询失败，设置镜像系统盘大小给前端 */
            imageInfoVO.setSystemDiskSize(cbbImageTemplateDetailDTO.getSystemDisk());

            Integer baseFileSize = imageCalcService.getImageFileSize(cbbImageTemplateDetailDTO.getId());
            imageInfoVO.setBaseFileSize(baseFileSize);

            imageInfoVO.setTerminalLocalEditType(cbbImageTemplateDetailDTO.getTerminalLocalEditType());

        } catch (BusinessException ex) {
            LOGGER.error("获取镜像大小失败, imageId: {}", imageId);
            LOGGER.error("获取镜像大小失败", ex);
            imageInfoVO.setBaseFileSize(0);
        }

        JSONObject result = new JSONObject();
        result.put("imageInfo", imageInfoVO);
        messageHandlerAPI.response(ShineMessageUtil.buildResponseMessage(request, result));
    }
}
