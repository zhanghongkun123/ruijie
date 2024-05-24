package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto;

/**
 * Description: SoftwareControlGuestToolMsgContentDTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author wuShengQiang
 */
public class SoftwareControlGuestToolMsgContentDTO {

    private String requestId;

    private Integer count;

    private Integer total = 0;

    private Integer pageIndex = 0;

    private Integer pageSize = 0;

    /*软控策略是否开启 */
    private Boolean isOpen;

    private String runType;

    private Boolean isWhiteListMode;

    /**
     * 白名单的清单
     */
    private SoftwareResponseDTO[] softwareArr;

    /**
     * 黑名单清单
     */
    private SoftwareResponseDTO[] blackSoftwareArr;

    private String softwareStrategyVersion;

    /**
     * 预留字段，跟GT约定的，防止后期返回值修改，GT未升级可能解析会奔溃
     */
    private Integer version;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public SoftwareResponseDTO[] getSoftwareArr() {
        return softwareArr;
    }

    public void setSoftwareArr(SoftwareResponseDTO[] softwareArr) {
        this.softwareArr = softwareArr;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean open) {
        isOpen = open;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public Boolean getIsWhiteListMode() {
        return isWhiteListMode;
    }

    public void setIsWhiteListMode(Boolean whitelistMode) {
        isWhiteListMode = whitelistMode;
    }

    public SoftwareResponseDTO[] getBlackSoftwareArr() {
        return blackSoftwareArr;
    }

    public void setBlackSoftwareArr(SoftwareResponseDTO[] blackSoftwareArr) {
        this.blackSoftwareArr = blackSoftwareArr;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSoftwareStrategyVersion() {
        return softwareStrategyVersion;
    }

    public void setSoftwareStrategyVersion(String softwareStrategyVersion) {
        this.softwareStrategyVersion = softwareStrategyVersion;
    }
}
