package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopOpLogDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DeleteIDVDesktopOperateLogTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/6/3
 *
 * @author chen zj
 */
@Service
public class IDVDesktopOperateLogHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDVDesktopOperateLogHelper.class);

    /**
     * 公用终端无用户名,使用"--"占位
     */
    private static final String DEFAULT_USER_NAME = "--";

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbClusterServerMgmtAPI clusterServerMgmtAPI;



    /**
     * 记录删除终端审计日志
     *
     * @param deskId 云桌面ID
     * @param operateLogTypeEnums 操作入口类型
     * @return DesktopOpLogRequest
     * @throws BusinessException 业务异常
     */
    public DesktopOpLogDTO buildDesktopOpLogRequest(UUID deskId, DeleteIDVDesktopOperateLogTypeEnums operateLogTypeEnums) throws BusinessException {
        Assert.notNull(deskId, "Param [deskId] must not be null");
        Assert.notNull(operateLogTypeEnums, "Param [operateLogTypeEnums] must not be null");

        DesktopOpLogDTO desktopOpLogRequest = new DesktopOpLogDTO();
        desktopOpLogRequest.setDesktopId(deskId);
        desktopOpLogRequest.setEventName(DesktopOpEvent.DELETE);
        desktopOpLogRequest.setOperatorType(DesktopOpType.ADMIN_FROM_WEB);
        desktopOpLogRequest.setSourceIp(getClusterVirtualIp());
        UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(deskId);
        desktopOpLogRequest.setDesktopName(userDesk.getDesktopName());
        String userName = buildUserOperatorInfo(deskId, desktopOpLogRequest);
        desktopOpLogRequest.setDetail(LocaleI18nResolver.resolve(
                BusinessKey.RCDC_USER_DESKTOP_OPLOG_DELETE_DESKTOP_PRE + operateLogTypeEnums.name().toLowerCase(),
                getUserNameOrTerminalMacWhitBusinessKeyParam(userName, userDesk.getTerminalId(), operateLogTypeEnums), userDesk.getDesktopName()));
        desktopOpLogRequest.setTerminalId(userDesk.getTerminalId());
        return desktopOpLogRequest;
    }



    /**
     * 获取国际化参数
     *
     * @param userName 用户名
     * @param terminalId 终端标识
     * @param operateLogTypeEnums 引起删除云桌面源
     * @return 用户名或终端mac
     * @throws BusinessException 业务异常
     */
    private String getUserNameOrTerminalMacWhitBusinessKeyParam(String userName, String terminalId,
            DeleteIDVDesktopOperateLogTypeEnums operateLogTypeEnums) throws BusinessException {
        switch (operateLogTypeEnums) {
            case INIT_TERMINAL:
            case DELETE_TERMINAL:
                return getTerminalMac(terminalId);
            case DELETE_USER:
            case CLOSE_USER_IDV_CONFIG:
                return userName;
            default:
                return DEFAULT_USER_NAME;
        }
    }

    /**
     * 获取用户名
     *
     * @param deskId 桌面ID
     * @param desktopOpLogRequest 审计日志request
     * @return 用户名
     * @throws BusinessException 业务异常
     */
    private String buildUserOperatorInfo(UUID deskId, CbbDesktopOpLogDTO desktopOpLogRequest) throws BusinessException {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
        if (userDesktopEntity != null && userDesktopEntity.getUserId() != null) {
            IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(userDesktopEntity.getUserId());
            String userName = userDetailResponse.getUserName();
            LOGGER.info("IDV云桌面[id:{}]为绑定用户,查询用户名[{}]", deskId, userName);
            desktopOpLogRequest.setOperatorName(userName);
            desktopOpLogRequest.setOperatorId(userDetailResponse.getId());
        } else {
            desktopOpLogRequest.setOperatorName(DEFAULT_USER_NAME);
        }

        return desktopOpLogRequest.getOperatorName();
    }

    /**
     * 获取终端mac
     *
     * @param terminalId 终端标识
     * @return 终端mac
     * @throws BusinessException 业务异常
     */
    private String getTerminalMac(String terminalId) throws BusinessException {
        CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        return response.getMacAddr();
    }

    /**
     * 获取服务器虚IP
     *
     * @return 服务器虚IP
     * @throws BusinessException 业务异常
     */
    private String getClusterVirtualIp() throws BusinessException {
        CbbClusterVirtualIpDTO cbbClusterVirtualIpDTO = clusterServerMgmtAPI.getClusterVirtualIp();
        return cbbClusterVirtualIpDTO.getClusterVirtualIp();
    }
}
