package com.ruijie.rcos.rcdc.rco.module.impl.globalparameter;


/**
 * Description: 全局配置被修改的后的事件策略
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-29
 *
 * @param <T>
 * @author chenl
 */
public interface GlobalParameterModifiedEventStrategy<T> {

    /**
     *
     * @param value 通知参数
     */
    void notify(T value);

    /**
     * 返回策略名
     * @param key 全局参数key
     * @return 策略名
     */
    Boolean needNotify(String key);
}
