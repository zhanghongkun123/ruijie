package com.ruijie.rcos.rcdc.rco.module.impl.handler;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogConfigMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.enums.BaseLogType;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 
 * Description: 设置3A审计日志周期
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author zhiweiHong
 */
@Service
public class SetBaseAuditLogConfigProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetBaseAuditLogConfigProcessor.class);

    @Autowired
    private BaseAuditLogConfigMgmtAPI baseAuditLogConfigMgmtAPI;

    private static final int SECOND_OF_DAY = 86400;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context is not null");
        Integer intervalDay = context.get(Constants.INTERVAL_DAY_KEY, Integer.class);
        Integer second = intervalDay * SECOND_OF_DAY;
        BaseLogType[] baseLogTypeArr = BaseLogType.values();
        for (BaseLogType logType : baseLogTypeArr) {
            LOGGER.info("调用BaseAuditLogConfigMgmtAPI进行日志周期更新，更新类型为{}，更新时间为{}s", logType, second);
            baseAuditLogConfigMgmtAPI.updateLogMaxRetentionTime(second, logType);
        }
        return StateTaskHandle.ProcessResult.next();
    }

    @Override
    public StateTaskHandle.ProcessResult undoProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context is not null");
        Integer oldValue = context.get(Constants.OLD_DAY_KEY, Integer.class);
        Integer second = oldValue * SECOND_OF_DAY;
        BaseLogType[] baseLogTypeArr = BaseLogType.values();
        for (BaseLogType logType : baseLogTypeArr) {
            LOGGER.info("调用BaseAuditLogConfigMgmtAPI进行日志周期回滚，更新类型为{}，更新时间为{}s", logType, second);
            baseAuditLogConfigMgmtAPI.updateLogMaxRetentionTime(second, logType);
        }
        return StateTaskHandle.ProcessResult.next();
    }
}
