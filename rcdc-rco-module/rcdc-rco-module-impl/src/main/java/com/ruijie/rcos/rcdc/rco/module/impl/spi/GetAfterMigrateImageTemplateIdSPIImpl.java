package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.NewImageIdDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.SystemBusinessMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.OldImageIdDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.SystemBusinessMappingEntity;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年05月11日
 *
 * @author xgx
 */
@DispatcherImplemetion(Constants.GET_NEW_IMAGE_ID)
public class GetAfterMigrateImageTemplateIdSPIImpl implements CbbDispatcherHandlerSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAfterMigrateImageTemplateIdSPIImpl.class);

    @Autowired
    private SystemBusinessMappingDAO systemBusinessMappingDAO;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest can not be null");

        LOGGER.info("终端[{}]请求获取新平台镜像ID", cbbDispatcherRequest.getTerminalId());
        String data = cbbDispatcherRequest.getData();
        OldImageIdDTO oldImageIdDTO = JSON.parseObject(data, OldImageIdDTO.class);
        Assert.hasText(oldImageIdDTO.getOldImageId(), "oldImageId can not be blank");
        SystemBusinessMappingEntity systemBusinessMapping = systemBusinessMappingDAO.findBySystemTypeAndBusinessTypeAndSrcId(
                SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, oldImageIdDTO.getOldImageId());
        NewImageIdDTO newImageIdDTO = new NewImageIdDTO(StringUtils.EMPTY);
        if (null != systemBusinessMapping) {
            UUID imageTemplateId = UUID.fromString(systemBusinessMapping.getDestId());
            if (imageTemplateMgmtAPI.checkImageTemplateExist(imageTemplateId)) {
                newImageIdDTO.setNewImageId(String.valueOf(imageTemplateId));
            }
        }

        try {
            shineMessageHandler.responseContent(cbbDispatcherRequest, CommonMessageCode.SUCCESS, newImageIdDTO);
        } catch (Exception e) {
            LOGGER.error("终端ID[" + cbbDispatcherRequest.getTerminalId() + "]请求获取镜像ID响应失败", e);
        }
    }
}
