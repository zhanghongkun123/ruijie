package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVmsMappingAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopGuestToolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DeskFaultInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolComputerService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeSubTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Description: 处理回收站删除云桌面消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月22日
 *
 * @author zhuangchenwu
 */
@Service
public class RecycleBinDeleteDeskHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecycleBinDeleteDeskHandler.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private DeskFaultInfoDAO deskFaultInfoDAO;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private DistributeSubTaskService distributeSubTaskService;

    @Autowired
    private DesktopGuestToolMgmtAPI desktopGuestToolMgmtAPI;

    @Autowired
    private CbbVmsMappingAPI cbbVmsMappingAPI;

    @Autowired
    private DesktopPoolComputerService desktopPoolComputerService;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.COMPLETE_DELETE_DESK == request.getOperateType()
                || request.getOperateType() == CbbCloudDeskOperateType.DELETE_IDV_DESK;
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws Exception {
        if (request.getIsSuccess()) {
            LOGGER.info("收到桌面组件通知，回收站删除桌面完成, 桌面id[{}]", request.getDeskId());
            userDesktopDAO.deleteByCbbDesktopId(request.getDeskId());
            deskFaultInfoDAO.deleteByDeskId(request.getDeskId());
            uwsDockingAPI.notifyDesktopDelete(request.getDeskId());
            rcoDeskInfoDAO.deleteByDeskId(request.getDeskId());
            distributeSubTaskService.deleteByTargetId(request.getDeskId());
            desktopGuestToolMgmtAPI.releaseGuestToolAlarm(request.getDeskId());
            // 删除vms信息
            cbbVmsMappingAPI.deleteVmsMappingByBusinessId(request.getDeskId());

            desktopPoolComputerService.deleteByRelatedId(request.getDeskId());
            hostUserService.deleteByDeskId(request.getDeskId());
            computerBusinessService.updateWorkModel(request.getDeskId(), null);
            return;
        }
        LOGGER.error("收到回收站删除桌面[{}] 失败消息[{}]", request.getDeskId(), request.getErrorMsg());
    }
}
