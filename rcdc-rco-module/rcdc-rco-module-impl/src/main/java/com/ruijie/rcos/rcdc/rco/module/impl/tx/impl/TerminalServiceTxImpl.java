package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDeskBasicInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WatermarkMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CreateDesktopHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.TerminalServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/6/18
 *
 * @author nt
 */
@Service
public class TerminalServiceTxImpl implements TerminalServiceTx {
    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalServiceTxImpl.class);

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CreateDesktopHelper createDesktopHelper;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public void deleteById(UserTerminalEntity terminal) {
        Assert.notNull(terminal, "terminal can not be null");

        LOGGER.info("删除终端，解绑终端云桌面，终端id：{}", terminal.getId());
        cloudDesktopOperateService.unbindDesktopTerminalForDeleteTerminal(terminal);
        userTerminalDAO.deleteById(terminal.getId());

        // bindDeskId为idv终端绑定的桌面id，删除idv终端记录时，需要同步删除用户-桌面关系表记录
        UUID bindDeskId = terminal.getBindDeskId();
        LOGGER.info("删除的终端信息为：terminal={}", JSONObject.toJSONString(terminal));
        if (null != bindDeskId) {
            userDesktopDAO.deleteByCbbDesktopId(bindDeskId);
        }
    }

    @Override
    public void bindUser(UserTerminalEntity userTerminalEntity, IacUserDetailDTO userDetail) throws BusinessException {
        Assert.notNull(userTerminalEntity, "userTerminalEntity can not be null");
        Assert.notNull(userDetail, "userDetail can not be null");
        CbbDeskDTO cbbDeskDTO = cbbIDVDeskMgmtAPI.getDeskIDV(userTerminalEntity.getBindDeskId());
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(cbbDeskDTO.getImageTemplateId());
        UUID bindUserId = userDetail.getId();
        userTerminalEntity.setBindUserId(bindUserId);
        userTerminalEntity.setBindUserName(userDetail.getUserName());
        userTerminalEntity.setBindUserTime(new Date());
        userTerminalDAO.save(userTerminalEntity);

        String desktopName = createDesktopHelper.generateDesktopName(userDetail);
        UserDesktopEntity userDesktop = userDesktopDAO.findUserDesktopEntityByTerminalId(userTerminalEntity.getTerminalId());
        userDesktop.setUserId(bindUserId);
        userDesktop.setDesktopName(desktopName);
        userDesktopDAO.save(userDesktop);

        CbbDeskBasicInfoDTO cbbDeskBasicInfoDTO = new CbbDeskBasicInfoDTO();
        cbbDeskBasicInfoDTO.setDeskId(userTerminalEntity.getBindDeskId());
        cbbDeskBasicInfoDTO.setDesktopName(desktopName);
        cbbDeskBasicInfoDTO.setMac(cbbDeskDTO.getDeskMac());
        cbbIDVDeskMgmtAPI.updateIDVDesktopBasicInfo(cbbDeskBasicInfoDTO);

        // 判断是否自动开启用户的IDV/VDI桌面配置
        CbbImageType cbbImageType = imageTemplateDetail.getCbbImageType();
        UserCloudDeskTypeEnum deskTypeEnum = UserCloudDeskTypeEnum.valueOf(cbbImageType.name());
        UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigAPI.getUserDesktopConfig(bindUserId, deskTypeEnum);
        // 用户终端桌面配置存在,但配置字段可能为空,需要单独判断相应的字段是否为空
        if (userDesktopConfigDTO == null || userDesktopConfigDTO.getImageTemplateId() == null) {
            CreateUserDesktopConfigRequest configRequest = new CreateUserDesktopConfigRequest(bindUserId, deskTypeEnum);
            configRequest.setImageTemplateId(cbbDeskDTO.getImageTemplateId());
            configRequest.setStrategyId(cbbDeskDTO.getStrategyId());
            userDesktopConfigAPI.createOrUpdateUserDesktopConfig(configRequest);
            auditLogAPI.recordLog(BusinessKey.RCDC_USER_EDIT_SUCCESS_LOG, userDetail.getUserName());
        }
    }

}
