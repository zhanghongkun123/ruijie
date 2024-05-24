package com.ruijie.rcos.rcdc.rco.module.web.interceptor;

import java.lang.reflect.Field;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.dto.IacLoginUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.session.SessionEventSPI;
import com.ruijie.rcos.sk.webmvc.core.session.SessionContextHolder;
import com.ruijie.rcos.sk.webmvc.core.session.data.LocalSessionData;
import com.ruijie.rcos.sk.webmvc.session.context.HttpServletSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.interceptor.OrderdHandlerInterceptor;

/**
 * Description: 兼容身份中心，处理session登录信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月13日
 *
 * @author lihengjing
 */
@Service
public class SessionContextOrderdHandlerInterceptor implements OrderdHandlerInterceptor {

    private final SessionContextHolder sessionContextHolder = SessionContextHolder.getInstance();

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionContextOrderdHandlerInterceptor.class);

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(response, "response can not be null");
        Assert.notNull(handler, "handler can not be null");

        SessionContext sessionContext = this.sessionContextHolder.loadSessionContext();
        IacLoginUserDTO loginUserInfo = iacAdminMgmtAPI.getLoginUserInfo();
        if (sessionContext.isLogin() && Objects.nonNull(loginUserInfo)
                && Objects.equals(loginUserInfo.getUserName(), sessionContext.getUserName())) {
            return true;
        }

        if (Objects.nonNull(loginUserInfo)) {
            setSessionInfo(sessionContext, loginUserInfo);
        }

        return true;
    }

    private void setSessionInfo(SessionContext sessionContext, IacLoginUserDTO loginUserInfo) {

        if (sessionContext instanceof HttpServletSessionContext) {

            // 获取对象的Class对象
            Class<?> clazz = sessionContext.getClass();

            try {
                Field field = clazz.getDeclaredField("sessionData");
                field.setAccessible(true);
                // 获取字段的值
                try {
                    LocalSessionData sessionData = (LocalSessionData) field.get(sessionContext);
                    sessionData.login(loginUserInfo.getId(), loginUserInfo.getUserName(),
                            new CustomerSessionEventOptLogRecord(loginUserInfo.getUserName()));
                } catch (IllegalAccessException e) {
                    // 正常不会走到这里
                    LOGGER.error("获取sessionData数据异常", e);
                }
            } catch (NoSuchFieldException e) {
                // 正常不会走到这里
                LOGGER.error("获取sessionData数据异常", e);
            }
        }



    }

    @Override
    public int getOrder() {
        return OrderdHandlerInterceptor.AFTER_FRAMEWORK + 1;
    }

    /**
     * SessionEventSPI实现类
     * Description: SessionEventSPI实现类
     * Copyright: Copyright (c) 2018
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年3月19日
     *
     * @author lin
     */
    private class CustomerSessionEventOptLogRecord implements SessionEventSPI {

        private String userName;

        CustomerSessionEventOptLogRecord(String userName) {
            this.userName = userName;
        }

        @Override
        public void onLogout() {
            //身份中心记录
            LOGGER.info("管理员[{}]退出登录,记录日志", userName);
        }

        @Override
        public void onKickout() {
            // 身份中心记录
            LOGGER.info("管理员[{}]被踢出,记录日志", userName);
        }
    }

}
