package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.rco.module.impl.service.DesksoftUseRecordService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;

/**
 * Description:桌面软件使用记录定时清理任务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/28 17:06
 *
 * @author linrenjian
 */
@Service
public class DesksoftUseRecordQuartzTask implements SafetySingletonInitializer, Runnable {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DesksoftUseRecordQuartzTask.class);

    @Autowired
    private DesksoftUseRecordService desksoftUseRecordService;

    @Override
    public void safeInit() {
        // 每天 晚上2点 进行数据清理 清理距离超过7的数据 0 0 2 * * ?
        String cronExpression = "0 0 2 * * ?";
        try {
            ThreadExecutors.scheduleWithCron(this.getClass().getSimpleName(), this, cronExpression);
        } catch (ParseException e) {
            throw new RuntimeException("定时任务[" + this.getClass() + "]cron表达式[" + cronExpression + "]解析异常", e);
        }
    }

    @Override
    public void run() {
        cleanExpiredSession();
    }


    private void cleanExpiredSession() {
        // 获取当前时间
        Long updateTime = LocalDate.now().toEpochDay();
        // 清理距离超过7天的数据
        updateTime = updateTime - 7L;
        desksoftUseRecordService.deleteListByUpdateTime(updateTime);
    }

}
