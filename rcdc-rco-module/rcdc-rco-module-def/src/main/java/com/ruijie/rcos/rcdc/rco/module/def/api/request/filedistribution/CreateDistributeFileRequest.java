package com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeFileDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;

/**
 * Description: 创建分发文件请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/11 15:28
 *
 * @author zhangyichi
 */
public class CreateDistributeFileRequest extends DefaultRequest {

    private String sourcePath;

    private String targetPath;

    private DistributeFileDTO fileDTO;

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public DistributeFileDTO getFileDTO() {
        return fileDTO;
    }

    public void setFileDTO(DistributeFileDTO fileDTO) {
        this.fileDTO = fileDTO;
    }

}
