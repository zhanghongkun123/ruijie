package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DeleteIDVDesktopOperateLogTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.IDVDesktopOperateLogHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/2
 *
 * @author XiaoJiaXin
 */
public class DesktopOpLogMgmtAPIImpl implements DesktopOpLogMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOpLogMgmtAPIImpl.class);

    @Autowired
    private IDVDesktopOperateLogHelper idvDesktopOperateLogHelper;

    @Autowired
    private CbbDesktopOpLogMgmtAPI cbbDesktopOpLogMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public DesktopOpLogDTO buildDeleteDesktopOpLogRequest(UUID deskId
            , DeleteIDVDesktopOperateLogTypeEnums operateLogTypeEnums) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        Assert.notNull(operateLogTypeEnums, "operateLogTypeEnums can not be null");

        return idvDesktopOperateLogHelper.buildDesktopOpLogRequest(deskId,operateLogTypeEnums);
    }

    @Override
    public void saveOperateLog(DesktopOpLogDTO desktopOpLogRequest) {
        Assert.notNull(desktopOpLogRequest, "desktopOpLogRequest can not be null");

        if (StringUtils.isNotEmpty(desktopOpLogRequest.getTerminalId()) && StringUtils.isEmpty(desktopOpLogRequest.getTerminalName())) {
            try {
                CbbTerminalBasicInfoDTO basicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(desktopOpLogRequest.getTerminalId());
                desktopOpLogRequest.setTerminalName(basicInfo.getTerminalName());
                desktopOpLogRequest.setTerminalType(basicInfo.getProductType());
            } catch (BusinessException e) {
                LOGGER.error("未查询到终端Id为[{}]的终端,异常原因为：[{}]", desktopOpLogRequest.getTerminalId(), e);
            }
        }
        try {
            cbbDesktopOpLogMgmtAPI.recordDeskOperateLog(desktopOpLogRequest);
        } catch (BusinessException e) {
            LOGGER.error("保存IDV云桌面审计日志出现错误：{}", e);
        }
    }

}