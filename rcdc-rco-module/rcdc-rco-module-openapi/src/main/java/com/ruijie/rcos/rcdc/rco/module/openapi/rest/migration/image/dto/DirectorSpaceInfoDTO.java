package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月07日
 *
 * @author xgx
 */
public class DirectorSpaceInfoDTO {
    private Long totalSpace;

    private Long usedSpace;

    public Long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(Long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public Long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(Long usedSpace) {
        this.usedSpace = usedSpace;
    }
}
