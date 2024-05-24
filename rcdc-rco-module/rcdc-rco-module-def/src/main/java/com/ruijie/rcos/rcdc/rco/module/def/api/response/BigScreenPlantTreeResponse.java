package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.lang.Nullable;

/**
 * Description: 大屏 碳排放 资源节省信息统计
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
public class BigScreenPlantTreeResponse extends DefaultResponse {

    /**
     * 节电量
     */
    @Nullable
    private Float electricalSaving;

    /**
     * 减少碳排放量
     */
    @Nullable
    private Float carbonEmissions;

    /**
     * 植树量
     */
    @Nullable
    private Float plantTrees;

    public Float getElectricalSaving() {
        return electricalSaving;
    }

    public void setElectricalSaving(Float electricalSaving) {
        this.electricalSaving = electricalSaving;
    }

    public Float getCarbonEmissions() {
        return carbonEmissions;
    }

    public void setCarbonEmissions(Float carbonEmissions) {
        this.carbonEmissions = carbonEmissions;
    }

    public Float getPlantTrees() {
        return plantTrees;
    }

    public void setPlantTrees(Float plantTrees) {
        this.plantTrees = plantTrees;
    }
}
