package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.QueryDesktopItemDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ListUserVdiDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedUserDesktopResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.QueryDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: shine请求查询云桌面列表
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月29日
 *
 * @author Administrator
 */
@DispatcherImplemetion(ShineAction.RCDC_SHINE_CMM_QUERY_DESKTOP)
public class QueryDesktopHandleSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryDesktopHandleSPIImpl.class);

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request is null");
        String terminalId = request.getTerminalId();
        // 开启统一登录且是软终端就向集群管理中心请求云桌面列表
        if (userLoginRccmOperationService.isUnifiedLoginOn(terminalId)) {
            // 开启统一登录向集群管理中心请求云桌面列表
            if (responseByQueryInRccm(request)) {
                return;
            }
            // 请求rccm异常走本机逻辑
            LOGGER.error("请求rccm异常走本机逻辑，request=[{}]", JSON.toJSONString(request));
        }

        UserInfo userInfo = userLoginSession.getLoginUserInfo(terminalId);
        if (userInfo == null) {
            LOGGER.error("终端[{}]未登录用户", request.getTerminalId());
            responseByCode( request, CommonMessageCode.CODE_USER_NO_LOGIN);
            return;
        }

        ListUserVdiDesktopRequest listUserVdiDeskRequest = new ListUserVdiDesktopRequest();
        listUserVdiDeskRequest.setUserId(userInfo.getUserId());
        listUserVdiDeskRequest.setUserName(userInfo.getUserName());
        listUserVdiDeskRequest.setUserType(userInfo.getUserType());
        listUserVdiDeskRequest.setTerminalId(terminalId);
        // 查询用户绑定的云桌面（不包括回收站中的桌面）
        List<QueryDesktopItemDTO> itemList = userDesktopMgmtAPI.listUserVDIDesktop(listUserVdiDeskRequest);
        responseMessage(request, itemList);
    }

    /**
     * 向集群管理中心请求云桌面列表，并返回消息给客户端
     *
     * @param request CbbDispatcherRequest
     */
    private boolean responseByQueryInRccm(CbbDispatcherRequest request) {
        List<UnifiedUserDesktopResultDTO> desktopResultDTOList;
        try {
            desktopResultDTOList = userLoginRccmOperationService.requestUserVDIDesktopInRccm(request.getTerminalId(), true);
            responseMessage(request, desktopResultDTOList);
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("终端[%s]向r-center查询云桌面列表异常", request.getTerminalId()), e);
            return false;
        }
    }

    private void responseError(CbbDispatcherRequest request) {
        responseByCode(request, CommonMessageCode.CODE_ERR_OTHER);
    }

    private void responseByCode(CbbDispatcherRequest request, int code) {
        try {
            // 返回错误消息给shine
            shineMessageHandler.response(request, code);
        } catch (Exception e) {
            LOGGER.error("终端[" + request.getTerminalId() + "]查询云桌面列表应答消息失败,", e);
        }
    }



    private void responseMessage(CbbDispatcherRequest request, List<? extends QueryDesktopItemDTO> itemList) {
        try {
            // 桌面列表以桌面名称排序
            if (!itemList.isEmpty()) {
                itemList.sort(Comparator.comparing(QueryDesktopItemDTO::getDesktopName));
            }
            shineMessageHandler.responseSuccessContent(request, ImmutableMap.of("desktopList", itemList));
        } catch (Exception e) {
            LOGGER.error("终端[" + request.getTerminalId() + "]查询云桌面列表应答消息失败", e);
        }
    }
}
