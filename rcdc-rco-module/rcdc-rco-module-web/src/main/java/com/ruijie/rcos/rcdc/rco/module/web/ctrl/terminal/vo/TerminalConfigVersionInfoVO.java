package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeNodeVO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description: 终端硬件配置版本信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/08
 *
 * @author clone
 */
public class TerminalConfigVersionInfoVO {
    private String version;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
