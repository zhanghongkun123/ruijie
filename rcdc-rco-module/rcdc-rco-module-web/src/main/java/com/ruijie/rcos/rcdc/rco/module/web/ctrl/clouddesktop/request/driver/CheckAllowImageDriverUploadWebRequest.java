package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.driver;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCheckAllowImageDriverUploadDTO;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/16
 *
 * @author songxiang
 */
public class CheckAllowImageDriverUploadWebRequest implements WebRequest {

    @NotEmpty
    private CbbCheckAllowImageDriverUploadDTO[] fileInfoArr;

    public CbbCheckAllowImageDriverUploadDTO[] getFileInfoArr() {
        return fileInfoArr;
    }

    public void setFileInfoArr(CbbCheckAllowImageDriverUploadDTO[] fileInfoArr) {
        this.fileInfoArr = fileInfoArr;
    }
}
