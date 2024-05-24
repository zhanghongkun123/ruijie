package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.vo;

import com.ruijie.rcos.base.alarm.module.def.enums.AlarmType;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月10日
 *
 * @author xgx
 */
public class AlarmIdLabelVO {
    private AlarmType id;

    private String label;

    public AlarmIdLabelVO() {
    }

    public AlarmIdLabelVO(AlarmType id, String label) {
        this.id = id;
        this.label = label;
    }

    public AlarmType getId() {
        return id;
    }

    public void setId(AlarmType id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
