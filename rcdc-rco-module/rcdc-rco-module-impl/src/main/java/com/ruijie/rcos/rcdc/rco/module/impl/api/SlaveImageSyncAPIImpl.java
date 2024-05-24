package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.SlaveImageSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.PublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlavePrepareImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveRollbackImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.SlaveImageSyncService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:  从端镜像同步
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public class SlaveImageSyncAPIImpl implements SlaveImageSyncAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlaveImageSyncAPIImpl.class);

    @Autowired
    private SlaveImageSyncService slaveImageSyncService;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Override
    public void prepare(SlavePrepareImageSyncRequest request) throws BusinessException {

        Assert.notNull(request, "request can not be null");

        SlavePrepareImageSyncDTO dto = new SlavePrepareImageSyncDTO();
        BeanUtils.copyProperties(request, dto);
        dto.setImageId(UUID.randomUUID());
        slaveImageSyncService.prepareStage(dto);
    }

    @Override
    public void publish(PublishSyncingImageTemplateDTO request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        slaveImageSyncService.publishStage(request);
    }

    @Override
    public void rollback(SlaveRollbackImageSyncRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        slaveImageSyncService.rollbackStage(request);
    }

    @Override
    public List<RcoImageTemplateSnapshotDTO> getDiffSnapshotListCompareWithMaster(List<RcoImageTemplateSnapshotDTO> masterSnapshotList) {
        Assert.notNull(masterSnapshotList, "masterSnapshotList can not be null");

        if (CollectionUtils.isEmpty(masterSnapshotList)) {
            return Lists.newArrayList();
        }

        UUID remoteUnifiedId = masterSnapshotList.stream().max(Comparator.comparing(RcoImageTemplateSnapshotDTO::getCreateTime))
                .get().getUnifiedManageDataId();
        UUID remoteLatestSnapshotId = masterSnapshotList.stream().max(Comparator.comparing(RcoImageTemplateSnapshotDTO::getCreateTime))
                .get().getId();

        List<UnifiedManageDataEntity> unifiedManageDataEntityList = unifiedManageDataService.findByUnifiedManageDataIdIn
                (Arrays.asList(remoteUnifiedId));
        List<RcoImageTemplateSnapshotDTO> latestSnapshotList = masterSnapshotList.stream().filter(i -> i.getId().equals(remoteLatestSnapshotId))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(unifiedManageDataEntityList)) {
            // 本地没有快照的统一管理id，说明之前没有同步过。
            return latestSnapshotList;
        }
        // 本地有快照的统一管理id，无需再次同步
        return Lists.newArrayList();
    }
}
