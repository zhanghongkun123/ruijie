package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.spi.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dto.GuesttoolAuditPrinterStrategyDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Description: 安全打印机全局策略配置GT响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class AuditPrinterStrategyResponse {
    /**
     * 请求ID
     */
    private UUID requestId;

    /**
     * 安全打印机配置信息
     */
    @JSONField(serialize = false)
    private AuditPrinterStrategyDTO auditPrinterStrategy;

    /**
     * GT读取安全打印机配置信息
     */
    @JSONField(name = "auditPrinterGlobalStrategy")
    private GuesttoolAuditPrinterStrategyDTO guesttoolAuditPrinterStrategyDTO;

    /**
     * Ftp配置信息
     */
    private FtpConfigDTO ftpInfo;

    public AuditPrinterStrategyResponse(AuditPrinterStrategyDTO auditPrinterStrategy, FtpConfigDTO ftpInfo) {
        this.guesttoolAuditPrinterStrategyDTO = convertDisplayContent(auditPrinterStrategy);
        this.auditPrinterStrategy = auditPrinterStrategy;
        this.ftpInfo = ftpInfo;
    }

    public AuditPrinterStrategyResponse() {

    }

    public UUID getRequestId() {
        return this.requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public AuditPrinterStrategyDTO getAuditPrinterStrategy() {
        return this.auditPrinterStrategy;
    }

    /**
     * 设置安全打印审计全局配置策略对象
     *
     * @param auditPrinterStrategy 安全打印审计全局配置策略对象
     */
    public void setAuditPrinterStrategy(AuditPrinterStrategyDTO auditPrinterStrategy) {
        Assert.notNull(auditPrinterStrategy, "auditPrinterStrategy must not null");
        this.guesttoolAuditPrinterStrategyDTO = convertDisplayContent(auditPrinterStrategy);
        this.auditPrinterStrategy = auditPrinterStrategy;
    }

    private GuesttoolAuditPrinterStrategyDTO convertDisplayContent(AuditPrinterStrategyDTO auditPrinterGlobalStrategy) {
        GuesttoolAuditPrinterStrategyDTO guesttoolAuditPrinterGlobalStrategyDTO = new GuesttoolAuditPrinterStrategyDTO();
        BeanUtils.copyProperties(auditPrinterGlobalStrategy, guesttoolAuditPrinterGlobalStrategyDTO);
        if (BooleanUtils.isTrue(auditPrinterGlobalStrategy.getEnableWatermark())
                && StringUtils.hasText(auditPrinterGlobalStrategy.getDisplayContent())) {
            guesttoolAuditPrinterGlobalStrategyDTO
                    .setDisplayContent(JSON.parseObject(auditPrinterGlobalStrategy.getDisplayContent(), WatermarkDisplayContentDTO.class));
        }
        return guesttoolAuditPrinterGlobalStrategyDTO;
    }

    public GuesttoolAuditPrinterStrategyDTO getGuesttoolAuditPrinterStrategyDTO() {
        return guesttoolAuditPrinterStrategyDTO;
    }

    public void setGuesttoolAuditPrinterStrategyDTO(GuesttoolAuditPrinterStrategyDTO guesttoolAuditPrinterStrategyDTO) {
        this.guesttoolAuditPrinterStrategyDTO = guesttoolAuditPrinterStrategyDTO;
    }

    public FtpConfigDTO getFtpInfo() {
        return this.ftpInfo;
    }

    public void setFtpInfo(FtpConfigDTO ftpInfo) {
        this.ftpInfo = ftpInfo;
    }
}
