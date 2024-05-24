package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dto;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;

import java.util.List;

/**
 * Description: 获取文件流转审计申请单列表响应体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/26
 *
 * @author WuShengQiang
 */
public class GetAuditFileApplyGuestToolMsgContentDTO {

    private String requestId;

    private Integer total = 0;

    private Integer pageIndex = 0;

    private Integer pageSize = 0;

    private List<AuditApplyDetailDTO> applyList;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public List<AuditApplyDetailDTO> getApplyList() {
        return applyList;
    }

    public void setApplyList(List<AuditApplyDetailDTO> applyList) {
        this.applyList = applyList;
    }
}
