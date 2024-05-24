package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.common;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuModelType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskTopAllowLoginTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Description: 云桌面策略工具类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-05-13
 *
 * @author linke
 */
public class DeskStrategyHelper {

    private static final String VGPU_SIZE_FORMAT = "%.2f";

    /**
     * 允许登录时间配置最大数量
     */
    private static final int ALLOW_LOGIN_TIME_MAX_NUM = 50;

    private static final int WEEK_MIN = 1;

    private static final int WEEK_MAX = 7;

    /**
     * 时间格式
     */
    private static final String TIME_PATTERN = "HH:mm:ss";

    /** 计算机名称只能包含[a-z]、[A-Z]、[0-9]或-，必须以字母开头，长度不超过8个字符 */
    private static final Pattern COMPUTER_NAME_PATTERN = Pattern.compile("^[a-zA-Z]([0-9a-zA-Z-]{0,7})$");

    private static final double ZONE = 0.0;
    
    /**
     * 格式化显存大小格式
     *
     * @param vgpuType vgpuType
     * @param vgpuExtra vgpuExtra
     * @return 显存大小
     */
    public static Long getGraphicsMemorySize(VgpuType vgpuType, String vgpuExtra) {
        Assert.notNull(vgpuType, "vgpuType must not be null");
        Assert.notNull(vgpuExtra, "vgpuExtra must not be null");

        if (StringUtils.isEmpty(vgpuExtra)) {
            return 0L;
        }
        VgpuExtraInfoSupport vgpuExtraInfoSupport = VgpuUtil.deserializeVgpuExtraInfoByType(vgpuType, vgpuExtra);
        if (!(vgpuExtraInfoSupport instanceof VgpuExtraInfo)) {
            return 0L;
        }
        VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) vgpuExtraInfoSupport;
        return vgpuExtraInfo.getGraphicsMemorySize() == null ? 0L : vgpuExtraInfo.getGraphicsMemorySize();
    }

    /**
     * 反序列化 VgpuExtraInfo
     *
     * @param vgpuType vgpuType
     * @param vgpuExtra vgpuExtra
     * @return VgpuExtraInfo
     */
    public static VgpuExtraInfo convert2VgpuExtraInfo(VgpuType vgpuType, String vgpuExtra) {
        Assert.notNull(vgpuType, "vgpuType must not be null");
        Assert.notNull(vgpuExtra, "vgpuExtra must not be null");
        VgpuExtraInfo vgpuExtraInfo = new VgpuExtraInfo();
        if (StringUtils.isEmpty(vgpuExtra) || vgpuType == VgpuType.QXL) {
            return vgpuExtraInfo;
        }
        VgpuExtraInfoSupport vgpuExtraInfoSupport = VgpuUtil.deserializeVgpuExtraInfoByType(vgpuType, vgpuExtra);
        if (vgpuExtraInfoSupport instanceof VgpuExtraInfo) {
            return (VgpuExtraInfo) vgpuExtraInfoSupport;
        }
        vgpuExtraInfo.setGraphicsMemorySize(0L);
        vgpuExtraInfo.setVgpuModelType(VgpuModelType.G);
        vgpuExtraInfo.setModel("");
        return vgpuExtraInfo;
    }

    /**
     * 格式化显存显示的字符串
     *
     * @param gpuSize vgpuType
     * @return 显存显示的字符串
     */
    public static String formatGpuSize(Long gpuSize) {
        Assert.notNull(gpuSize, "gpuSize must not be null");
        if (gpuSize <= 0) {
            return "";
        }
        double gbSize = CapacityUnitUtils.bit2Gb(gpuSize);
        return String.format(VGPU_SIZE_FORMAT, gbSize);
    }

    /**
     * 验证时间
     * @param desktopAllowLoginTimeArr 云桌面允许登录时间
     * @throws BusinessException 业务异常
     */
    public static void verifyDesktopAllowLoginTime(DeskTopAllowLoginTimeDTO[] desktopAllowLoginTimeArr) throws BusinessException {
        Assert.notNull(desktopAllowLoginTimeArr, "desktopAllowLoginTimeArr must not be null");
        if (desktopAllowLoginTimeArr.length == 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_TIME_NOT_NULL);
        }
        if (desktopAllowLoginTimeArr.length > ALLOW_LOGIN_TIME_MAX_NUM) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_TIME_OVER_LIMIT_NUM);
        }
        for (DeskTopAllowLoginTimeDTO deskTopAllowLoginTimeDTO : desktopAllowLoginTimeArr) {
            verifyTime(deskTopAllowLoginTimeDTO);
        }

    }

    private static void verifyTime(DeskTopAllowLoginTimeDTO deskTopAllowLoginTimeDTO) throws BusinessException {
        Integer[] weekArr = deskTopAllowLoginTimeDTO.getWeekArr();
        String startTime = deskTopAllowLoginTimeDTO.getStartTime();
        String endTime = deskTopAllowLoginTimeDTO.getEndTime();
        if (weekArr == null || weekArr.length == 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_WEEK_NOT_NULL);
        }
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_LOGIN_START_TIME_OR_END_TIME_NOT_NULL);
        }
        LocalTime start;
        LocalTime end;
        try {
            start = localTimeParse(startTime);
            end = localTimeParse(endTime);
        } catch (Exception ex) {
            throw new BusinessException(BusinessKey.RCDC_RCO_TIME_FORMAT_ERROR, ex);
        }
        //结束时间小于开始时间
        if (end.compareTo(start) <= 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_START_TIME_LATER_THAN_END_TIME);
        }
        for (Integer week : weekArr) {
            if (week < WEEK_MIN || week > WEEK_MAX) {
                throw new BusinessException(BusinessKey.RCDC_RCO_TIME_FORMAT_ERROR);
            }
        }
    }

    /**
     * 校验VDI云桌面策略内存
     * @param memory 内存值
     * @throws BusinessException 业务异常
     */
    public static void validateVDIMemory(Double memory) throws BusinessException {
        Assert.notNull(memory, "memory can not be null");
        // 内存值的范围
        if (memory < 1 || memory > 256) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_INVALID_VDI_MEM_VALUE);
        }
        double remainder = memory % 0.5;
        if (remainder != ZONE) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_INVALID_VDI_MEM_VALUE_LIMIT);
        }
    }

    /**
     * 校验计算机名称前缀
     * @param computerName 计算机名称前缀
     * @throws BusinessException 业务异常
     */
    public static void computerNameValueValidate(String computerName) throws BusinessException {
        Assert.notNull(computerName, "name can not be null");
        if (computerName.length() > 0 && !COMPUTER_NAME_PATTERN.matcher(computerName).matches()) {
            throw new BusinessException(BusinessKey.RCO_COMPUTER_NAME_INVALID_COMPUTER_NAME_PREFIX);
        }
    }

    /**
     * 本地盘和云桌面重定向功能必须同时开启或关闭校验
     * @param enableAllowLocalDisk 本地盘是否开启
     * @param enableOpenDesktopRedirect 桌面重定向是否开启
     * @param name 云桌面策略名称
     * @throws BusinessException 业务异常
     */
    public static void enableAllowLocalDiskValueValidate(@Nullable Boolean enableAllowLocalDisk, 
                                                         @Nullable Boolean enableOpenDesktopRedirect, String name) throws BusinessException {
        Assert.notNull(name, "name can not be null");
        if (Boolean.FALSE.equals(enableAllowLocalDisk) && Boolean.TRUE.equals(enableOpenDesktopRedirect)) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_STRATEGY_NOT_LEGAL, name);
        }
    }

    /**
     * TCI云桌面策略不支持APP_LAYER
     * @param desktopType 云桌面策略类型
     * @throws BusinessException 业务异常
     */
    public static void voiValidateCloudDeskPattern(CbbCloudDeskPattern desktopType) throws BusinessException {
        Assert.notNull(desktopType, "desktopType can not be null");
        if (desktopType == CbbCloudDeskPattern.PERSONAL || desktopType == CbbCloudDeskPattern.RECOVERABLE) {
            return;
        }
        throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_CREATE_VOI_PATTERN_ERR, desktopType.name());
    }

    private static LocalTime localTimeParse(String localTimeStr) {
        Assert.hasText(localTimeStr, "localTimeStr must has text");
        return LocalTime.parse(localTimeStr, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }
}
