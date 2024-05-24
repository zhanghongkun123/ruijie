package com.ruijie.rcos.rcdc.rco.module.impl.message.computer;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/9 14:33
 *
 * @author ketb
 */
public class QueryAssistantVersionResponseContent extends BaseResponseContent {

    private String mainVersion;

    private String minorVersion;

    private String threeVersion;

    private String fourVersion;

    public QueryAssistantVersionResponseContent(String business) {
        super(business);
    }

    public String getMainVersion() {
        return mainVersion;
    }

    public void setMainVersion(String mainVersion) {
        this.mainVersion = mainVersion;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    public String getThreeVersion() {
        return threeVersion;
    }

    public void setThreeVersion(String threeVersion) {
        this.threeVersion = threeVersion;
    }

    public String getFourVersion() {
        return fourVersion;
    }

    public void setFourVersion(String fourVersion) {
        this.fourVersion = fourVersion;
    }
}
