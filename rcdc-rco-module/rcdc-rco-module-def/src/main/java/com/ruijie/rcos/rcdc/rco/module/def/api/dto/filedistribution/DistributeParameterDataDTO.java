package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

/**
 * Description: 分发任务参数运行数据结构
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 10:02
 *
 * @author zhangyichi
 */
public class DistributeParameterDataDTO {

    /**
     * 文件ID
     */
    private UUID id;

    /**
     * 文件名
     */
    @JSONField(name = "filename")
    private String fileName;

    /**
     * 执行参数
     */
    @JSONField(name = "exec_par")
    private String execPar;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExecPar() {
        return execPar;
    }

    public void setExecPar(String execPar) {
        this.execPar = execPar;
    }

}
