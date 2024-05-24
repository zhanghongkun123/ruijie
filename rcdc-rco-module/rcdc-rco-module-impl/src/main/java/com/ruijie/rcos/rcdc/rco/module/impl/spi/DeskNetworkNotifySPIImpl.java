package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.callback.CbbDeskNetworkCallback;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.DeskNetworkChangeEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskNetworkDeleteNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.response.CbbDeskNetworkDeleteNotifyResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserGroupDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserGroupDesktopConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UamAppService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

;

/**
 * Description: 校验删除云桌面网络策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/5/27
 *
 * @author nt
 */
public class DeskNetworkNotifySPIImpl implements CbbDeskNetworkDeleteNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskNetworkNotifySPIImpl.class);

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserGroupDesktopConfigDAO userGroupDesktopConfigDAO;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private UamAppService uamAppService;



    @Override
    public CbbDeskNetworkDeleteNotifyResponse beforeDeleteDeskNetwork(UUID deskNetworkId,
                                                                CbbDeskNetworkCallback callback) throws BusinessException {
        Assert.notNull(deskNetworkId, "deskNetworkId can not be null");

        LOGGER.info("开始校验删除云桌面网络策略");

        // 校验是否存在关联的云桌面
        checkRelativeDesktop(deskNetworkId);

        // 校验是否存在关联的用户组
        checkRelativeUserGroup(deskNetworkId);

        uamAppService.isExistRelateAppByNetworkIdThrowEx(deskNetworkId);

        LOGGER.info("校验删除云桌面网络策略通过");
        return new CbbDeskNetworkDeleteNotifyResponse();
    }

    private void checkRelativeUserGroup(UUID deskNetworkId) throws BusinessException {
        UserGroupDesktopConfigEntity configEntity = userGroupDesktopConfigDAO.findFirstByNetworkId(deskNetworkId);
        if (configEntity != null) {
            IacUserGroupDetailDTO groupDetail = cbbUserGroupAPI.getUserGroupDetail(configEntity.getGroupId());
            String deskNetworkName = getDeskNetworkName(deskNetworkId);
            LOGGER.warn("网络策略[{}]存在关联的用户组[{}]", deskNetworkName, groupDetail.getName());
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_RELATIVE_GROUP,
                    deskNetworkName, groupDetail.getName());
        }
    }

    private void checkRelativeDesktop(UUID deskNetworkId) throws BusinessException {
        ViewUserDesktopEntity viewUserDesktopEntity = viewDesktopDetailDAO.findFirstByCbbNetworkId(deskNetworkId);
        if (viewUserDesktopEntity != null) {
            String deskNetworkName = getDeskNetworkName(deskNetworkId);
            LOGGER.warn("网络策略[{}]存在关联的云桌面", deskNetworkName);
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NETWORK_STRATEGY_HAS_VM, deskNetworkName);
        }
    }

    private String getDeskNetworkName(UUID deskNetworkId) throws BusinessException {
        CbbDeskNetworkDetailDTO deskNetworkDTO = cbbNetworkMgmtAPI.getDeskNetwork(deskNetworkId);
        if (deskNetworkDTO == null) {
            //返回空
            return null;
        }
        return deskNetworkDTO.getDeskNetworkName();
    }

    @Override
    public DefaultResponse afterDeleteDeskNetwork(DeskNetworkChangeEvent deskNetworkChangeEvent) {
        Assert.notNull(deskNetworkChangeEvent, "deskNetworkChangeEvent can not be null");

        LOGGER.info("收到网络策略删除通知：NetworkId:[{}]，RCO默认不做处理", deskNetworkChangeEvent.getNetworkId());

        return DefaultResponse.Builder.success();
    }
}
