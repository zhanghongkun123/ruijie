package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IpLimitDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IpLimitEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.IpLimitServiceTx;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: IP网段限制事务Service实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/4 9:29
 *
 * @author yxq
 */
@Service
public class IpLimitServiceTxImpl implements IpLimitServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpLimitServiceTxImpl.class);

    @Autowired
    private IpLimitDAO ipLimitDao;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public void modifyIpLimitStrategy(List<IpLimitEntity> ipLimitEntityList, Boolean enableIpLimit) {
        Assert.notNull(ipLimitEntityList, "ipLimitEntityList must not null");
        Assert.notNull(enableIpLimit, "enableIpLimit must not null");

        LOGGER.info("开始执行修改IP限制策略事务方法");
        globalParameterService.updateParameter(Constants.RCDC_RCO_IP_LIMIT_VALUE, enableIpLimit.toString());
        ipLimitDao.deleteAll();
        LOGGER.info("删除全部IP网段成功");
        ipLimitDao.saveAll(ipLimitEntityList);
        LOGGER.info("插入新IP网段成功");
    }
}
