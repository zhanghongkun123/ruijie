package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年06月30日
 *
 * @author xgx
 */
public enum EffectiveStrategy {
    RIGHT_NOW(true),

    NEXT(false);

    private Boolean enableForceApply;

    EffectiveStrategy(Boolean enableForceApply) {
        this.enableForceApply = enableForceApply;
    }

    public Boolean getEnableForceApply() {
        return enableForceApply;
    }
}
