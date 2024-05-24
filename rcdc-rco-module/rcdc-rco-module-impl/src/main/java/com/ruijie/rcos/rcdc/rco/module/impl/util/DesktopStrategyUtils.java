package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.CdRomMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DiskMappingEnum;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.NetDiskMappingEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Description: 云桌面策略工具类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-03-02
 *
 * @author linke
 */
public class DesktopStrategyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStrategyUtils.class);

    private DesktopStrategyUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 根据磁盘映射状态，获取是否开启磁盘映射、是否可写
     *
     * @param diskMappingType 磁盘映射状态
     * @return diskMapArr[0]：是否开启磁盘映射，diskMapArr[1]：是否可写
     * @throws BusinessException 业务异常
     */
    public static Boolean[] getVdiDiskMapArr(DiskMappingEnum diskMappingType) throws BusinessException {
        Assert.notNull(diskMappingType, "diskMappingType must not null");

        LOGGER.info("磁盘映射状态为：{}", diskMappingType);
        switch (diskMappingType) {
            case CLOSED:
                return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
            case READ_ONLY:
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
            case READ_WRITE:
            default:
                return new Boolean[]{Boolean.TRUE, Boolean.TRUE};
        }
    }

    /**
     * 根据是否开启磁盘映射、是否可写，获取磁盘映射状态
     *
     * @param enableDiskMapping 是否开启磁盘映射
     * @param enableDiskMappingWriteable 是否可写
     * @return 磁盘映射状态
     */
    public static DiskMappingEnum getDiskMappingEnum(Boolean enableDiskMapping, Boolean enableDiskMappingWriteable) {
        Assert.notNull(enableDiskMapping, "enableDiskMapping must not null");
        Assert.notNull(enableDiskMappingWriteable, "enableDiskMappingWriteable must not null");

        LOGGER.info("是否开启磁盘映射[{}]，是否可写[{}]", enableDiskMapping, enableDiskMappingWriteable);
        if (Boolean.TRUE.equals(enableDiskMapping)) {
            // 若开启磁盘映射，并且可读写
            if (Boolean.TRUE.equals(enableDiskMappingWriteable)) {
                return DiskMappingEnum.READ_WRITE;
            } else {
                // 开启磁盘映射，并且只读
                return DiskMappingEnum.READ_ONLY;
            }
        }
        return DiskMappingEnum.CLOSED;
    }

    /**
     * 根据是否开启网盘映射、是否可写，获取网盘映射状态
     *
     * @param enableNetDiskMapping 是否开启网盘映射
     * @param enableNetDiskMappingWriteable 是否可写
     * @return 网盘映射状态
     */
    public static NetDiskMappingEnum getNetDiskMappingEnum(Boolean enableNetDiskMapping, Boolean enableNetDiskMappingWriteable) {
        Assert.notNull(enableNetDiskMapping, "enableNetDiskMapping must not null");
        Assert.notNull(enableNetDiskMappingWriteable, "enableNetDiskMappingWriteable must not null");

        LOGGER.info("是否开启网盘映射[{}]，是否可写[{}]", enableNetDiskMapping, enableNetDiskMappingWriteable);
        if (Boolean.TRUE.equals(enableNetDiskMapping)) {
            // 若开启网盘映射，并且可读写
            if (Boolean.TRUE.equals(enableNetDiskMappingWriteable)) {
                return NetDiskMappingEnum.READ_WRITE;
            } else {
                // 开启网盘映射，并且只读
                return NetDiskMappingEnum.READ_ONLY;
            }
        }
        return NetDiskMappingEnum.CLOSED;
    }

    /**
     * 根据是否开启CDROM映射、是否可写，获取CDROM映射状态
     *
     * @param enableCDRomMapping 是否开启CDROM映射
     * @param enableCDRomWriteable 是否可写
     * @return CDROM映射状态
     */
    public static CdRomMappingEnum getCDRomMappingEnum(Boolean enableCDRomMapping, Boolean enableCDRomWriteable) {
        Assert.notNull(enableCDRomMapping, "enableCDRomMapping must not null");
        Assert.notNull(enableCDRomWriteable, "enableCDRomWriteable must not null");

        LOGGER.info("是否开启CDROM映射[{}]，是否可写[{}]", enableCDRomMapping, enableCDRomWriteable);
        if (Boolean.TRUE.equals(enableCDRomMapping)) {
            // 若开启CDROM映射，并且可读写
            if (Boolean.FALSE.equals(enableCDRomWriteable)) {
                // 开启CDROM映射，并且只读
                return CdRomMappingEnum.READ_ONLY;
            }
        }
        return CdRomMappingEnum.CLOSED;
    }

    /**
     * 根据网盘映射状态，获取是否开启网盘映射、是否可写
     *
     * @param netDiskMappingType 网盘映射状态
     * @return diskMapArr[0]：是否开启网盘映射，diskMapArr[1]：是否可写
     * @throws BusinessException 业务异常
     */
    public static Boolean[] getVdiNetDiskMapArr(NetDiskMappingEnum netDiskMappingType) throws BusinessException {
        Assert.notNull(netDiskMappingType, "netDiskMappingType must not null");

        LOGGER.info("网盘映射状态为：{}", netDiskMappingType);
        switch (netDiskMappingType) {
            case CLOSED:
                return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
            case READ_ONLY:
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
            case READ_WRITE:
            default:
                return new Boolean[]{Boolean.TRUE, Boolean.TRUE};
        }
    }

    /**
     * 根据磁盘映射状态，获取是否开启磁盘映射、是否可写
     *
     * @param cdRomMappingType 网盘映射状态
     * @return diskMapArr[0]：是否开启网盘映射，diskMapArr[1]：是否可写
     * @throws BusinessException 业务异常
     */
    public static Boolean[] getVdiCDROMDiskMapArr(CdRomMappingEnum cdRomMappingType) throws BusinessException {
        Assert.notNull(cdRomMappingType, "cdRomMappingType must not null");

        LOGGER.info("CDROM映射状态为：{}", cdRomMappingType);
        switch (cdRomMappingType) {
            case CLOSED:
                return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
            case READ_ONLY:
            default:
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
        }
    }
}
