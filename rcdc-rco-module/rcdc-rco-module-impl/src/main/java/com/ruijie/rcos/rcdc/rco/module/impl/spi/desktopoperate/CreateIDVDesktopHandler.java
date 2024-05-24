package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVmsMappingAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.SaveVmsMappingRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ProductTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Description: IDV云桌面创建成功SPI实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/27
 *
 * @author chen zj
 */
@Service
public class CreateIDVDesktopHandler extends AbstractIDVDesktopOperateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateIDVDesktopHandler.class);

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private CbbVmsMappingAPI cbbVmsMappingAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return request.getOperateType() == CbbCloudDeskOperateType.CREATE_IDV_DESK;
    }

    @Override
    protected void doProcessMessage(CbbDeskOperateNotifyRequest request) {
        if (!request.getIsSuccess()) {
            LOGGER.info("创建云桌面[id:{}]失败,无需记录审计日志", request.getDeskId());
            return;
        }

        // 保存vms信息
        saveVmsMapping(request.getDeskId());

        try {
            LOGGER.debug("准备记录创建IDV云桌面[id:{}]日志", request.getDeskId());
            DesktopOpLogDTO desktopOpLogRequest = buildDesktopOpLogRequest(request.getDeskId(), BusinessKey.RCDC_USER_CLOUDDESKTOP_CREATE_SUC_LOG);
            desktopOpLogRequest.setEventName(DesktopOpEvent.CREATE);
            desktopOpLogRequest.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);

            UserDesktopEntity deskInfo = userDesktopDAO.findByCbbDesktopId(request.getDeskId());
            desktopOpLogRequest.setTerminalId(deskInfo.getTerminalId());
            desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
        } catch (BusinessException e) {
            LOGGER.error("记录创建IDV云桌面[id:{}]日志出现异常:{}", request.getDeskId(), e);
        }

        // 通知 uws 桌面新建
        uwsDockingAPI.notifyDesktopAdd(request.getDeskId());
    }

    private void saveVmsMapping(UUID deskId) {
        SaveVmsMappingRequest saveRequest = new SaveVmsMappingRequest();
        saveRequest.setBusinessId(deskId);
        saveRequest.setBusinessType(ProductTypeEnum.RCO);
        cbbVmsMappingAPI.saveVmsMappingInfo(saveRequest);
    }
}
