package com.ruijie.rcos.rcdc.rco.module.def.imageexport.request;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/21 14:58
 *
 * @author ketb
 */
public class CheckExportSizeOverResponse extends DefaultResponse {

    private boolean isOver;

    private int limitSize;

    public CheckExportSizeOverResponse() {
    }

    public CheckExportSizeOverResponse(boolean isOver, int limitSize) {
        this.isOver = isOver;
        this.limitSize = limitSize;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }
}
