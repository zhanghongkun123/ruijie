package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.ObtainEduLicenseInfoDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/27
 *
 * @author linremjian
 */
public class ObtainEduLicenseInfoResponse {

    /**
     * VOI教育版证书
     */
    private ObtainEduLicenseInfoDTO voiLicenseInfoDTO;

    /**
     * CPU教育版证书
     */
    private ObtainEduLicenseInfoDTO cpuLicenseInfoDTO;

    public ObtainEduLicenseInfoDTO getVoiLicenseInfoDTO() {
        return voiLicenseInfoDTO;
    }

    public void setVoiLicenseInfoDTO(ObtainEduLicenseInfoDTO voiLicenseInfoDTO) {
        this.voiLicenseInfoDTO = voiLicenseInfoDTO;
    }

    public ObtainEduLicenseInfoDTO getCpuLicenseInfoDTO() {
        return cpuLicenseInfoDTO;
    }

    public void setCpuLicenseInfoDTO(ObtainEduLicenseInfoDTO cpuLicenseInfoDTO) {
        this.cpuLicenseInfoDTO = cpuLicenseInfoDTO;
    }
}
