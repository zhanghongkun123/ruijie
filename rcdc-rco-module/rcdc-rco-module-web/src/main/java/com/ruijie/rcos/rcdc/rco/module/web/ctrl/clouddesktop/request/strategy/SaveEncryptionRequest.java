package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 保存密钥请求体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/6
 *
 * @author WuShengQiang
 */
public class SaveEncryptionRequest {

    /**
     * 种子
     */
    @NotBlank
    private String seed;

    /**
     * true:重置 false:新建
     */
    @NotNull
    private Boolean restore;

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public Boolean getRestore() {
        return restore;
    }

    public void setRestore(Boolean restore) {
        this.restore = restore;
    }
}
