package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy;

import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementConfigRequestDTO;

import java.util.List;

/**
 * Description: 协议模板列表响应体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/2
 *
 * @author WuShengQiang
 */
public class AgreementTemplateVO {

    /**
     * 局域网配置
     */
    private List<AgreementConfigRequestDTO> lanTemplateList;

    /**
     * 广域网配置
     */
    private List<AgreementConfigRequestDTO> wanTemplateList;

    public List<AgreementConfigRequestDTO> getLanTemplateList() {
        return lanTemplateList;
    }

    public void setLanTemplateList(List<AgreementConfigRequestDTO> lanTemplateList) {
        this.lanTemplateList = lanTemplateList;
    }

    public List<AgreementConfigRequestDTO> getWanTemplateList() {
        return wanTemplateList;
    }

    public void setWanTemplateList(List<AgreementConfigRequestDTO> wanTemplateList) {
        this.wanTemplateList = wanTemplateList;
    }
}
