package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/2  <br>
 *
 * @author yyz
 */
public class ConfigurationWizardDTO {

    private boolean show;

    private int index;

    //是否加入用户体验计划
    private boolean isJoinUserExperiencePlan;

    private ConfigurationWizardCustomDataDTO[] customDataArr;

    public void setCustomDataArr(ConfigurationWizardCustomDataDTO[] customDataArr) {
        this.customDataArr = customDataArr;
    }

    public ConfigurationWizardCustomDataDTO[] getCustomDataArr() {

        return customDataArr;
    }

    public boolean getShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isJoinUserExperiencePlan() {
        return isJoinUserExperiencePlan;
    }

    public void setJoinUserExperiencePlan(boolean isJoinUserExperiencePlan) {
        this.isJoinUserExperiencePlan = isJoinUserExperiencePlan;
    }

}
