package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.log4jconfig;

import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年02月19日
 *
 * @author GuoZhouYue
 */
public class BaseAddLog4jConfigWebRequest implements WebRequest {

    @TextMedium
    private String loggerName;

    @TextShort
    private String loggerLevel;

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public String getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(String loggerLevel) {
        this.loggerLevel = loggerLevel;
    }
}
