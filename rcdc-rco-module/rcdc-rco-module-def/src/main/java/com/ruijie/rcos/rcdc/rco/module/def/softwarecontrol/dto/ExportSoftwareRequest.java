package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto;


import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 
 * Description: 导出软件信息回应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/16
 *
 * @author lihengjing
 */
public class ExportSoftwareRequest {

    @Nullable
    private String userId;

    @Nullable
    private Sort[] sortArr;

    @Nullable
    private UUID softwareGroupId;


    public ExportSoftwareRequest(String userId, UUID softwareGroupId) {
        this.userId = userId;
        this.softwareGroupId = softwareGroupId;
    }

    public ExportSoftwareRequest(String userId, Sort[] sortArr, UUID softwareGroupId) {
        this.userId = userId;
        this.sortArr = sortArr;
        this.softwareGroupId = softwareGroupId;
    }

    /**
     * 构造key
     * @return 返回key
     */
    public String genExportSoftwareKey() {
        String userId = this.getUserId();
        StringBuffer sb = new StringBuffer();
        sb.append(userId);
        if (this.getSoftwareGroupId() != null) {
            sb.append("_").append(this.getSoftwareGroupId());
        }

        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Nullable
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    public UUID getSoftwareGroupId() {
        return softwareGroupId;
    }

    public void setSoftwareGroupId(UUID softwareGroupId) {
        this.softwareGroupId = softwareGroupId;
    }
}
