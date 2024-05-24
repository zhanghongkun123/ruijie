package com.ruijie.rcos.rcdc.rco.module.impl.ipllimit.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 21:28
 *
 * @author yxq
 */
public interface IpLimitService {

    /**
     * 校验IP池是否有重复
     *
     * @param ipLimitDTOList IP池
     * @throws BusinessException 业务异常
     */
    void validateIpDTOList(List<IpLimitDTO> ipLimitDTOList) throws BusinessException;

    /**
     * 删除数据库中所有受限IP段，并且将新的IP段保存到数据库中
     *
     * @param ipLimitDTOList IP池
     * @param enableIpLimit 是否开启IP限制
     */
    void modifyIpPoolAndStrategy(List<IpLimitDTO> ipLimitDTOList, Boolean enableIpLimit);

    /**
     * 查询所有的IP网段
     *
     * @return 所有的IP网段
     */
    List<IpLimitDTO> getAllIpLimitDTOList();
}
