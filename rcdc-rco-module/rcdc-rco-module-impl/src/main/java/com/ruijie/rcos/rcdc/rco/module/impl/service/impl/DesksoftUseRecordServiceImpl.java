package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesksoftUseRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesksoftUseRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesksoftUseRecordService;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/28 20:11
 *
 * @author linrenjian
 */
@Service
public class DesksoftUseRecordServiceImpl implements DesksoftUseRecordService {


    @Autowired
    private DesksoftUseRecordDAO desksoftUseRecordDAO;

    @Override
    public void deleteListByUpdateTime(Long updateTime) {
        Assert.notNull(updateTime, "updateTime cannot be null");
        // 查询比传入时间小的数据
        List<DesksoftUseRecordEntity> desksoftUseRecordList = desksoftUseRecordDAO.findByUpdateTimeLessThanEqual(updateTime);
        // 调用平台批量删除
        desksoftUseRecordDAO.deleteAll(desksoftUseRecordList);
    }
}
