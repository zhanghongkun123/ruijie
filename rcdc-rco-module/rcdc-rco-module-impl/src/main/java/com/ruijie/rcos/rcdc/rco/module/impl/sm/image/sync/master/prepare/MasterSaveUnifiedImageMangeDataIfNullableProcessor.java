package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.prepare;

import com.alibaba.fastjson.JSON;
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

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 主端保存统一管理镜像标识
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
@Service
public class MasterSaveUnifiedImageMangeDataIfNullableProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterSaveUnifiedImageMangeDataIfNullableProcessor.class);

    @Autowired
    private MasterStageContextResolver resolver;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        MasterCrossClusterImageSyncDTO dto = resolver.resolveDTO(context);

        UUID imageId = dto.getImageId();

        UnifiedManageDataRequest queryRequest = new UnifiedManageDataRequest();
        queryRequest.setRelatedId(imageId);
        queryRequest.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
        UnifiedManageDataEntity oldEntity = unifiedManageDataService.findByRelatedIdAndRelatedType(queryRequest);

        if (Objects.nonNull(oldEntity)) {
            return StateTaskHandle.ProcessResult.next();
        }


        UnifiedManageDataRequest request = new UnifiedManageDataRequest();
        request.setRelatedId(imageId);
        request.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
        request.setUnifiedManageDataId(dto.getUnifiedManageDataId());
        LOGGER.info("保存统一管理标识[{}]", JSON.toJSONString(request));
        unifiedManageDataService.saveUnifiedManage(request);

        return StateTaskHandle.ProcessResult.next();
    }

}
