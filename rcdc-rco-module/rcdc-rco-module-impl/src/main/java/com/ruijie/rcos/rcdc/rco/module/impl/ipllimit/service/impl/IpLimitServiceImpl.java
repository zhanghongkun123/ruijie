package com.ruijie.rcos.rcdc.rco.module.impl.ipllimit.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IpLimitDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IpLimitEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.ipllimit.service.IpLimitService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.IpLimitServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.IPv4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27 21:29
 *
 * @author yxq
 */
@Service
public class IpLimitServiceImpl implements IpLimitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpLimitServiceImpl.class);

    @Autowired
    private IpLimitDAO ipLimitDao;

    @Autowired
    private IpLimitServiceTx ipLimitServiceTx;

    @Override
    public void validateIpDTOList(List<IpLimitDTO> ipLimitDTOList) throws BusinessException {
        Assert.notNull(ipLimitDTOList, "ipLimitDTOList must not null");

        LOGGER.info("需要校验的IP池为：[{}]", JSON.toJSONString(ipLimitDTOList));
        if (CollectionUtils.isEmpty(ipLimitDTOList)) {
            return;
        }

        // 校验所有IP段是否合法
        validateIpPairs(ipLimitDTOList);
        // 校验所有IP段之间是否有覆盖情况，只有一个IP段的时候，不需要校验
        if (ipLimitDTOList.size() > 1) {
            validateIpPool(ipLimitDTOList);
        }
    }

    /**
     * 校验所有IP段是否合法
     *
     * @param ipLimitDTOList IP池
     * @throws BusinessException 业务异常
     */
    private void validateIpPairs(List<IpLimitDTO> ipLimitDTOList) throws BusinessException {
        for (IpLimitDTO ipLimitDTO : ipLimitDTOList) {
            boolean isIllegal = IPv4Util.compareIp(ipLimitDTO.getIpStart(), ipLimitDTO.getIpEnd()) > 0;
            if (isIllegal) {
                LOGGER.info("起始IP{}大于终止IP{}", ipLimitDTO.getIpStart(), ipLimitDTO.getIpEnd());
                throw new BusinessException(BusinessKey.RCDC_RCO_START_IP_GREATER_THAN_END_IP, ipLimitDTO.getIpStart(), ipLimitDTO.getIpEnd());
            }
        }
    }

    /**
     * 检验IP池是否存在覆盖情况
     *
     * @param ipLimitDTOList IP池
     * @throws BusinessException 业务异常
     */
    private void validateIpPool(List<IpLimitDTO> ipLimitDTOList) throws BusinessException {
        LOGGER.info("开始校验IP段之间是否存在覆盖情况");
        // IP段的数量
        int ipPairSize = ipLimitDTOList.size();
        // 逐个比较是否有覆盖情况
        for (int i = 0; i < ipPairSize; i++) {
            String ipStartOuter = ipLimitDTOList.get(i).getIpStart();
            String ipEndOuter = ipLimitDTOList.get(i).getIpEnd();
            for (int j = i + 1; j < ipPairSize; j++) {
                String ipStartInside = ipLimitDTOList.get(j).getIpStart();
                String ipEndInside = ipLimitDTOList.get(j).getIpEnd();
                if (IPv4Util.isIpSectionHasOverlapping(ipStartOuter, ipEndOuter, ipStartInside, ipEndInside)) {
                    LOGGER.error("IP段[{}-{}]与IP段[{}-{}]存在覆盖情况", ipStartOuter, ipEndOuter, ipStartInside, ipEndInside);
                    throw new BusinessException(BusinessKey.RCDC_RCO_IP_POOL_COVERAGE, ipStartOuter, ipEndOuter, ipStartInside, ipEndInside);
                }
            }
        }
        LOGGER.info("IP段之间不存在覆盖情况");
    }

    @Override
    public void modifyIpPoolAndStrategy(List<IpLimitDTO> ipLimitDTOList, Boolean enableIpLimit) {
        Assert.notNull(ipLimitDTOList, "ipLimitDTOList must not null");
        Assert.notNull(enableIpLimit, "enableIpLimit must not null");

        LOGGER.info("构造IP网段实体类");
        Date createTime = new Date();
        List<IpLimitEntity> ipLimitEntityList = ipLimitDTOList.stream().map((dto) -> {
            IpLimitEntity entity = new IpLimitEntity();
            entity.setIpStart(dto.getIpStart());
            entity.setIpEnd(dto.getIpEnd());
            entity.setCreateTime(createTime);
            entity.setUpdateTime(createTime);
            return entity;
        }).collect(Collectors.toList());

        // 事务
        ipLimitServiceTx.modifyIpLimitStrategy(ipLimitEntityList, enableIpLimit);
    }

    @Override
    public List<IpLimitDTO> getAllIpLimitDTOList() {
        return ipLimitDao.findAll().stream().map((entity) -> {
            IpLimitDTO dto = new IpLimitDTO();
            dto.setId(entity.getId());
            dto.setIpStart(entity.getIpStart());
            dto.setIpEnd(entity.getIpEnd());
            return dto;
        }).collect(Collectors.toList());
    }
}
