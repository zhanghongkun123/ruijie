package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbVmRebootNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.RebootVmRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/22
 *
 * @author hs
 */
public class RebootVmNotifySPIImpl implements CbbVmRebootNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RebootVmNotifySPIImpl.class);

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;


    @Override
    public void notifyRebootVm(CbbDispatcherDTO dispatcherDTO) {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is null");

        RebootVmRequest req = JSON.parseObject(dispatcherDTO.getData(), RebootVmRequest.class);
        UUID deskId = req.getDeskId();
        ViewUserDesktopEntity entity = null;
        ViewTerminalEntity terminalView = null;
        try {
            entity = queryCloudDesktopService.checkDesktopExistInDeskViewById(deskId);
            terminalView = terminalService.getViewByTerminalId(dispatcherDTO.getTerminalId());

            LOGGER.info("OneClient重启虚机，terminalId={},desktopId={}", dispatcherDTO.getTerminalId(), deskId);

            if (entity.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
                throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_REBOOT_FAIL_NOT_SUPPORT_MULTIPLE_SESSION_DESKTOP, entity.getDesktopName());
            }

            switch (entity.getDesktopType()) {
                case "VDI":
                    cbbVDIDeskOperateAPI.rebootDeskVDI(deskId);
                    break;
                case "THIRD":
                    cloudDesktopOperateAPI.rebootDeskThird(deskId);
                    break;
                default:
                    throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_REBOOT_FAIL_NOT_SUPPORT_DESKTOP_TYPE, entity.getDesktopName(),
                            entity.getDesktopType());
            }

            logDeskRebootSucEvent(entity, terminalView.getIp());
        } catch (BusinessException e) {
            LOGGER.error("OneClient重启虚机出现异常，terminalId={},desktopId={}", dispatcherDTO.getTerminalId(), deskId, e);
            if (entity != null) {
                logDeskRebootFailEvent(entity, terminalView == null ? null : terminalView.getIp(), e);
            }
        }
    }

    private void logDeskRebootSucEvent(ViewUserDesktopEntity desktop, String sourceIp) throws BusinessException {
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(desktop.getUserId());

        if (userDetail == null) {
            LOGGER.error("用户id为[{}]的用户对象不存在", desktop.getUserId());
            return;
        }
        DesktopOpLogDTO req = createCommonRequest(desktop, userDetail, sourceIp);
        req.setEventName(DesktopOpEvent.REBOOT);
        req.setDetail(
                LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_REBOOT_SUC, userDetail.getUserName(), desktop.getDesktopName()));

        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    private void logDeskRebootFailEvent(ViewUserDesktopEntity desktop, String sourceIp, BusinessException businessException) {
        IacUserDetailDTO userDetail = null;
        try {
            userDetail = cbbUserAPI.getUserDetail(desktop.getUserId());
        } catch (BusinessException e) {
            LOGGER.error("获取用户[" + desktop.getUserId() + "异常]", e);
            return;
        }

        if (userDetail == null) {
            LOGGER.error("用户id为[{}]的用户对象不存在", desktop.getUserId());
            return;
        }
        DesktopOpLogDTO req = createCommonRequest(desktop, userDetail, sourceIp);
        req.setEventName(DesktopOpEvent.REBOOT);
        req.setDetail(LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_REBOOT_FAIL, userDetail.getUserName(),
                desktop.getDesktopName(), businessException.getI18nMessage()));
        desktopOpLogMgmtAPI.saveOperateLog(req);
    }


    private DesktopOpLogDTO createCommonRequest(ViewUserDesktopEntity desktop, IacUserDetailDTO userDetail, String sourceIp) {
        DesktopOpLogDTO req = new DesktopOpLogDTO();
        req.setDesktopId(desktop.getCbbDesktopId());
        req.setDesktopName(desktop.getDesktopName());
        req.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);
        req.setOperatorId(userDetail.getId());
        req.setOperatorName(userDetail.getUserName());
        req.setSourceIp(sourceIp);
        req.setTerminalId(desktop.getTerminalId());
        return req;
    }
}
