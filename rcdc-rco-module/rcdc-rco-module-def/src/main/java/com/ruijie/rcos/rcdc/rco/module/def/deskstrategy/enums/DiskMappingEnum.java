package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums;

import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * Description: 磁盘映射枚举类，关闭、只读、读写
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/11 15:23
 *
 * @author yxq
 */
public enum DiskMappingEnum {
    /**
     * 关闭
     */
    CLOSED(Boolean.FALSE, Boolean.FALSE),
    /**
     * 只读
     */
    READ_ONLY(Boolean.TRUE, Boolean.FALSE),
    /**
     * 可读写
     */
    READ_WRITE(Boolean.TRUE, Boolean.TRUE);

    private Boolean enableDiskMapping;

    private Boolean enableDiskMappingWriteable;

    DiskMappingEnum(Boolean enableDiskMapping, Boolean enableDiskMappingWriteable) {
        this.enableDiskMapping = enableDiskMapping;
        this.enableDiskMappingWriteable = enableDiskMappingWriteable;
    }

    public Boolean getEnableDiskMapping() {
        return enableDiskMapping;
    }

    public Boolean getEnableDiskMappingWriteable() {
        return enableDiskMappingWriteable;
    }

    /**
     * 获取diskMapping
     *
     * @param enableDiskMapping 是否开启
     * @param enableDiskMappingWriteable 是否可行
     * @return Optional<DiskMappingEnum>
     */
    public static Optional<DiskMappingEnum> getNetDiskMappingEnum(@Nullable Boolean enableDiskMapping, @Nullable Boolean enableDiskMappingWriteable) {

        for (DiskMappingEnum diskMappingEnum : DiskMappingEnum.values()) {
            if (diskMappingEnum.enableDiskMapping.equals(enableDiskMapping)
                    && diskMappingEnum.enableDiskMappingWriteable.equals(enableDiskMappingWriteable)) {
                return Optional.ofNullable(diskMappingEnum);
            }
        }
        return Optional.empty();
    }
}
