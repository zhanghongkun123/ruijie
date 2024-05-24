package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.InfeTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/22 13:54
 *
 * @author linrenjian
 */
public class CmsInfoDTO {
    @Nullable
    private InfeTypeEnum info;

    /**
     * 任务ID
     */
    @NotNull
    private UUID taskid;

    /**
     * 文件路径
     */
    @Nullable
    private String resultTaskPath;

    /**
     * 文件相对路径
     */
    @Nullable
    private String ftpTaskRelativePath;

    /**
     * 容器内部文件TXT文件的绝对路径
     */
    @Nullable
    private String insideTxtAbsolutePath;

    /**
     * 容器内部文件相对路径
     */
    @Nullable
    private String insideRelativePath;


    /**
     * RCDC 内部任务ID
     */
    @Nullable
    private UUID resultTaskid;

    /**
     * RCDC 统计数量
     */
    @Nullable
    private int count;



    /**
     * 构造ZIP 文件绝对路径
     * 
     * @return 文件绝对路径
     */
    public String buildZipFileAbsolutePath() {

        return insideRelativePath + resultTaskid.toString() + ".zip";
    }

    /**
     * 构造FTP ZIP 文件路径
     *
     * @return 返回FTP文件路径提供CMS使用
     */
    public String buildFtpZipFileResultPath() {

        return ftpTaskRelativePath + resultTaskid.toString() + ".zip";
    }

    public InfeTypeEnum getInfo() {
        return info;
    }

    public void setInfo(InfeTypeEnum info) {
        this.info = info;
    }

    public UUID getTaskid() {
        return taskid;
    }

    public void setTaskid(UUID taskid) {
        this.taskid = taskid;
    }

    public String getResultTaskPath() {
        return resultTaskPath;
    }

    public void setResultTaskPath(String resultTaskPath) {
        this.resultTaskPath = resultTaskPath;
    }

    public UUID getResultTaskid() {
        return resultTaskid;
    }

    public void setResultTaskid(UUID resultTaskid) {
        this.resultTaskid = resultTaskid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getInsideRelativePath() {
        return insideRelativePath;
    }

    public void setInsideRelativePath(String insideRelativePath) {
        this.insideRelativePath = insideRelativePath;
    }

    public String getInsideTxtAbsolutePath() {
        return insideTxtAbsolutePath;
    }

    public void setInsideTxtAbsolutePath(String insideTxtAbsolutePath) {
        this.insideTxtAbsolutePath = insideTxtAbsolutePath;
    }

    @Nullable
    public String getFtpTaskRelativePath() {
        return ftpTaskRelativePath;
    }

    public void setFtpTaskRelativePath(@Nullable String ftpTaskRelativePath) {
        this.ftpTaskRelativePath = ftpTaskRelativePath;
    }
}
