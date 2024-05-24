package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyHttpConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyResultParserConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserSyncConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyAuthOriginEnum;

/**
 * 第三方认证配置信息
 *
 * Description: 第三方认证配置信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/13 17:25
 *
 * @author zjy
 */
public class ThirdPartyConfigResponse {

    /**
     * 第三方认证开关
     */
    private Boolean thirdPartyEnable;

    /**
     * 认证源
     */
    private ThirdPartyAuthOriginEnum authOrigin;

    /**
     * 备注
     */
    private String note;

    /**
     * http相关配置
     */
    private BaseThirdPartyHttpConfigDTO httpConfig;

    /**
     * 结果解析相关配置
     */
    private BaseThirdPartyResultParserConfigDTO resultParserConfig;

    /**
     * 用户同步相关配置
     */
    private BaseThirdPartyUserSyncConfigDTO userSyncConfig;


    public Boolean getThirdPartyEnable() {
        return thirdPartyEnable;
    }

    public void setThirdPartyEnable(Boolean thirdPartyEnable) {
        this.thirdPartyEnable = thirdPartyEnable;
    }

    public ThirdPartyAuthOriginEnum getAuthOrigin() {
        return authOrigin;
    }

    public void setAuthOrigin(ThirdPartyAuthOriginEnum authOrigin) {
        this.authOrigin = authOrigin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BaseThirdPartyHttpConfigDTO getHttpConfig() {
        return httpConfig;
    }

    public void setHttpConfig(BaseThirdPartyHttpConfigDTO httpConfig) {
        this.httpConfig = httpConfig;
    }

    public BaseThirdPartyResultParserConfigDTO getResultParserConfig() {
        return resultParserConfig;
    }

    public void setResultParserConfig(BaseThirdPartyResultParserConfigDTO resultParserConfig) {
        this.resultParserConfig = resultParserConfig;
    }

    public BaseThirdPartyUserSyncConfigDTO getUserSyncConfig() {
        return userSyncConfig;
    }

    public void setUserSyncConfig(BaseThirdPartyUserSyncConfigDTO userSyncConfig) {
        this.userSyncConfig = userSyncConfig;
    }
}
