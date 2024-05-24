package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/4
 *
 * @author hs
 */
public class ImageNameDuplicationDTO {

    private boolean hasDuplication;

    public ImageNameDuplicationDTO(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public boolean isHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
