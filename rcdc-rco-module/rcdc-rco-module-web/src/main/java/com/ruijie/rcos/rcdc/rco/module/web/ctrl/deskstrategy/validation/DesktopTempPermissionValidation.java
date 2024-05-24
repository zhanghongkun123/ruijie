package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.validation;

import java.util.Objects;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbClipBoardSupportTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DesktopTempPermissionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request.CreateDesktopTempPermissionRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 临时权限Validation
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
@Service
public class DesktopTempPermissionValidation {

    
    @Autowired
    private DesktopTempPermissionAPI desktopTempPermissionAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    /**
     * 云桌面临时权限校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createDesktopTempPermissionValidate(CreateDesktopTempPermissionRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        if (request.getStartTime().after(request.getEndTime())) {
            throw new BusinessException(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_START_END_TIME_ERROR);
        }

        // 剪贴板校验
        request.setClipBoardMode(deskStrategyAPI.clipBoardArrValidate(request.getClipBoardSupportTypeArr(), true));

        // 剪切板 为空 磁盘映射枚举 为空 外设策略 为空
        if (Objects.isNull(request.getClipBoardMode()) && Objects.isNull(request.getDiskMappingType())
                && CollectionUtils.isEmpty(request.getUsbTypeIdList()) && Objects.isNull(request.getUsbStorageDeviceMappingMode())) {
            throw new BusinessException(DesktopTempPermissionBusinessKey.RCO_DESK_TEMP_PERMISSION_CHOOSE_AT_LEAST_ONE);

        }
    }
}
