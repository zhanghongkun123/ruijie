package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 15:52
 *
 * @author linrenjian
 */
public class DesksoftUseConfigDTO {


    /**
     * 软件上报状态
     */
    private Boolean desksoftMsgStatus;

    /**
     * CMC开关状态
     */
    private String cmcStatus;

    public Boolean getDesksoftMsgStatus() {
        return desksoftMsgStatus;
    }

    public void setDesksoftMsgStatus(Boolean desksoftMsgStatus) {
        this.desksoftMsgStatus = desksoftMsgStatus;
    }

    public String getCmcStatus() {
        return cmcStatus;
    }

    public void setCmcStatus(String cmcStatus) {
        this.cmcStatus = cmcStatus;
    }
}
