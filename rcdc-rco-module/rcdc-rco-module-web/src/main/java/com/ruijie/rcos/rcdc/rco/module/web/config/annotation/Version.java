package com.ruijie.rcos.rcdc.rco.module.web.config.annotation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.10
 *
 * @author linhj
 */
public enum Version {

    V1_0_0("1.0.0", "汇总"), // 基线版本
    V3_1_1("3.1.1", "RCO_5.4_R1"),
    V3_1_461("3.1.54", "RCO_5.4_R1A4"),
    V3_1_160("3.1.160", "RCO_6.0_R4"),
    V3_2_0("3.2.0", "RCO_7.0_R1"),
    V3_2_101("3.2.101", "RCO_7.0_R2"),
    V3_2_156("3.2.156", "企金1.0"),
    V3_2_221("3.2.221", "企金1.1"),
    V3_2_271("3.2.271", "企金1.1R1T1"),
    V6_0_1("6.0.1", "云平台1.0"),
    V6_0_20("6.0.20", "RCO-ENT_V2.0_R1"),
    V6_0_50("6.0.50", "企金2.0");
    Version(String mavenVersion, String productVersion) {
        this.mavenVersion = mavenVersion;
        this.productVersion = productVersion;
    }

    private final String mavenVersion;
    private final String productVersion;

    public String getMavenVersion() {
        return mavenVersion;
    }

    public String getProductVersion() {
        return productVersion;
    }
}
