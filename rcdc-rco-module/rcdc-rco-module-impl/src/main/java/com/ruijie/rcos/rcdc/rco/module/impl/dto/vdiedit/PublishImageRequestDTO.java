package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 16:10
 *
 * @author zhangyichi
 */
public class PublishImageRequestDTO extends VDIEditImageIdRequestDTO {

    private String changeLog;

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }
}
