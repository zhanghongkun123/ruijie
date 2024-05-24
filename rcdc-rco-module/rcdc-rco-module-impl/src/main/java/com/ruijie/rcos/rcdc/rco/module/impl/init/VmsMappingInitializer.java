package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVmsMappingAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.SaveVmsMappingRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.VmsMappingDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ProductTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月24日
 *
 * @author TING
 */
@Service
public class VmsMappingInitializer implements SafetySingletonInitializer, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(VmsMappingInitializer.class);

    @Autowired
    private CbbVmsMappingAPI cbbVmsMappingAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    private static final int ORDER_VALUE = 50;

    @Override
    public void safeInit() {
        ThreadExecutors.execute("VmsMappingInitializer", this::doInit);
    }

    private void doInit() {
        LOGGER.info("开始VMS映射信息初始化");

        List<UUID> allDeskIdList = viewDesktopDetailDAO.findAllDeskId();

        if (CollectionUtils.isEmpty(allDeskIdList)) {
            LOGGER.info("桌面列表为空，无需进行vms初始化");
            return;
        }

        allDeskIdList.forEach(deskId -> {
            VmsMappingDTO mappingDTO = cbbVmsMappingAPI.getVmsMappingDTOByBusinessId(deskId);
            if (Objects.isNull(mappingDTO)) {
                cbbVmsMappingAPI.saveVmsMappingInfo(buildSaveVmsMappingRequest(deskId));
            }
        });

        LOGGER.info("VMS映射信息初始化结束");
    }

    private SaveVmsMappingRequest buildSaveVmsMappingRequest(UUID deskId) {
        SaveVmsMappingRequest request = new SaveVmsMappingRequest();
        request.setBusinessId(deskId);
        request.setBusinessType(ProductTypeEnum.RCO);

        return request;
    }

    @Override
    public int getOrder() {
        return ORDER_VALUE;
    }
}
