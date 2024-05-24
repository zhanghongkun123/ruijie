package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskOperateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbVmStopNotifySPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.service.DesktopUpdateService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.StopVmRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/22
 *
 * @author hs
 */
public class StopVmNotifySPIImpl implements CbbVmStopNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopVmNotifySPIImpl.class);

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private DesktopUpdateService desktopUpdateService;

    @Autowired
    private CbbThirdPartyDeskOperateMgmtAPI cbbThirdPartyDeskOperateAPI;

    @Override
    public void notifyStopVm(CbbDispatcherDTO dispatcherDTO) {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is null");

        StopVmRequest req = JSON.parseObject(dispatcherDTO.getData(), StopVmRequest.class);
        CbbDispatcherRequest dispatcherRequest = new CbbDispatcherRequest();
        BeanUtils.copyProperties(dispatcherDTO, dispatcherRequest);
        UserDesktopEntity entity = null;
        ViewTerminalEntity terminalView = null;
        try {
            entity = queryCloudDesktopService.checkAndFindById(req.getId());
            terminalView = terminalService.getViewByTerminalId(dispatcherDTO.getTerminalId());
            CbbShutdownDeskVDIDTO cbbReq = new CbbShutdownDeskVDIDTO();
            cbbReq.setId(req.getId());
            // 增加非强制关机的参数，如果参数为空，则保持强制关机逻辑
            cbbReq.setIsForce(req.getForce() != null ? BooleanUtils.toBoolean(req.getForce()) : Boolean.TRUE);

            LOGGER.info("浮动条强制关机，terminalId={},desktopId={}", dispatcherDTO.getTerminalId(), req.getId());
            // 更新缓存信息
            DesktopRequestDTO desktopRequestDTO = new DesktopRequestDTO(dispatcherRequest, false);
            desktopRequestDTO.setDesktopRunInTerminal(true);
            desktopOperateRequestCache.addCache(req.getId(), desktopRequestDTO);

            if (entity.getDesktopType() == CbbCloudDeskType.THIRD) {
                cbbThirdPartyDeskOperateAPI.shutdownDeskThirdParty(cbbReq);
            } else {
                cbbVDIDeskOperateAPI.shutdownDeskVDI(cbbReq);
            }
            logDeskStopSucEvent(entity, terminalView.getIp());
        } catch (BusinessException e) {
            LOGGER.error("dispatch stop vm command fail.", e);
            if (entity != null) {
                desktopOperateRequestCache.addCache(req.getId(), new DesktopRequestDTO(dispatcherRequest, true));
                logDeskStopFailEvent(entity, terminalView == null ? null : terminalView.getIp(), e);
            }
        }

        // 关机后校验，变更策略信息
        desktopUpdateService.updateNotRecoverableVDIConfigAsync(req.getId());
    }

    private void logDeskStopSucEvent(UserDesktopEntity desktop, String sourceIp) throws BusinessException {
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(desktop.getUserId());

        if (userDetail == null) {
            LOGGER.error("用户id为[{}]的用户对象不存在", desktop.getUserId());
            return;
        }
        DesktopOpLogDTO req = createCommonRequest(desktop, userDetail, sourceIp);
        req.setEventName(DesktopOpEvent.POWEROFF);
        req.setDetail(
                LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_STOP_SUC, userDetail.getUserName(), desktop.getDesktopName()));

        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    private void logDeskStopFailEvent(UserDesktopEntity desktop, String sourceIp, BusinessException businessException) {
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
        req.setEventName(DesktopOpEvent.POWEROFF);
        req.setDetail(LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_STOP_FAIL, userDetail.getUserName(),
                desktop.getDesktopName(), businessException.getI18nMessage()));
        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    private DesktopOpLogDTO createCommonRequest(UserDesktopEntity desktop, IacUserDetailDTO userDetail, String sourceIp) {
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
