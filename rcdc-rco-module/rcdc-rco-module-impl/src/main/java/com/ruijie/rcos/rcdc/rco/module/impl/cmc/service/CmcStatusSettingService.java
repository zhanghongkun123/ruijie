package com.ruijie.rcos.rcdc.rco.module.impl.cmc.service;

import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.contant.CmcConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:  GuestTool 上报软件信息开关配置
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
@Service
@Scope("prototype")
public class CmcStatusSettingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmcStatusSettingService.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    // 记录当前时间戳
    private static final Lock CHECK_IS_OPEN_LOCK = new ReentrantLock();

    // 上一次更新时间
    private static long lastly = 0;

    // 是否开启，默认开启
    private static boolean isOpen = true;

    /**
     * 初始化配置
     */
    @PostConstruct
    public void initMethod() {
        isOpen = obtain();
        lastly = Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 查询是否开启 CMC 配置
     *
     * @return 开关
     */
    public boolean isOpen() {

        Calendar currDate = Calendar.getInstance();
        long now = currDate.getTimeInMillis();
        // 配置十分钟缓存过期时间`
        if ((now - lastly) <= CmcConstants.UPDATE_SETTING_CACHE) {
            return isOpen;
        }

        if (!CHECK_IS_OPEN_LOCK.tryLock()) {
            return isOpen;
        }

        try {
            isOpen = obtain();
            lastly = now;
            LOGGER.info("更新 CMC 开关配置状态：{}", isOpen);
        } catch (Exception ex) {
            LOGGER.error("查询数据库异常", ex);
        } finally {
            CHECK_IS_OPEN_LOCK.unlock();
        }

        return isOpen;
    }

    private boolean obtain() {
        return Boolean.parseBoolean(globalParameterService.findParameter(Constants.RCDC_GT_DESKSOFT_MSG_STATUS));
    }
}
