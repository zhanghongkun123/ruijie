package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskOperateType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 处理软删除云桌面消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月8日
 *
 * @author yinfeng
 */
@Service
public class SoftDeleteDeskHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDeskHandler.class);


    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.SOFT_DELETE_DESK == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws BusinessException {
        if (!request.getIsSuccess()) {
            LOGGER.error("收到软删除云桌面[{}] 失败消息[{}]，删除 user desktop 数据", request.getDeskId(),
                    request.getErrorMsg());
            return;
        }
        //移动到回收站成功后，存在rccm纳管，需要通知rccm处理用户集群关系缓存
        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        if (rccmServerConfig != null && rccmServerConfig.hasHealth()) {
            UserDesktopEntity userDesktopEntity = userDesktopService.findByDeskId(request.getDeskId());
            if (userDesktopEntity != null && userDesktopEntity.getUserId() != null) {
                IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userDesktopEntity.getUserId());
                LOGGER.info("收到软删除云桌面[{}]成功,开启判断用户[{}],是否需要删除rcm集群关系缓存", request.getDeskId(), userDetail.getUserName());
                rccmManageAPI.delRccmUserCLuster(Collections.singletonList(userDetail.getUserName()), false);
            }
        }
        LOGGER.info("收到软删除云桌面消息, 桌面id[{}]", request.getDeskId());
        // 通知UWS VDI 删除
        uwsDockingAPI.notifyDesktopDelete(request.getDeskId());
    }
}
