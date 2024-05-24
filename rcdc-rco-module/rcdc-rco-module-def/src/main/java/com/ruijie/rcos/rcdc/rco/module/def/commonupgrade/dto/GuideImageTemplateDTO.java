package com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/25 15:54
 *
 * @author chenl
 */
public class GuideImageTemplateDTO {

    private UUID imageTemplateId;

    private String imageTemplateName;

    private List<GuideDesktopPoolDTO> guideDesktopPoolList;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public List<GuideDesktopPoolDTO> getGuideDesktopPoolList() {
        return guideDesktopPoolList;
    }

    public void setGuideDesktopPoolList(List<GuideDesktopPoolDTO> guideDesktopPoolList) {
        this.guideDesktopPoolList = guideDesktopPoolList;
    }

    @Override
    public String toString() {
        return "GuideImageTemplateDTO{" +
                "imageTemplateId=" + imageTemplateId +
                ", imageTemplateName='" + imageTemplateName + '\'' +
                '}';
    }
}
