package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import com.ruijie.rcos.base.alarm.module.def.enums.AlarmType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月10日
 *
 * @author xgx
 */
public class AlarmIdLabelRequest {
    @NotNull
    private AlarmType id;

    public AlarmType getId() {
        return id;
    }

    public void setId(AlarmType id) {
        this.id = id;
    }
}
