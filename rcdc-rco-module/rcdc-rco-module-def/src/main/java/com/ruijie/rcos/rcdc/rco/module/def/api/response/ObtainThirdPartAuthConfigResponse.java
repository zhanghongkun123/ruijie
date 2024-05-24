package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacThirdPartyAuthConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;

/**
 * Description: 获取第三方认证配置信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-09 10:34
 *
 * @author wanglianyun
 */
public class ObtainThirdPartAuthConfigResponse extends Result {

    private IacThirdPartyAuthConfigDTO thirdPartyAuthConfigDTO;

    public IacThirdPartyAuthConfigDTO getThirdPartyAuthConfigDTO() {
        return thirdPartyAuthConfigDTO;
    }

    public void setThirdPartyAuthConfigDTO(IacThirdPartyAuthConfigDTO thirdPartyAuthConfigDTO) {
        this.thirdPartyAuthConfigDTO = thirdPartyAuthConfigDTO;
    }
}
