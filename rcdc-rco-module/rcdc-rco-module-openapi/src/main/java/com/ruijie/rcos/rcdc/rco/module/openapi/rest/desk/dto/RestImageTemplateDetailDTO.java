package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoImageTemplateSnapshotDTO;

import java.util.List;
import java.util.UUID;

/**
 * Description: 返回给R-Center的镜像模板信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/28 20:41
 *
 * @author yxq
 */
public class RestImageTemplateDetailDTO extends CbbImageTemplateDTO {

    private UUID unifiedManageDataId;

    private List<RcoImageTemplateSnapshotDTO> snapshotList;



    public List<RcoImageTemplateSnapshotDTO> getSnapshotList() {
        return snapshotList;
    }

    public void setSnapshotList(List<RcoImageTemplateSnapshotDTO> snapshotList) {
        this.snapshotList = snapshotList;
    }

    public UUID getUnifiedManageDataId() {
        return unifiedManageDataId;
    }

    public void setUnifiedManageDataId(UUID unifiedManageDataId) {
        this.unifiedManageDataId = unifiedManageDataId;
    }
}
