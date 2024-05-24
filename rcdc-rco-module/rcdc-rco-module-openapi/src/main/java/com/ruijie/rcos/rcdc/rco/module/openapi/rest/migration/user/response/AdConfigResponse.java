package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.response;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacLdapServerDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdCoverTypeEnum;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
public class AdConfigResponse {

    private String serverName;

    private String serverIp;

    private String serverPort;

    private String domainName;

    private Boolean enable;

    private String managerName;

    private Boolean autoJoin;

    private IacAdCoverTypeEnum coverType;

    private Boolean adAutoLogon;

    private IacLdapServerDTO mainServer;

    private IacLdapServerDTO[] backupServerArr;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Boolean getAutoJoin() {
        return autoJoin;
    }

    public void setAutoJoin(Boolean autoJoin) {
        this.autoJoin = autoJoin;
    }

    public IacAdCoverTypeEnum getCoverType() {
        return coverType;
    }

    public void setCoverType(IacAdCoverTypeEnum coverType) {
        this.coverType = coverType;
    }

    public Boolean getAdAutoLogon() {
        return adAutoLogon;
    }

    public void setAdAutoLogon(Boolean adAutoLogon) {
        this.adAutoLogon = adAutoLogon;
    }

    public IacLdapServerDTO getMainServer() {
        return mainServer;
    }

    public void setMainServer(IacLdapServerDTO mainServer) {
        this.mainServer = mainServer;
    }

    public IacLdapServerDTO[] getBackupServerArr() {
        return backupServerArr;
    }

    public void setBackupServerArr(IacLdapServerDTO[] backupServerArr) {
        this.backupServerArr = backupServerArr;
    }
}
