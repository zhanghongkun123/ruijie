package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.RecycleBinConfigStateEnum;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 设置定期清理回收站
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年6月19日 <br>
 * 
 * @author dan
 */
public class RecycleBinConfigEditWebRequest implements WebRequest {

    /**
     * true-启用，false-禁用
     */
    @NotNull
    private RecycleBinConfigStateEnum enable;

    /**
     * 周期，单位为月
     */
    @Nullable
    @NumberFormat
    private Integer cycle;

    public RecycleBinConfigStateEnum getEnable() {
        return enable;
    }

    public void setEnable(RecycleBinConfigStateEnum enable) {
        this.enable = enable;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public Integer getCycle() {
        return cycle;
    }
}
