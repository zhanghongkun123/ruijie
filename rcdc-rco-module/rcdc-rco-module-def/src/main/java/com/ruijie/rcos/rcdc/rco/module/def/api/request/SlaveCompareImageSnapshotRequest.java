package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoImageTemplateSnapshotDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/3
 *
 * @author zhiweiHong
 */
public class SlaveCompareImageSnapshotRequest implements Serializable {


    @NotNull
    private List<RcoImageTemplateSnapshotDTO> snapshotList;

    public List<RcoImageTemplateSnapshotDTO> getSnapshotList() {
        return snapshotList;
    }

    public void setSnapshotList(List<RcoImageTemplateSnapshotDTO> snapshotList) {
        this.snapshotList = snapshotList;
    }
}
