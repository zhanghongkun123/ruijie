package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto;

/**
 * 
 * Description: license文件上传之前的操作
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月19日
 * 
 * @author zouqi
 */
public class BaseUploadLicFileBeforeDTO {
    
    private BaseUploadDTO[] baseUploadLicFileArr;
    
    /**是否为zip上传*/
    private boolean isZipUpload;

    public BaseUploadDTO[] getBaseUploadLicFileArr() {
        return baseUploadLicFileArr;
    }

    public void setBaseUploadLicFileArr(BaseUploadDTO[] baseUploadLicFileArr) {
        this.baseUploadLicFileArr = baseUploadLicFileArr;
    }

    public boolean isZipUpload() {
        return isZipUpload;
    }

    public void setZipUpload(boolean isZipUpload) {
        this.isZipUpload = isZipUpload;
    }
    
}
