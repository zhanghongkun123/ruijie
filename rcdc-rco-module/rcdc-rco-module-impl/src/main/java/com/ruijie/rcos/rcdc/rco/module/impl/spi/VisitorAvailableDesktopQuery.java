package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 访客用户查询可用的桌面
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/24
 *
 * @author Jarman
 */
@Service
public class VisitorAvailableDesktopQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisitorAvailableDesktopQuery.class);

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private UserLoginSession userLoginSession;

    /**
     * 查询可用的云桌面
     * 
     * @param userId 用户id
     * @param terminalId 登录的终端id
     * @return ViewUserDesktopEntity 只返回1个可用桌面
     */
    public ViewUserDesktopEntity queryVisitorAvailableDesktop(UUID userId, String terminalId) {
        Assert.notNull(userId, "userId cannot null");
        Assert.hasText(terminalId, "terminalId cannot empty");

        List<ViewUserDesktopEntity> userDesktopList = viewDesktopDetailDAO.findByUserIdAndIsDelete(userId, false);
        if (CollectionUtils.isEmpty(userDesktopList)) {
            // 没有配置桌面
            return null;
        }

        // 根据名称排序，默认正序
        userDesktopList = userDesktopList.stream().sorted(Comparator.comparing(ViewUserDesktopEntity::getDesktopName)).collect(Collectors.toList());

        // 获取最近一次绑定终端且在运行中的桌面
        ViewUserDesktopEntity runningDesktop = getLatestRunningDesktop(userDesktopList, terminalId);
        if (runningDesktop != null) {
            LOGGER.info("终端[{}]获取到最近一次绑定且运行中的云桌面[{}]", terminalId, runningDesktop.getDesktopName());
            return runningDesktop;
        }
        // 获取已绑定过的桌面
        ViewUserDesktopEntity bindDesktop = getHasBindDesktop(userDesktopList, terminalId);
        if (bindDesktop != null) {
            LOGGER.info("终端[{}]获取到已绑定的云桌面[{}]", terminalId, bindDesktop.getDesktopName());
            return bindDesktop;
        }
        // 获取未绑定过终端的桌面
        ViewUserDesktopEntity unBindTerminalDesktop = getUnBindTerminalDesktop(userDesktopList);
        if (unBindTerminalDesktop != null) {
            LOGGER.info("终端[{}]获取到未绑定终端的云桌面，桌面名称={}", terminalId, unBindTerminalDesktop.getDesktopName());
            // 绑定终端桌面
            bindTerminal(unBindTerminalDesktop.getCbbDesktopId(), terminalId);
            return unBindTerminalDesktop;
        }
        // 查找空闲的桌面
        ViewUserDesktopEntity idleDesktop = getIdleDesktop(userDesktopList, terminalId);
        if (idleDesktop != null) {
            LOGGER.info("终端[{}]获取到空闲的云桌面，桌面名称={}", terminalId, idleDesktop.getDesktopName());
            // 绑定终端桌面
            bindTerminal(idleDesktop.getCbbDesktopId(), terminalId);
            return idleDesktop;
        }
        // 无空闲桌面，抢占一个没有正在使用的桌面
        ViewUserDesktopEntity grabDesktop = grabDesktop(userDesktopList, terminalId);
        if (grabDesktop != null) {
            // 绑定终端桌面
            bindTerminal(grabDesktop.getCbbDesktopId(), terminalId);
            return grabDesktop;
        }
        // 无可用桌面
        return null;
    }

    private ViewUserDesktopEntity grabDesktop(List<ViewUserDesktopEntity> userDesktopList, String terminalId) {
        for (ViewUserDesktopEntity desktop : userDesktopList) {
            if (CbbCloudDeskState.SHUTTING.name().equals(desktop.getDeskState())) {
                // 访客过滤掉状态为关机中的桌面，防止终端抢占后收到关闭桌面报文执行联动关机操作
                LOGGER.info("终端[{}]访客过滤掉状态为关机中的桌面[{}]", terminalId, desktop.getDesktopName());
                continue;
            }
            // 排除故障桌面
            if (CbbCloudDeskState.ERROR.name().equals(desktop.getDeskState())) {
                LOGGER.warn("桌面[{}]处于故障状态，不可用", desktop.getDesktopName());
                continue;
            }
            UserInfo userInfo = userLoginSession.getLoginUserInfo(desktop.getTerminalId());
            // 终端没有访客用户登录，说明此终端没有访客在使用桌面
            if (userInfo == null || userInfo.getUserType() != IacUserTypeEnum.VISITOR) {
                if (CbbCloudDeskState.RUNNING.name().equals(desktop.getDeskState())) {
                    LOGGER.info("桌面[{}]处于运行状态，不支持被抢占", desktop.getDesktopName());
                    continue;
                }
                LOGGER.info("终端[{}]抢占没有正在被使用的桌面[{}]", terminalId, desktop.getDesktopName());
                return desktop;
            }
            LOGGER.info("终端[{}]正在使用桌面[{}]", desktop.getTerminalId(), desktop.getDesktopName());
        }
        // 无可用桌面
        return null;
    }

    private ViewUserDesktopEntity getUnBindTerminalDesktop(List<ViewUserDesktopEntity> userDesktopList) {
        for (ViewUserDesktopEntity desktop : userDesktopList) {
            if (desktop.getTerminalId() == null) {
                // 排除故障桌面
                if (CbbCloudDeskState.ERROR.name().equals(desktop.getDeskState())) {
                    LOGGER.warn("桌面[{}]处于故障状态，不可用", desktop.getDesktopName());
                    continue;
                }
                return desktop;
            }
        }
        // 无可用桌面
        return null;
    }

    private ViewUserDesktopEntity getLatestRunningDesktop(List<ViewUserDesktopEntity> userDesktopList, String terminalId) {
        // 获取最近一次登录的，且在运行中的桌面
        for (ViewUserDesktopEntity desktop : userDesktopList) {
            // 之前正在用的云桌面
            if (CbbCloudDeskState.RUNNING.name().equals(desktop.getDeskState()) && terminalId.equals(desktop.getTerminalId())) {
                return desktop;
            }
        }
        // 没有找到已绑定在当前终端且运行中的桌面
        return null;
    }

    private ViewUserDesktopEntity getHasBindDesktop(List<ViewUserDesktopEntity> userDesktopList, String terminalId) {
        // 获取已绑定终端的桌面
        for (ViewUserDesktopEntity desktop : userDesktopList) {
            if (terminalId.equals(desktop.getTerminalId())) {
                // 排除故障桌面
                if (CbbCloudDeskState.ERROR.name().equals(desktop.getDeskState())) {
                    LOGGER.warn("桌面[{}]处于故障状态，不可用", desktop.getDesktopName());
                    continue;
                }
                return desktop;
            }
        }
        // 没有找到已绑定在当前终端的桌面
        return null;
    }

    private ViewUserDesktopEntity getIdleDesktop(List<ViewUserDesktopEntity> userDesktopList, String terminalId) {
        // 查找空闲状态的云桌面
        for (ViewUserDesktopEntity desktop : userDesktopList) {
            boolean isClose = CbbCloudDeskState.CLOSE.name().equals(desktop.getDeskState());
            if (isClose) {
                bindTerminal(desktop.getCbbDesktopId(), terminalId);
                return desktop;
            }
        }
        // 无空闲桌面
        return null;
    }

    private void bindTerminal(UUID desktopId, String terminalId) {
        boolean isSuccess = userDesktopDAO.updateTerminalIdByCbbDesktopId(desktopId, terminalId) > 0;
        LOGGER.info("终端[{}]绑定桌面[{}]是否成功[{}]", terminalId, desktopId, isSuccess);
    }
}
