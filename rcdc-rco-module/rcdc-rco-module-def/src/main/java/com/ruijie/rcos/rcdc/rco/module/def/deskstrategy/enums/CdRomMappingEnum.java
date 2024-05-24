package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums;

import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * Description: CDROM映射枚举类，关闭、只读、读写
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月24日
 *
 * @author dongquanyi
 */
public enum CdRomMappingEnum {
    /**
     * 关闭
     */
    CLOSED(Boolean.FALSE, Boolean.FALSE),
    /**
     * 只读
     */
    READ_ONLY(Boolean.TRUE, Boolean.FALSE);

    private Boolean enableCdRomMapping;

    private Boolean enableCDRomMappingWriteable;

    CdRomMappingEnum(Boolean enableCdRomMapping, Boolean enableCDRomMappingWriteable) {
        this.enableCdRomMapping = enableCdRomMapping;
        this.enableCDRomMappingWriteable = enableCDRomMappingWriteable;
    }

    public Boolean getEnableCdRomMapping() {
        return enableCdRomMapping;
    }

    public Boolean getEnableCDRomMappingWriteable() {
        return enableCDRomMappingWriteable;
    }

    /**
     * 获取CdRomMappingEnum
     * 
     * @param enableCdRomMapping 是否开启磁盘映射
     * @param enableCDRomMappingWriteable 是否开启可写
     * @return Optional<CdRomMappingEnum>
     */
    public static Optional<CdRomMappingEnum> getCdRomMappingEnum(@Nullable Boolean enableCdRomMapping,
            @Nullable Boolean enableCDRomMappingWriteable) {
        for (CdRomMappingEnum cdRomMapping : CdRomMappingEnum.values()) {
            if (cdRomMapping.enableCdRomMapping.equals(enableCdRomMapping)
                    && cdRomMapping.enableCDRomMappingWriteable.equals(enableCDRomMappingWriteable)) {
                return Optional.ofNullable(cdRomMapping);
            }
        }

        return Optional.empty();
    }

}
