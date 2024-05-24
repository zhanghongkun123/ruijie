package com.ruijie.rcos.rcdc.rco.module.impl.upgrade.request;

import com.ruijie.rcos.rcdc.rco.module.impl.upgrade.dto.CancelPromptVersionDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/15 16:46
 *
 * @author ketb
 */
public class CancelPromptVersionRequest {

    @NotNull
    private CancelPromptVersionDTO content;

    public CancelPromptVersionRequest(CancelPromptVersionDTO content) {
        this.content = content;
    }

    public CancelPromptVersionDTO getContent() {
        return content;
    }

    public void setContent(CancelPromptVersionDTO content) {
        this.content = content;
    }
}
