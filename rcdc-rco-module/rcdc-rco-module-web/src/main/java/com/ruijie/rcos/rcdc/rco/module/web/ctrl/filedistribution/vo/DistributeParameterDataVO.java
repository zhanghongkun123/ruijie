package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo;

import java.util.UUID;

/**
 * Description: 分发任务参数运行数据结构VO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/25 17:24
 *
 * @author zhangyichi
 */
public class DistributeParameterDataVO {

    /**
     * 文件ID
     */
    private UUID id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 执行参数
     */
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
