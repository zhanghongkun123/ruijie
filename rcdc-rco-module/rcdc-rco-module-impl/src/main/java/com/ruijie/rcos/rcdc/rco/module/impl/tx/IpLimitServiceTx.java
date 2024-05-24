package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.IpLimitEntity;

import java.util.List;

/**
 * Description: IP网段限制事务Service接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/4 9:28
 *
 * @author yxq
 */
public interface IpLimitServiceTx {

    /**
     * 修改IP限制策略信息
     *
     * @param ipLimitEntityList IP网段
     * @param enableIpLimit 是否开启IP限制
     */
    void modifyIpLimitStrategy(List<IpLimitEntity> ipLimitEntityList, Boolean enableIpLimit);
}
