package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaPoolGroupBindSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.AdGroupPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 云应用分组分配SPI
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月16日
 *
 * @author zhengjingyong
 */
public class RcaPoolGroupBindSPIImpl implements RcaPoolGroupBindSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaPoolGroupBindSPIImpl.class);

    private static final String NAME_SPLIT = ",";

    private static final int SINGLE_LOG_MAX_NUM = 20;

    @Autowired
    private AdGroupPoolService adGroupPoolService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcoAdGroupService rcoAdGroupService;


    @Override
    public Boolean saveBindAdSafeGroupLog(String poolName, String appGroupName, List<UUID> adSafeGroupIdList, String key) throws BusinessException {
        Assert.hasText(poolName, "poolName cannot be null");
        Assert.hasText(appGroupName, "appGroupName cannot be null");
        Assert.hasText(key, "key cannot be null");
        Assert.notNull(adSafeGroupIdList, "adSafeGroupIdList cannot be null");

        if (CollectionUtils.isEmpty(adSafeGroupIdList)) {
            return Boolean.FALSE;
        }

        List<IacAdGroupEntityDTO> adGroupList = adGroupPoolService.listAdGroupByIds(adSafeGroupIdList);
        List<List<IacAdGroupEntityDTO>> tempAdGroupList = Lists.partition(adGroupList, SINGLE_LOG_MAX_NUM);
        for (List<IacAdGroupEntityDTO> subAdGroupList : tempAdGroupList) {
            List<String> nameList = subAdGroupList.stream().map(IacAdGroupEntityDTO::getName).collect(Collectors.toList());
            auditLogAPI.recordLog(key, poolName, appGroupName, StringUtils.join(nameList, NAME_SPLIT));
        }
        return Boolean.TRUE;
    }

    @Override
    public List<UUID> getAdSafeGroupIdList(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId is not null");

        return rcoAdGroupService.getUserRelatedAdGroupList(userId);
    }
}
