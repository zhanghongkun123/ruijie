package com.ruijie.rcos.rcdc.rco.module.def.imageexport.request;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年04月14日
 *
 * @author xwx
 */
public class CheckExportSizeOverRequest {
    /**
     * 将要导出的文件个数
     */
    private Integer numOfExportImage;

    public CheckExportSizeOverRequest(int numOfExportImage) {
        this.numOfExportImage = numOfExportImage;
    }

    public Integer getNumOfExportImage() {
        return numOfExportImage;
    }

    public void setNumOfExportImage(Integer numOfExportImage) {
        this.numOfExportImage = numOfExportImage;
    }
}
