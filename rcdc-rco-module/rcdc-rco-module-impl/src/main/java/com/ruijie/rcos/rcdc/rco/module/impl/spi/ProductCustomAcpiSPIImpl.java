package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskActiveStatus;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.ProductCustomAcpiSPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.COMMA;
import static com.ruijie.rcos.rcdc.rco.module.impl.Constants.APP_DISK_ID_ARR;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-05-05
 *
 * @author chen zj
 */
@SuppressWarnings("unused")
public class ProductCustomAcpiSPIImpl implements ProductCustomAcpiSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCustomAcpiSPIImpl.class);

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Override
    public JSONObject getCustomAcpi(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");

        // 产品侧实现 acpi.product 参数
        JSONObject acpiParam = new JSONObject();
        acpiParam.put("product", "RCO");
        return acpiParam;
    }

    @Override
    public JSONObject getStartVdiDeskCustomAcpi(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");

        List<CbbDeskDiskDTO> diskList = cbbVDIDeskDiskAPI.listDeskDisk(deskId);

        List<String> appDiskList = diskList.stream()
                .filter(this::isAttachedAppDisk)
                .map(CbbDeskDiskDTO::getDiskSn)
                .collect(Collectors.toList());


        JSONObject acpi = new JSONObject();
        if (CollectionUtils.isNotEmpty(appDiskList)) {
            acpi.put(APP_DISK_ID_ARR, String.join(COMMA, appDiskList));
        }

        return acpi;
    }

    private boolean isAttachedAppDisk(CbbDeskDiskDTO disk) {

        return disk.getType() == CbbDiskType.APP_SOFTWARE_PACKAGE
                && disk.getState() == CbbDiskState.ACTIVE
                && disk.getActiveStatus() == CbbDiskActiveStatus.ACTIVE;

    }

    @Override
    public JSONObject getStartIdvDeskCustomAcpi(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        // 预留接口，这边先只返回空JSONObject，后续有具体业务再返回具体信息
        return new JSONObject();
    }
}
