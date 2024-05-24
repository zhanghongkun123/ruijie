package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import java.util.List;
import java.util.UUID;

/**
 * Description: 池分配日志DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/28
 *
 * @author TD
 */
public class DesktopPoolBindGroupDTO {

    private List<UUID> addGroupIdList;

    private List<UUID> deleteGroupIdList;

    private List<UUID> addAdGroupIdList;

    private List<UUID> deleteAdGroupIdList;

    public List<UUID> getAddGroupIdList() {
        return addGroupIdList;
    }

    public void setAddGroupIdList(List<UUID> addGroupIdList) {
        this.addGroupIdList = addGroupIdList;
    }

    public List<UUID> getDeleteGroupIdList() {
        return deleteGroupIdList;
    }

    public void setDeleteGroupIdList(List<UUID> deleteGroupIdList) {
        this.deleteGroupIdList = deleteGroupIdList;
    }

    public List<UUID> getAddAdGroupIdList() {
        return addAdGroupIdList;
    }

    public void setAddAdGroupIdList(List<UUID> addAdGroupIdList) {
        this.addAdGroupIdList = addAdGroupIdList;
    }

    public List<UUID> getDeleteAdGroupIdList() {
        return deleteAdGroupIdList;
    }

    public void setDeleteAdGroupIdList(List<UUID> deleteAdGroupIdList) {
        this.deleteAdGroupIdList = deleteAdGroupIdList;
    }
}
