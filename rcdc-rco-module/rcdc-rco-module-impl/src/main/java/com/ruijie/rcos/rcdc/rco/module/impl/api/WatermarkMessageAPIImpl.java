package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWatermarkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WatermarkMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.WatermarkConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.WatermarkMessageSender;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.CommonInfoProducerAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月2日
 *
 * @author XiaoJiaXin
 */
public class WatermarkMessageAPIImpl implements WatermarkMessageAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkMessageAPIImpl.class);

    @Autowired
    private WatermarkMessageSender watermarkMessageSender;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CommonInfoProducerAPI commonInfoProducerAPI;

    @Autowired
    private CbbWatermarkMgmtAPI cbbWatermarkMgmtAPI;

    @Override
    public void send() throws BusinessException {
        // 获取所有RUNNING状态云桌面的信息
        List<CloudDesktopDetailDTO> desktopList = userDesktopMgmtAPI.getSendGlobalWatermarkDesktopList();
        if (CollectionUtils.isEmpty(desktopList)) {
            LOGGER.info("没有运行中的桌面，不下发水印");
            return;
        }
        LOGGER.info("有{}个运行中的桌面需要下发水印信息", desktopList.size());
        for (CloudDesktopDetailDTO cloudDesktopDetailDTO : desktopList) {
            try {
                watermarkMessageSender.send(cloudDesktopDetailDTO);
            } catch (Exception e) {
                LOGGER.error("发送水印信息给桌面[" + cloudDesktopDetailDTO.getDesktopName() + "]失败", e);
            }
        }
    }

    @Override
    public void notifyConfigUpdate() {
        CbbWatermarkConfigDTO cbbWatermarkConfigDTO = cbbWatermarkMgmtAPI.getWatermarkConfig();
        if (cbbWatermarkConfigDTO == null) {
            LOGGER.info("水印配置不存在，不发送通知");
            return;
        }

        try {
            LOGGER.info("发送水印配置变更通知消息");
            WatermarkConfigDTO watermarkConfigDTO = new WatermarkConfigDTO();
            watermarkConfigDTO.setEnable(cbbWatermarkConfigDTO.getEnable());
            watermarkConfigDTO.setDisplayConfig(cbbWatermarkConfigDTO.getDisplayConfig());
            WatermarkConfigDTO.WatermarkDisplayContent displayContent = JSON.parseObject(cbbWatermarkConfigDTO.getDisplayContent(),
                    WatermarkConfigDTO.WatermarkDisplayContent.class);
            watermarkConfigDTO.setDisplayContent(displayContent);
            commonInfoProducerAPI.notifyWatermarkUpdate(watermarkConfigDTO);
        } catch (Exception ex) {
            LOGGER.error("水印配置变更通知消息发送失败，ex :", ex);
        }
    }

    @Override
    public void sendToDesktopList(List<CloudDesktopDetailDTO> desktopList, @Nullable CbbWatermarkConfigDTO watermarkConfig) {
        Assert.notNull(desktopList, "desktopList cannot be null");

        if (CollectionUtils.isEmpty(desktopList)) {
            LOGGER.info("没有运行中的桌面，不下发水印");
            return;
        }
        for (CloudDesktopDetailDTO desktopDetail : desktopList) {
            try {
                if (Objects.isNull(watermarkConfig)) {
                    watermarkMessageSender.send(desktopDetail);
                } else {
                    watermarkMessageSender.sendWatermarkMessage(desktopDetail, watermarkConfig);
                }
            } catch (Exception e) {
                LOGGER.error("发送水印信息给桌面[{}]失败", desktopDetail.getDesktopName(), e);
            }
        }
    }

    @Override
    public void sendToRcaHostDesktopList(List<RcaHostDesktopDTO> rcaHostDesktopList, @Nullable CbbWatermarkConfigDTO watermarkConfig) {
        Assert.notNull(rcaHostDesktopList, "rcaHostDesktopList cannot be null");

        if (CollectionUtils.isEmpty(rcaHostDesktopList)) {
            LOGGER.info("没有运行中的桌面，不下发水印");
            return;
        }
        for (RcaHostDesktopDTO rcaHostDesktopDTO : rcaHostDesktopList) {
            try {
                watermarkMessageSender.sendWatermarkMessage(rcaHostDesktopDTO, null, watermarkConfig);
            } catch (Exception e) {
                LOGGER.error("发送水印信息给VDI应用桌面[{}]失败", rcaHostDesktopDTO.getId(), e);
            }
        }
    }

    @Override
    public void sendToRcaHostDesktopMultiSessionId(RcaHostDesktopDTO rcaHostDesktop,
                                                   @Nullable UUID multiSessionId, @Nullable CbbWatermarkConfigDTO watermarkConfig) {
        Assert.notNull(rcaHostDesktop, "rcaHostDesktop cannot be null");

        try {
            watermarkMessageSender.sendWatermarkMessage(rcaHostDesktop, multiSessionId, watermarkConfig);
        } catch (Exception e) {
            LOGGER.error("发送水印信息给VDI应用桌面[{}],multiSessionId[{}]失败", rcaHostDesktop.getId(), multiSessionId, e);
        }
    }

    @Override
    public void sendToRcaHostList(List<RcaHostDTO> rcaHostList, @Nullable CbbWatermarkConfigDTO watermarkConfig) {
        Assert.notNull(rcaHostList, "rcaHostList cannot be null");

        if (CollectionUtils.isEmpty(rcaHostList)) {
            LOGGER.info("没有运行中的桌面，不下发水印");
            return;
        }
        for (RcaHostDTO rcaHostDTO : rcaHostList) {
            try {
                watermarkMessageSender.sendWatermarkMessage(rcaHostDTO, null, watermarkConfig);
            } catch (Exception e) {
                LOGGER.error("发送水印信息给第三方应用主机[{}]失败", rcaHostDTO.getId(), e);
            }
        }
    }

    @Override
    public void sendToRcaHostListMultiSessionId(RcaHostDTO rcaHostDTO,
                                                @Nullable UUID multiSessionId, @Nullable CbbWatermarkConfigDTO watermarkConfig) {
        Assert.notNull(rcaHostDTO, "rcaHostDTO cannot be null");

        try {
            watermarkMessageSender.sendWatermarkMessage(rcaHostDTO, multiSessionId, watermarkConfig);
        } catch (Exception e) {
            LOGGER.error("发送水印信息给第三方应用主机[{}],multiSessionId[{}]失败", rcaHostDTO.getId(), multiSessionId, e);
        }
    }
}
