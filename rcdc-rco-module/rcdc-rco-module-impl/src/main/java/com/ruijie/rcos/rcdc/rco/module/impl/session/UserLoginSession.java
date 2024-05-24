package com.ruijie.rcos.rcdc.rco.module.impl.session;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientOpLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.enums.ClientOperateLogType;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 终端用户登录session
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/23
 *
 * @author Jarman
 */
@Service
public class UserLoginSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginSession.class);

    /**
     * 登录用户session key-> userName,value-> UserInfo
     */
    private static final Multimap<String, UserInfo> LOGIN_USER_SESSION = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());

    /**
     * 登录用户缓存 key-> terminalId,value-> UserInfo
     */
    private static final Map<String, UserInfo> LOGIN_TERMINAL_USER_CACHE = new ConcurrentHashMap<>();
    
    private static final int MAX_FETCH_LOCK_TIME_IN_SECOND = 30;
    
    private static final String LOCK_FLAG = "USER_SESSION";

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private ClientOpLogAPI clientOpLogAPI;

    /**
     * 添加登录用户
     * 
     * @param userInfo 用户信息
     */
    public void addLoginUser(UserInfo userInfo) {
        Assert.notNull(userInfo, "userInfo can not null");
        String userName = userInfo.getUserName();
        try {
            LockableExecutor.executeWithTryLock(LOCK_FLAG + userName, () -> {
                // 判断用户在指定终端是否已登录
                if (hasLogin(userName, userInfo.getTerminalId())) {
                    LOGGER.warn("用户登录重入请求，用户名[{}]，用户信息[{}]", userName, JSONObject.toJSONString(userInfo));
                    return;
                }
                LOGIN_USER_SESSION.put(userName, userInfo);
                LOGIN_TERMINAL_USER_CACHE.put(userInfo.getTerminalId(), userInfo);
                LOGGER.info("当前登录用户数量为：[{}]", LOGIN_USER_SESSION.size());
            }, MAX_FETCH_LOCK_TIME_IN_SECOND);
        } catch (BusinessException e) {
            LOGGER.error("lock user error:{}", userName, e);
        }
    }

    /**
     * 根据终端id移除对应的登录用户
     * 
     * @param terminalId 终端id
     */
    public void removeLoginUser(String terminalId) {
        Assert.hasText(terminalId, "terminal can not empty");
        if (LOGIN_USER_SESSION.size() == 0) {
            LOGGER.info("当前没有登录用户");
            return;
        }
        UserInfo userInfo = LOGIN_TERMINAL_USER_CACHE.get(terminalId);
        if (userInfo != null) {
            try {
                iacUserMgmtAPI.logout(userInfo.getUserId());
                clientOpLogAPI.saveUserOperateLog(terminalId, userInfo.getUserId(), ClientOperateLogType.ONE_CLIENT_LOG_LOGOUT_SUCCESS);
            } catch (Exception e) {
                // 失败不能影响业务，只影响更新用户最后登出时间
                LOGGER.error("执行gss用户[{}]登出失败", userInfo.getUserName(), e);
            }
            boolean isSuccess = LOGIN_USER_SESSION.remove(userInfo.getUserName(), userInfo);
            LOGGER.info("移除终端[{}]用户[{}]session：{}", terminalId, userInfo.getUserName(), isSuccess);
        }
        LOGIN_TERMINAL_USER_CACHE.remove(terminalId);

    }

    /**
     * 判断用户是否登录
     * 
     * @param userName 用户名
     * @return true 已登录，false未登录
     */
    public boolean hasLogin(String userName) {
        Assert.hasText(userName, "userName can not empty");
        List<UserInfo> userInfoList = (List<UserInfo>) LOGIN_USER_SESSION.get(userName);
        LOGGER.info("用户[{}]已在{}台终端登录", userName, userInfoList.size());
        userInfoList.forEach(item -> LOGGER.info("用户[{}]已在终端[{}]登录，登录时间为：{}", userName, item.getTerminalId(), item.getLoginTime()));

        return userInfoList.size() > 0;

    }

    /**
     * 判断用户是否在指定终端登录
     * 
     * @param userName 用户名
     * @param terminalId 终端id
     * @return true 已登录
     */
    public boolean hasLogin(String userName, String terminalId) {
        Assert.hasText(userName, "userName can not empty");
        Assert.hasText(terminalId, "terminalId can not empty");
        List<UserInfo> userInfoList = (List<UserInfo>) LOGIN_USER_SESSION.get(userName);
        LOGGER.info("用户[{}]已在{}台终端登录", userName, userInfoList.size());
        for (UserInfo userInfo : userInfoList) {
            if (terminalId.equals(userInfo.getTerminalId())) {
                LOGGER.info("用户[{}]在终端[{}]已登录", userInfo.getUserName(), userInfo.getTerminalId());
                return true;
            }
        }
        return false;
    }

    /**
     * 根据终端id获取登录的用户信息
     *
     * @param terminalId 终端id
     * @return 返回用户名，没有登录用户返回null
     */
    public UserInfo getLoginUserInfo(String terminalId) {
        Assert.hasText(terminalId, "terminalId can not empty");
        UserInfo userInfo = LOGIN_TERMINAL_USER_CACHE.get(terminalId);
        if (userInfo == null) {
            LOGGER.info("终端[{}]当前没有登录用户", terminalId);
            // 终端没有登录用户
            return null;
        }
        return userInfo;
    }
}
