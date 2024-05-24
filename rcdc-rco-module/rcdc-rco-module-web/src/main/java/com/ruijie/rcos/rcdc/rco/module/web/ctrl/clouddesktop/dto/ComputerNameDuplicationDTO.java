package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

/**
 * Description: 镜像计算机名称重名检查结果响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/24
 *
 * @author WuShengQiang
 */
public class ComputerNameDuplicationDTO {

    private boolean hasDuplication;

    public ComputerNameDuplicationDTO(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public boolean isHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
