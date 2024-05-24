package com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.validation;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.request.CheckComputerNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.request.EditComputerNameWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 * 
 * @author wjp
 */
@Service
public class DeskStrategyComputerNameValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyComputerNameValidation.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DesktopAPI desktopAPI;

    /**
     * 编辑云桌面计算机名称参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void editComputerNameValidate(EditComputerNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        checkComputerNameByOsType(request.getId(), request.getComputerName());
    }

    /**
     * 检测云桌面计算机名称是否冲突参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void checkComputerNameValidate(CheckComputerNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        checkComputerNameByOsType(request.getId(), request.getComputerName());
    }

    private void checkComputerNameByOsType(UUID deskId, String computerName) throws BusinessException {
        CloudDesktopDetailDTO desktopDetailDTO;
        try {
            desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            LOGGER.error("桌面信息不存在，桌面id [{}]", deskId);
            throw new BusinessException(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, e);
        }

        desktopAPI.validateComputerName(deskId, computerName, desktopDetailDTO.getDesktopImageType());
    }
}
