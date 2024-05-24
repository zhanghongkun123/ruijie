package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.sk.base.annotation.TextMedium;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-03-09
 *
 * @author linhj
 */
public class AbstractDeskStrategyWebRequest {

    /**
     * 盘符预占配置
     */
    @ApiModelProperty(value = "盘符预占配置")
    @Nullable
    private String[] desktopOccupyDriveArr;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @TextMedium
    private String note;

    @Nullable
    public String[] getDesktopOccupyDriveArr() {
        return desktopOccupyDriveArr;
    }

    public void setDesktopOccupyDriveArr(@Nullable String[] desktopOccupyDriveArr) {
        this.desktopOccupyDriveArr = desktopOccupyDriveArr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
