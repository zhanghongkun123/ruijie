package com.ruijie.rcos.rcdc.rco.module.impl.service;

/**
 * Description: 桌面使用数量记录
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/28 19:37
 *
 * @author linrenjian
 */
public interface DesksoftUseRecordService {

    /**
     * 删除记录根据时间
     * 
     * @param updateTime updateTime
     * @return
     */
    void deleteListByUpdateTime(Long updateTime);
}
