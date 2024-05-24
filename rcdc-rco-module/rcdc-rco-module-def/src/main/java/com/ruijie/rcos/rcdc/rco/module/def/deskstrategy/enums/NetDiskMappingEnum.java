package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums;

import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * Description: 网盘映射枚举类，关闭、只读、读写
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月24日
 *
 * @author dongquanyi
 */
public enum NetDiskMappingEnum {
    /**
     * 关闭
     */
    CLOSED(Boolean.FALSE, Boolean.FALSE),
    /**
     * 只读
     */
    READ_ONLY(Boolean.TRUE, Boolean.FALSE),
    /**
     *
     */
    READ_WRITE(Boolean.TRUE, Boolean.TRUE);

    private Boolean enableNetDiskMapping;

    private Boolean enableNetDiskMappingWriteable;

    NetDiskMappingEnum(Boolean enableNetDiskMapping, Boolean enableNetDiskMappingWriteable) {
        this.enableNetDiskMapping = enableNetDiskMapping;
        this.enableNetDiskMappingWriteable = enableNetDiskMappingWriteable;
    }

    public Boolean getEnableNetDiskMapping() {
        return enableNetDiskMapping;
    }

    public Boolean getEnableNetDiskMappingWriteable() {
        return enableNetDiskMappingWriteable;
    }

    /**
     * 获取netDiskMapping
     * 
     * @param enableNetDiskMapping 是否开启
     * @param enableNetDiskMappingWriteable 是否可行
     * @return Optional<NetDiskMappingEnum>
     */
    public static Optional<NetDiskMappingEnum> getNetDiskMappingEnum(@Nullable Boolean enableNetDiskMapping,
            @Nullable Boolean enableNetDiskMappingWriteable) {

        for (NetDiskMappingEnum diskMappingEnum : NetDiskMappingEnum.values()) {
            if (diskMappingEnum.enableNetDiskMapping.equals(enableNetDiskMapping)
                    && diskMappingEnum.enableNetDiskMappingWriteable.equals(enableNetDiskMappingWriteable)) {
                return Optional.ofNullable(diskMappingEnum);
            }
        }
        return Optional.empty();
    }
}
