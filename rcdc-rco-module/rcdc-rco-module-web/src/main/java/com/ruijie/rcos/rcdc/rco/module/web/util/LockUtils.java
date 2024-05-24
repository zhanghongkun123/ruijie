package com.ruijie.rcos.rcdc.rco.module.web.util;

import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年07月31日
 *
 * @author xgx
 */
public class LockUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LockUtils.class);

    private static final CopyOnWriteArraySet<String> COPY_ON_WRITE_ARRAY_SET = new CopyOnWriteArraySet();


    /**
     * 尝试执行
     * 
     * @param resourceId 资源ID
     * @param lockFunction lockFunction
     * @param <R> 返回值
     * @return R
     * @throws BusinessException 业务异常
     */
    public static <R> R tryExecute(String resourceId, LockFunction<R> lockFunction) throws BusinessException {
        Assert.notNull(resourceId, "resourceId can not be null");
        Assert.notNull(lockFunction, "lockFunction can not be null");

        tryLock(resourceId);
        try {
            return lockFunction.execute();
        } finally {
            unLock(resourceId);
        }
    }

    /**
     * 尝试加锁
     * 
     * @param resourceId 资源ID
     * @throws BusinessException 业务异常
     */
    public static void tryLock(String resourceId) throws BusinessException {
        Assert.notNull(resourceId, "resourceId can not be null");
        if (!COPY_ON_WRITE_ARRAY_SET.add(resourceId)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_TRY_LOCK_FAIL, resourceId);
        }
    }

    /**
     * 释放锁
     * @param resourceId 资源ID
     */
    public static void unLock(String resourceId) {
        Assert.notNull(resourceId, "resourceId can not be null");
        COPY_ON_WRITE_ARRAY_SET.remove(resourceId);
    }

    /**
     * LockFunction
     * 
     * @param <R> 返回值类型
     */
    @FunctionalInterface
    public interface LockFunction<R> {
        /**
         * 执行
         *
         * @return R
         * @throws BusinessException 业务异常
         */
        R execute() throws BusinessException;
    }

}

