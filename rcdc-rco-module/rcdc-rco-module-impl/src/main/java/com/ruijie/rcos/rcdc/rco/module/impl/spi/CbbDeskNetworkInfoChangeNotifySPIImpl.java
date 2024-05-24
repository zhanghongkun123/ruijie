package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbDeskNetworkInfoChangeNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PublicBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.WatermarkMessageSender;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.dao.UserLoginRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.entity.UserLoginRecordEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/3 13:49
 *
 * @author ketb
 */
public class CbbDeskNetworkInfoChangeNotifySPIImpl implements CbbDeskNetworkInfoChangeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbDeskNetworkInfoChangeNotifySPIImpl.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private WatermarkMessageSender watermarkMessageSender;

    @Autowired
    private UserLoginRecordDAO userLoginRecordDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public void afterSaveDeskIp(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskid can not be null");

        LOGGER.info("接收到桌面[{}]网络变更的消息", deskId);
        CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        if (desktopDetailDTO == null) {
            LOGGER.error("桌面[{}]不存在", deskId);
            throw new BusinessException(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_NOT_EXIST, String.valueOf(deskId));
        }

        CbbDeskDTO cbbDeskDTO = new CbbDeskDTO();
        try {
            // 更新用户报表记录桌面IP
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
            UserLoginRecordEntity userLoginRecordEntity =
                    userLoginRecordDAO.findFirstByDeskIdOrderByCreateTimeDesc(deskId.toString());
            if (Objects.nonNull(userLoginRecordEntity)) {
                userLoginRecordEntity.setDeskIp(cbbDeskDTO.getDeskIp());
                userLoginRecordDAO.save(userLoginRecordEntity);
            }
        } catch (Exception e) {
            LOGGER.error("更新用户信息报表云桌面ip失败，桌面[{}]，桌面详情[{}]", deskId, JSON.toJSON(cbbDeskDTO), e);
        }


        // 判断云桌面状态是否还处于RUNNING或START_UP
        boolean isDeskStateCorrect = isDesktopRunningOrStartUp(desktopDetailDTO.getDesktopState());
        if (!isDeskStateCorrect) {
            LOGGER.error("桌面[{}]未处于启动或运行中状态", deskId);
            throw new BusinessException(BusinessKey.RCDC_RCO_WATERMARK_DESKTOP_STATE_IS_NOT_RUNNING);
        }

        if (PublicBusinessKey.DEFAULT_EMPTY_USERNAME.equals(desktopDetailDTO.getUserName())) {
            // 公用终端显示--太难看了，不显示用户名
            desktopDetailDTO.setUserName("");
        }
        deskStrategyAPI.sendDesktopStrategyWatermark(desktopDetailDTO);
    }

    private boolean isDesktopRunningOrStartUp(String desktopState) {
        LOGGER.debug("桌面状态为：{} ", desktopState);
        if (desktopState.equals(CbbCloudDeskState.RUNNING.name())) {
            return true;
        }

        return desktopState.equals(CbbCloudDeskState.START_UP.name());
    }

    @Override
    public void deskMacRefreshed(UUID deskId) throws BusinessException {
        // 云桌面网络mac信息更新后通知 (课堂需要接口，目前办公未用到，空实现就可)
    }
}
