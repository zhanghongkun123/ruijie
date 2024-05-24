package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageSnapshotRestoreTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.dto.RcoImageTemplateDetailDTO;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class ImageTemplateDetailVO {

    private CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO;

    private RcoImageTemplateDetailDTO rcoImageTemplateDetailDTO;

    private ImageSnapshotRestoreTaskDTO restoreTaskDTO;

    private List<UUID> imageVersionIdList;

    public CbbImageTemplateDetailDTO getCbbImageTemplateDetailDTO() {
        return cbbImageTemplateDetailDTO;
    }

    public void setCbbImageTemplateDetailDTO(CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO) {
        this.cbbImageTemplateDetailDTO = cbbImageTemplateDetailDTO;
    }

    public RcoImageTemplateDetailDTO getRcoImageTemplateDetailDTO() {
        return rcoImageTemplateDetailDTO;
    }

    public void setRcoImageTemplateDetailDTO(RcoImageTemplateDetailDTO rcoImageTemplateDetailDTO) {
        this.rcoImageTemplateDetailDTO = rcoImageTemplateDetailDTO;
    }

    public ImageSnapshotRestoreTaskDTO getRestoreTaskDTO() {
        return restoreTaskDTO;
    }

    public void setRestoreTaskDTO(ImageSnapshotRestoreTaskDTO restoreTaskDTO) {
        this.restoreTaskDTO = restoreTaskDTO;
    }

    public List<UUID> getImageVersionIdList() {
        return imageVersionIdList;
    }

    public void setImageVersionIdList(List<UUID> imageVersionIdList) {
        this.imageVersionIdList = imageVersionIdList;
    }
}
