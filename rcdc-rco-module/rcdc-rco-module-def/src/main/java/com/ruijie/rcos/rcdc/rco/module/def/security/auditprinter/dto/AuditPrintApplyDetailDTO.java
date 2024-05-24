package com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;

/**
 * Description: 文件导出审批申请详情
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class AuditPrintApplyDetailDTO extends AuditApplyDetailDTO {

    private List<AuditFilePrintInfoDTO> auditFilePrintInfoList;

    public List<AuditFilePrintInfoDTO> getAuditFilePrintInfoList() {
        return this.auditFilePrintInfoList;
    }

    public void setAuditFilePrintInfoList(List<AuditFilePrintInfoDTO> auditFilePrintInfoList) {
        this.auditFilePrintInfoList = auditFilePrintInfoList;
    }
}
