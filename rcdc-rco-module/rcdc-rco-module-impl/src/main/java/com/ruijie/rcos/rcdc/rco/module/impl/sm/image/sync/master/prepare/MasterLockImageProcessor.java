package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.master.prepare;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.MasterCrossClusterImageSyncDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author zhiweihong
 */
@Service
public class MasterLockImageProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MasterLockImageProcessor.class);

    @Autowired
    private MasterStageContextResolver resolver;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Override
    public StateTaskHandle.ProcessResult doProcess(StateTaskHandle.StateProcessContext context) throws Exception {
        Assert.notNull(context, "context can not be null");
        MasterCrossClusterImageSyncDTO dto = resolver.resolveDTO(context);

        UUID imageId = dto.getImageId();
        CbbImageTemplateDetailDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateDetail(imageId);

        if (imageTemplateDetail.getImageState() == ImageTemplateState.SYNCING) {
            return StateTaskHandle.ProcessResult.next();
        }

        LOGGER.info("更新镜像[{}]状态为[{}]", imageId, ImageTemplateState.SYNCING);
        imageTemplateMgmtAPI.updatePointedState(imageId, ImageTemplateState.SYNCING);
        return StateTaskHandle.ProcessResult.next();
    }

}
