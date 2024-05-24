package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/19 16:02
 *
 * @author coderLee23
 */
public class DistributeParameterSeedDataDTO {

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
     * 软件安装包大小
     */
    private Long fileSize;

    /**
     * 种子名称
     */
    private String torrentName;

    /**
     * 种子路径：ftp相对路径
     */
    private String torrentPath;

    /**
     * BT种子MD5值
     */
    private String torrentMd5;

    /**
     * 安装包唯一标识
     */
    private String fileMd5;

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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getTorrentName() {
        return torrentName;
    }

    public void setTorrentName(String torrentName) {
        this.torrentName = torrentName;
    }

    public String getTorrentPath() {
        return torrentPath;
    }

    public void setTorrentPath(String torrentPath) {
        this.torrentPath = torrentPath;
    }

    public String getTorrentMd5() {
        return torrentMd5;
    }

    public void setTorrentMd5(String torrentMd5) {
        this.torrentMd5 = torrentMd5;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}
