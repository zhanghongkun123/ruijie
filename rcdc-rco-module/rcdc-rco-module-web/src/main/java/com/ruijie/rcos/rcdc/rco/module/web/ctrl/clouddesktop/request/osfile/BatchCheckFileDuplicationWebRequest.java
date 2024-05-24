package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.osfile;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.FileInfoDTO;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: CheckSoftNameDuplicationWebRequest Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/09/28
 *
 * @author liangyifeng
 */
public class BatchCheckFileDuplicationWebRequest implements WebRequest {

    @NotEmpty
    private FileInfoDTO[] fileInfoArr;

    public FileInfoDTO[] getFileInfoArr() {
        return fileInfoArr;
    }

    public void setFileInfoArr(FileInfoDTO[] fileInfoArr) {
        this.fileInfoArr = fileInfoArr;
    }
}
