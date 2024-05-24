package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 16:58
 *
 * @author chenl
 */
@Service
public class AuditApplyUpdateNotifyServiceImpl implements AuditApplyUpdateNotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditApplyUpdateNotifyServiceImpl.class);

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Override
    public void notifyGuestToolAuditApplyDetail(AuditApplyDetailDTO auditApplyDetailDTO) {
        Assert.notNull(auditApplyDetailDTO, "auditFileApplyDetail notNull");
        Assert.notNull(auditApplyDetailDTO.getId(), "applyId notNull");
        Assert.notNull(auditApplyDetailDTO.getDesktopId(), "desktopId notNull");
        try {
            if (auditApplyDetailDTO.getDesktopPoolType() == DesktopPoolType.DYNAMIC) {
                UUID useDesktopId = desktopPoolUserService.findDeskRunningByDesktopPoolIdAndUserId(
                        auditApplyDetailDTO.getDesktopPoolId(), auditApplyDetailDTO.getUserId());
                if (Objects.isNull(useDesktopId)) {
                    LOGGER.info("用户[{}]申请单[{}]在动态池[{}]无运行中的桌面, 不向GT发送请求", 
                            auditApplyDetailDTO.getUserId(), auditApplyDetailDTO.getId(), auditApplyDetailDTO.getDesktopPoolId());
                    return;
                }
                auditApplyDetailDTO.setDesktopId(useDesktopId);
            } else {
                CloudDesktopDetailDTO deskDetail = queryCloudDesktopService.queryDeskDetail(auditApplyDetailDTO.getDesktopId());
                // 申请单所属用户与桌面所属不一致或桌面不在线则不发送请求
                if (!Objects.equals(deskDetail.getUserId(), auditApplyDetailDTO.getUserId()) 
                        || !Objects.equals(CbbCloudDeskState.RUNNING.name(), deskDetail.getDesktopState())) {
                    LOGGER.info("不向非运行中的GT发送申请单[{}]详情,当前云桌面[{}]状态[{}]", 
                            auditApplyDetailDTO.getId(), deskDetail.getDesktopName(), deskDetail.getDesktopState());
                    return;
                }  
            }
            
            GuesttoolMessageContent body = new GuesttoolMessageContent();
            body.setCode(CommonMessageCode.SUCCESS);
            body.setContent(auditApplyDetailDTO);
            CbbGuesttoolMessageDTO responseBody = new CbbGuesttoolMessageDTO();
            responseBody.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_AUDIT_FILE);
            responseBody.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_CMD_ID_AUDIT_APPLY_DETAIL));
            responseBody.setDeskId(auditApplyDetailDTO.getDesktopId());
            responseBody.setBody(JSON.toJSONString(body));
            guestToolMessageAPI.asyncRequest(responseBody);
        } catch (Exception e) {
            LOGGER.error("向GT发送申请单[{}]详情出现异常：", auditApplyDetailDTO.getId(), e);
        }
    }

}
