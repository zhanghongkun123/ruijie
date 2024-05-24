package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dto.CloudDeskRemountDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dto.NotifyCloudDeskRemountDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: CMS独立升级后，发送版本更新事件给RCDC
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
@DispatcherImplemetion(CmsCmdId.RCO_CMD_ID_NOTIFY_CLOUD_DESK_REMOUNT)
public class NotifyCloudDeskRemountSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyCloudDeskRemountSPIImpl.class);

    @Autowired
    CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Autowired
    private CbbGuestToolMessageAPI cbbGuestToolMessageAPI;

    private static final Integer CMS = 0;

    private static final Integer UWS = 1;

    private static final int SUCCESS = 0;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getDto(), "dto can not be null");
        UUID deskId = request.getDto().getDeskId();
        LOGGER.info("云空间独立升级后，通知事件给RCDC");

        CloudDeskRemountDTO cloudDeskRemountDTO = JSON.parseObject(request.getDto().getBody(), CloudDeskRemountDTO.class);
        int index = CMS;
        // 获取对应的VDI虚机，对其进行应用软件ISO热更换
        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
        if (cbbDeskDTO.getDeskState() == CbbCloudDeskState.RUNNING) {
            if (cloudDeskRemountDTO.getIndex() == null) {
                cmsUpgradeAPI.replaceUwsIso(cbbDeskDTO.getDeskId());
                cmsUpgradeAPI.replaceCmsIso(cbbDeskDTO.getDeskId());
            }
            if (cloudDeskRemountDTO.getIndex().equals(CMS)) {
                cmsUpgradeAPI.replaceCmsIso(cbbDeskDTO.getDeskId());
            }
            if (cloudDeskRemountDTO.getIndex().equals(UWS)) {
                cmsUpgradeAPI.replaceUwsIso(cbbDeskDTO.getDeskId());
                index = UWS;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", index);
        jsonObject.put("code", SUCCESS);
        String input = JSONObject.toJSONString(jsonObject);

        final CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO = new CbbGuesttoolMessageDTO();
        cbbGuesttoolMessageDTO.setCmdId(request.getDto().getCmdId());
        cbbGuesttoolMessageDTO.setPortId(request.getDto().getPortId());
        cbbGuesttoolMessageDTO.setBody(input);
        LOGGER.info("receive success");
        // 构造发送消息请求
        sendMsgToDesktop(request);

        return cbbGuesttoolMessageDTO;
    }

    private void sendMsgToDesktop(CbbGuestToolSPIReceiveRequest request) {
        NotifyCloudDeskRemountDTO notifyCloudDeskRemountDTO = buildMessageMessageDTO();
        CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO = buildSendMessageRequest(request, notifyCloudDeskRemountDTO);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("发送用户消息, 请求内容{}", JSON.toJSONString(cbbGuesttoolMessageDTO));
        }
        try {
            cbbGuestToolMessageAPI.syncRequest(cbbGuesttoolMessageDTO);
        } catch (Exception e) {
            LOGGER.error("CMS独立升级后，发送版本更新事件给RCDC失败", e);
        }
    }

    private CbbGuesttoolMessageDTO buildSendMessageRequest(CbbGuestToolSPIReceiveRequest request,
            NotifyCloudDeskRemountDTO notifyCloudDeskRemountDTO) {
        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(request.getDto().getCmdId());
        dto.setPortId(request.getDto().getPortId());
        dto.setDeskId(request.getDto().getDeskId());
        dto.setBody(JSON.toJSONString(notifyCloudDeskRemountDTO));
        return dto;
    }

    private NotifyCloudDeskRemountDTO buildMessageMessageDTO() {
        NotifyCloudDeskRemountDTO notifyCloudDeskRemountDTO = new NotifyCloudDeskRemountDTO();
        notifyCloudDeskRemountDTO.setCode(0);
        notifyCloudDeskRemountDTO.setMessage(StringUtils.EMPTY);
        NotifyCloudDeskRemountDTO.Content content = new NotifyCloudDeskRemountDTO.Content();
        notifyCloudDeskRemountDTO.setContent(content);
        return notifyCloudDeskRemountDTO;
    }
}
