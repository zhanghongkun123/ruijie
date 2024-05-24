package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoImageTemplateSnapshotDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 从端准备镜像同步接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
public class SlaveRollbackImageSyncResponse implements Serializable {

    @NotNull
    private List<RcoImageTemplateSnapshotDTO> snapshotList;

    public SlaveRollbackImageSyncResponse(List<RcoImageTemplateSnapshotDTO> snapshotList) {
        this.snapshotList = snapshotList;
    }

    public List<RcoImageTemplateSnapshotDTO> getSnapshotList() {
        return snapshotList;
    }

    public void setSnapshotList(List<RcoImageTemplateSnapshotDTO> snapshotList) {
        this.snapshotList = snapshotList;
    }
}
