package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.def.enums.OperationControlType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.SpiceDisplayStrategyType;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月30日
 * 
 * @author Ghang
 */
public class PartDisplayStrategyDTO {
    
    private UUID id;
    
    private String appName;
    
    private SpiceDisplayStrategyType displayStrategy;
    
    private OperationControlType operationStrategy;
    
    private String operationControlValue;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public SpiceDisplayStrategyType getDisplayStrategy() {
        return displayStrategy;
    }

    public void setDisplayStrategy(SpiceDisplayStrategyType displayStrategy) {
        this.displayStrategy = displayStrategy;
    }

    public OperationControlType getOperationStrategy() {
        return operationStrategy;
    }

    public void setOperationStrategy(OperationControlType operationStrategy) {
        this.operationStrategy = operationStrategy;
    }

    public String getOperationControlValue() {
        return operationControlValue;
    }

    public void setOperationControlValue(String operationControlValue) {
        this.operationControlValue = operationControlValue;
    }

}
