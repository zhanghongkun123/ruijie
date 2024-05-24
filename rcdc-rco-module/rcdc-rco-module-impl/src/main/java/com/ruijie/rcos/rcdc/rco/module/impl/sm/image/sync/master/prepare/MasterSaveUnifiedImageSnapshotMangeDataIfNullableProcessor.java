package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.prepare;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.ImageRestorePointDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.MasterCrossClusterImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 主端保存统一管理镜像标识
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
@Service
public class MasterSaveUnifiedImageSnapshotMangeDataIfNullableProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterSaveUnifiedImageSnapshotMangeDataIfNullableProcessor.class);

    @Autowired
    private MasterStageContextResolver resolver;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        MasterCrossClusterImageSyncDTO dto = resolver.resolveDTO(context);

        UUID imageId = dto.getImageId();

        List<UUID> imageSnapshotList = imageTemplateMgmtAPI.listImageSnapshot(imageId)
                .stream().map(ImageRestorePointDTO::getId).collect(Collectors.toList());

        List<UUID> snapshotUnifiedManageDataList = unifiedManageDataService.findByRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_SNAPSHOT)
                .stream().map(UnifiedManageDataEntity::getRelatedId).collect(Collectors.toList());

        List<UUID> needSaveSnapshotIdList = imageSnapshotList.stream().filter(id -> !snapshotUnifiedManageDataList.contains(id))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(needSaveSnapshotIdList)) {
            return StateTaskHandle.ProcessResult.next();
        }


        for (UUID imageSnapshotId : needSaveSnapshotIdList) {
            UnifiedManageDataRequest request = new UnifiedManageDataRequest();
            request.setRelatedId(imageSnapshotId);
            request.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_SNAPSHOT);
            request.setUnifiedManageDataId(UUID.randomUUID());
            LOGGER.info("保存统一管理标识[{}]", JSON.toJSONString(request));
            unifiedManageDataService.saveUnifiedManage(request);
        }

        return StateTaskHandle.ProcessResult.next();
    }

}
