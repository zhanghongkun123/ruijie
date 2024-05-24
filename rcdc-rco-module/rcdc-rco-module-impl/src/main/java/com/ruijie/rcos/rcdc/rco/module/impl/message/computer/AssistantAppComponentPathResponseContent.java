package com.ruijie.rcos.rcdc.rco.module.impl.message.computer;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/9 14:33
 *
 * @author ketb
 */
public class AssistantAppComponentPathResponseContent extends BaseResponseContent {

    private String path;

    public AssistantAppComponentPathResponseContent(String business) {
        super(business);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
