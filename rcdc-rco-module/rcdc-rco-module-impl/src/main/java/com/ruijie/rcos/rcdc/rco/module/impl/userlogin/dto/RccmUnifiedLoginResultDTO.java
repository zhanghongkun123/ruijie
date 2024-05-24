package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedLoginResultDTO;

/**
 *
 * Description: rcdc统一登录openApi返回结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class RccmUnifiedLoginResultDTO extends UnifiedLoginResultDTO {

    /**
     * 接口结果状态码
     */
    private int resultCode;

    /**
     * 是否需要绑定本集群用户终端关系
     */
    private Boolean needBindLoginSession;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Boolean getNeedBindLoginSession() {
        return needBindLoginSession;
    }

    public void setNeedBindLoginSession(Boolean needBindLoginSession) {
        this.needBindLoginSession = needBindLoginSession;
    }
}
