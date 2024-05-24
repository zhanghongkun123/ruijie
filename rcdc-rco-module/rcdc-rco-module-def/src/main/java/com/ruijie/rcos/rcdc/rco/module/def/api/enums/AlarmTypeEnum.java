package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author wanmulin
 */
public enum AlarmTypeEnum {

    SERVER(AlarmConstants.ALARM_TYPE_SERVER), DESKTOP(AlarmConstants.ALARM_TYPE_DESKTOP);

    private String name;

    AlarmTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
