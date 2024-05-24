package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/16 21:22
 *
 * @author zsm
 */
public class GeneralAuthDetailInfoDTO {

    private String authType;

    private String licenseDurationType;

    private Integer total;

    private Integer used;

    private Integer usedByVoi;

    private Integer usedByIdv;

    private Integer usedByVdi;

    private Integer usedByRca;


    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getLicenseDurationType() {
        return licenseDurationType;
    }

    public void setLicenseDurationType(String licenseDurationType) {
        this.licenseDurationType = licenseDurationType;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getUsedByVoi() {
        return usedByVoi;
    }

    public void setUsedByVoi(Integer usedByVoi) {
        this.usedByVoi = usedByVoi;
    }

    public Integer getUsedByIdv() {
        return usedByIdv;
    }

    public void setUsedByIdv(Integer usedByIdv) {
        this.usedByIdv = usedByIdv;
    }

    public Integer getUsedByVdi() {
        return usedByVdi;
    }

    public void setUsedByVdi(Integer usedByVdi) {
        this.usedByVdi = usedByVdi;
    }

    public Integer getUsedByRca() {
        return usedByRca;
    }

    public void setUsedByRca(Integer usedByRca) {
        this.usedByRca = usedByRca;
    }
}
