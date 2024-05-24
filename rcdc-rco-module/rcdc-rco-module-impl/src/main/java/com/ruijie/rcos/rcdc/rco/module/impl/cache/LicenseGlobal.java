package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import java.util.Set;
import com.google.common.collect.Sets;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/23
 *
 * @author nt
 */
public class LicenseGlobal {

    private LicenseGlobal() {
        throw new IllegalStateException("LicenseGlobal Utility class");
    }

    /** 临时授权，表示云桌面运行数不限制 */
    public static final Integer LICENSE_NUM_TEMP_NO_LIMIT = -1;

    /** 未授权或无可用的授权 */
    public static final Integer LICENSE_NUM_NO_AVALIABLE_AUTH = 0;

    /** rcdc授权数 */
    private static int rcdcLicenseNum = LICENSE_NUM_NO_AVALIABLE_AUTH;

    /** rcdc临时授权剩余时长 */
    private static long rcdcTrialRemainder = 0;

    /** windows正版授权数 */
    private static int windowsLicenseNum = LICENSE_NUM_NO_AVALIABLE_AUTH;

    /** windows正版授权激活数 */
    private static int windowsActiveNum = LICENSE_NUM_NO_AVALIABLE_AUTH;
    
    private static boolean isOpenKms = false;

    private static Set<String> windowsLicenseOsProTypeSet = Sets.newConcurrentHashSet();

    public static int getRcdcLicenseNum() {
        return rcdcLicenseNum;
    }

    public static void setRcdcLicenseNum(int rcdcLicenseNum) {
        LicenseGlobal.rcdcLicenseNum = rcdcLicenseNum;
    }

    public static long getRcdcTrialRemainder() {
        return rcdcTrialRemainder;
    }

    public static void setRcdcTrialRemainder(long rcdcTrialRemainder) {
        LicenseGlobal.rcdcTrialRemainder = rcdcTrialRemainder;
    }

    public static int getWindowsLicenseNum() {
        return windowsLicenseNum;
    }

    public static void setWindowsLicenseNum(int windowsLicenseNum) {
        LicenseGlobal.windowsLicenseNum = windowsLicenseNum;
    }

    /**
     * 增加授权
     */
    public static void activeWindowsActiveNum() {
        windowsActiveNum++;
    }
    
    public static int getWindowsActiveNum() {
        return windowsActiveNum;
    }

    public static void setWindowsActiveNum(int windowsActiveNum) {
        LicenseGlobal.windowsActiveNum = windowsActiveNum;
    }

    public static boolean isIsOpenKms() {
        return isOpenKms;
    }

    public static void setIsOpenKms(boolean isOpenKms) {
        LicenseGlobal.isOpenKms = isOpenKms;
    }

    public static Set<String> getWindowsLicenseOsProTypeSet() {
        return windowsLicenseOsProTypeSet;
    }
}
