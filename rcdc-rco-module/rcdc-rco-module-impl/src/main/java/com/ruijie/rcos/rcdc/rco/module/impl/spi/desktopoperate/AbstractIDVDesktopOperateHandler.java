package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopOpLogDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
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
 * Create Time: 2020/5/27
 *
 * @author chen zj
 */
public abstract class AbstractIDVDesktopOperateHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIDVDesktopOperateHandler.class);

    /**
     * 公用终端无用户名,使用"--"占位
     */
    private static final String USER_NAME = "--";

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws Exception {
        Assert.notNull(request, "Param [request] must not be null");
        Assert.notNull(request.getDeskId(), "Param [request.getDeskId] must not be null");

        if (isNotIDVDesktop(request.getDeskId())) {
            return;
        }

        doProcessMessage(request);
    }

    protected abstract void doProcessMessage(CbbDeskOperateNotifyRequest request);


    /**
     * businessKey:国际化key
     *
     * @param deskId      云桌面ID
     * @param businessKey 国际化key
     * @return DesktopOpLogRequest
     * @throws BusinessException 业务异常
     */
    protected DesktopOpLogDTO buildDesktopOpLogRequest(UUID deskId, String businessKey) throws BusinessException {
        DesktopOpLogDTO desktopOpLogRequest = new DesktopOpLogDTO();
        desktopOpLogRequest.setDesktopId(deskId);
        UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(deskId);
        desktopOpLogRequest.setDesktopName(userDesk.getDesktopName());
        desktopOpLogRequest.setSourceIp(getTerminalIpByTerminalId(userDesk.getTerminalId()));
        String userName = buildUserOperatorInfo(deskId, desktopOpLogRequest);
        desktopOpLogRequest.setDetail(LocaleI18nResolver.resolve(businessKey, userName, userDesk.getDesktopName()));
        return desktopOpLogRequest;
    }

    /**
     * businessKey:国际化key , String...param 国际化中参数值
     *
     * @param deskId      云桌面ID
     * @param businessKey 国际化key
     * @return DesktopOpLogRequest
     * @throws BusinessException 业务异常
     */
    protected CbbDesktopOpLogDTO buildDesktopOpLogRequest(UUID deskId, String businessKey, String... params) throws BusinessException {
        CbbDesktopOpLogDTO desktopOpLogRequest = new CbbDesktopOpLogDTO();
        desktopOpLogRequest.setDesktopId(deskId);
        UserDesktopEntity userDesk = userDesktopDAO.findByCbbDesktopId(deskId);
        desktopOpLogRequest.setDesktopName(userDesk.getDesktopName());
        buildUserOperatorInfo(deskId, desktopOpLogRequest);
        desktopOpLogRequest.setSourceIp(getTerminalIpByTerminalId(userDesk.getTerminalId()));
        desktopOpLogRequest.setDetail(LocaleI18nResolver.resolve(businessKey, params));

        return desktopOpLogRequest;
    }

    private String getTerminalIpByTerminalId(String terminalId) throws BusinessException {
        CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        return response.getIp();
    }

    /**
     * 判断是否已IDV桌面
     *
     * @param deskId cbb云桌面ID
     * @return true: 非IDV类型
     * @throws BusinessException 业务异常
     */
    private boolean isNotIDVDesktop(UUID deskId) throws BusinessException {
        CbbDeskDTO cbbDesktopDTO = cbbIDVDeskMgmtAPI.getDeskIDV(deskId);
        if (CbbCloudDeskType.IDV != cbbDesktopDTO.getDeskType()) {
            LOGGER.info("云桌面类型非IDV, 当前业务无需处理");
            return true;
        }

        return false;
    }

    /**
     * 获取用户名, 公用终端使用"--"替代
     *
     * @param deskId cbb组件桌面id
     * @return 用户名
     * @throws BusinessException 业务异常
     */
    protected String buildUserOperatorInfo(UUID deskId, CbbDesktopOpLogDTO desktopOpLogRequest) throws BusinessException {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
        if (userDesktopEntity != null && userDesktopEntity.getUserId() != null) {
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userDesktopEntity.getUserId());

            LOGGER.info("IDV云桌面[id:{}]为绑定用户,查询用户名[{}]", deskId, userDetail.getUserName());
            desktopOpLogRequest.setOperatorName(userDetail.getUserName());
            desktopOpLogRequest.setOperatorId(userDetail.getId());
        } else {
            desktopOpLogRequest.setOperatorName(USER_NAME);
        }

        return desktopOpLogRequest.getOperatorName();
    }
}
