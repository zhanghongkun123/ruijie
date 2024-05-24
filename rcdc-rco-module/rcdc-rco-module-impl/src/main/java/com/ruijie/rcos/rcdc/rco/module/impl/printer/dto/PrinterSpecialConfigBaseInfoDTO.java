package com.ruijie.rcos.rcdc.rco.module.impl.printer.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: 打印机特殊配置的详细信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/24
 *
 * @author chenjiehui
 */
public class PrinterSpecialConfigBaseInfoDTO {

    @JSONField(name = "hash")
    private String configMd5;

    @JSONField(name = "ver")
    private Long configVersion;

    @JSONField(name = "content")
    private String configContent;

    public String getConfigMd5() {
        return configMd5;
    }

    public void setConfigMd5(String configMd5) {
        this.configMd5 = configMd5;
    }


    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Long getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(Long configVersion) {
        this.configVersion = configVersion;
    }
}
