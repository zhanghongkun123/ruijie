package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ConfigurationWizardCustomDataDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;



/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/2  <br>
 *
 * @author yyz
 */
public class SetConfigurationWizardWebRequest implements WebRequest {

    @NotNull
    private Boolean show;

    @NotNull
    private Integer index;
    
    //是否加入用户体验计划
    @NotNull
    private Boolean isJoinUserExperiencePlan;

    @Nullable
    private ConfigurationWizardCustomDataDTO[] customDataArr;

    public void setCustomDataArr(ConfigurationWizardCustomDataDTO[] customDataArr) {
        this.customDataArr = customDataArr;
    }

    public ConfigurationWizardCustomDataDTO[] getCustomDataArr() {

        return customDataArr;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getIsJoinUserExperiencePlan() {
        return isJoinUserExperiencePlan;
    }

    public void setIsJoinUserExperiencePlan(Boolean isJoinUserExperiencePlan) {
        this.isJoinUserExperiencePlan = isJoinUserExperiencePlan;
    }
}
