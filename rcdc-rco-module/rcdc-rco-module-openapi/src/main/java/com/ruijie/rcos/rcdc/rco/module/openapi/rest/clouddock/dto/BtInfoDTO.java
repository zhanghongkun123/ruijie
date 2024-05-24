package com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto;

/**
 * Description: BT信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @author chenjuan
 */
public class BtInfoDTO {

    private String torrentFileName;

    private String torrentFilePath;

    private String torrentFileMD5;

    public String getTorrentFileName() {
        return torrentFileName;
    }

    public void setTorrentFileName(String torrentFileName) {
        this.torrentFileName = torrentFileName;
    }

    public String getTorrentFilePath() {
        return torrentFilePath;
    }

    public void setTorrentFilePath(String torrentFilePath) {
        this.torrentFilePath = torrentFilePath;
    }

    public String getTorrentFileMD5() {
        return torrentFileMD5;
    }

    public void setTorrentFileMD5(String torrentFileMD5) {
        this.torrentFileMD5 = torrentFileMD5;
    }
}
