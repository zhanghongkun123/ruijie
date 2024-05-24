package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.set;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.SyslogSendCycleEnum;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 编辑定时发送日志DTO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/13 22:18
 *
 * @author yxq
 */
@ApiModel("syslog定时任务请求")
public class EditSyslogScheduleWebRequest {

    @ApiModelProperty(value = "备份时间")
    @Nullable
    private String scheduleTime;

    @ApiModelProperty(value = "发送周期类型")
    @NotNull
    private SyslogSendCycleEnum sendCycleType = SyslogSendCycleEnum.DAY;

    @ApiModelProperty(value = "间隔分钟时间")
    @Nullable
    @Range(min = "1", max = "1440")
    private Integer intervalMinute;

    @Nullable
    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(@Nullable String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public SyslogSendCycleEnum getSendCycleType() {
        return sendCycleType;
    }

    public void setSendCycleType(SyslogSendCycleEnum sendCycleType) {
        this.sendCycleType = sendCycleType;
    }

    @Nullable
    public Integer getIntervalMinute() {
        return intervalMinute;
    }

    public void setIntervalMinute(@Nullable Integer intervalMinute) {
        this.intervalMinute = intervalMinute;
    }
}
