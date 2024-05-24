package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description: rcdc => shine 终端初始化请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/9
 *
 * @author WuShengQiang
 */
public class TerminalInitRequest {

    private Boolean retainImage;

    public TerminalInitRequest(Boolean retainImage) {
        this.retainImage = retainImage;
    }

    public Boolean getRetainImage() {
        return retainImage;
    }

    public void setRetainImage(Boolean retainImage) {
        this.retainImage = retainImage;
    }
}
