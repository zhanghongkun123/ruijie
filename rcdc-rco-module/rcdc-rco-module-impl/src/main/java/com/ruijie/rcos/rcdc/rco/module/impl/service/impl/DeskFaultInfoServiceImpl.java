package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;


import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CbbDeskFaultInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistStateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DeskFaultInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskFaultInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.CancelRequestRemoteAssistSrcTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.request.CancelRequestRemoteAssistRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DeskFaultInfoService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RemoteAssistService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.HibernateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/8 17:15
 *
 * @author ketb
 */
@Service
public class DeskFaultInfoServiceImpl implements DeskFaultInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeskFaultInfoServiceImpl.class);

    @Autowired
    private DeskFaultInfoDAO desktopFaultInfoDAO;

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private RemoteAssistService remoteAssistService;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private WebclientNotifyAPI webclientNotifyAPI;

    @Override
    public DeskFaultInfoEntity findByDeskMac(String deskMac) {
        Assert.notNull(deskMac, "deskMac can not be null");
        return desktopFaultInfoDAO.findByMac(deskMac);
    }

    @Override
    public void relieveFault(String deskId) throws BusinessException {
        Assert.hasText(deskId, "computerId不能为空");
        // 不是故障的pc无需解除，由前端限制，后端不做校验
        DeskFaultInfoEntity desktopFaultInfoEntity = desktopFaultInfoDAO.findByDeskId(UUID.fromString(deskId));
        // 向云桌面发送解除故障消息
        try {
            terminalOperatorAPI.relieveFault(desktopFaultInfoEntity.getMac(),
                    new CancelRequestRemoteAssistRequest(CancelRequestRemoteAssistSrcTypeEnum.ADMIN));
        } catch (Exception e) {
            LOGGER.error("send relieve fault msg to <{}> fail", deskId);
        }

        RemoteAssistStateDTO remoteAssistStateDTO = new RemoteAssistStateDTO(UUID.fromString(deskId), false);

        // 向网页版客户端发送解除故障消息
        webclientNotifyAPI.notifyRemoteAssistState(true, remoteAssistStateDTO);

        desktopFaultInfoEntity.setFaultState(false);
        desktopFaultInfoEntity.setFaultTime(new Date());
        desktopFaultInfoEntity.setFaultDescription("");
        desktopFaultInfoDAO.save(desktopFaultInfoEntity);
        // 向终端浮动条发送取消请求远程协助状态
        try {
            // 云桌面不在终端登录时，不存在terminalid，所以需要添加判断以及捕获异常
            UserDesktopEntity userDesktop = userDesktopDAO.findByCbbDesktopId(UUID.fromString(deskId));
            if (!StringUtils.isEmpty(userDesktop.getTerminalId())) {
                remoteAssistService.syncRemoteAssistRequestStatus(userDesktop.getTerminalId(), false);
            }
        } catch (Exception e) {
            LOGGER.error("sync remote assist status to <{}> fail:", deskId, e);
        }
    }

    @Override
    public CbbDeskFaultInfoDTO[] assemblyInfo(UUID[] uuidArr) {
        Assert.notNull(uuidArr, "uuidArr is not be null");
        List<UUID> uuidList = HibernateUtil.handleQueryIncludeList(Arrays.asList(uuidArr));
        List<DeskFaultInfoEntity> faultList = desktopFaultInfoDAO.queryDesignatedDesk(uuidList);
        CbbDeskFaultInfoDTO[] infoArr = new CbbDeskFaultInfoDTO[faultList.size()];
        for (int i = 0; i < faultList.size(); i++) {
            CbbDeskFaultInfoDTO dto = new CbbDeskFaultInfoDTO();
            BeanUtils.copyProperties(faultList.get(i), dto);
            infoArr[i] = dto;
        }
        return infoArr;
    }

    @Override
    public void updateMacByDeskId(UUID deskId, String newMac) {
        Assert.notNull(deskId, "deskId must not null");
        Assert.notNull(newMac, "newMac must not null");

        desktopFaultInfoDAO.updateMac(deskId, newMac);
    }
}
