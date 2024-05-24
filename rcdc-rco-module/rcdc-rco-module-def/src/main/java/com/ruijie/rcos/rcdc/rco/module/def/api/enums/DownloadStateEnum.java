package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.Map;

/**
 * Description: 下载状态枚举类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11 9:53
 *
 * @author yxq
 */
public enum DownloadStateEnum {
    /**
     * 完成
     */
    SUCCESS,
    /**
     * 未绑定
     */
    NONE,
    /**
     * 下载中
     */
    START,
    /**
     * 无法获取
     */
    CAN_NOT_OBTAIN,
    /**
     * 下载失败
     */
    FAIL;

    private static Integer DRIVER_NO_INSTALL = 3;

    private static Integer DOWNLOAD_CANCEL = -7;

    private static Integer TERMINAL_OFFLINE = 104;

    private static Integer OTHER_ERROR = 99;

    private static Integer GT_NO_LATER = -9;


    /**
     * 错误码映射
     */
    private static Map<Integer, String> DOWNLOAD_PROMPT_MSG_MAP = new HashedMap() {
        {
            put(DRIVER_NO_INSTALL, "23200027");
            put(DOWNLOAD_CANCEL, "23200028");
            put(TERMINAL_OFFLINE, "23200029");
            put(OTHER_ERROR, "23200030");
            put(GT_NO_LATER, "23200031");
        }
    };

    /**
     * 根据错误码，获取对应的提示语
     *
     * @param failCode 错误码
     * @return 提示语
     */
    public static String getDownloadPromptMessage(@Nullable Integer failCode) {
        if (failCode == null || !DOWNLOAD_PROMPT_MSG_MAP.containsKey(failCode)) {
            // 没有错误码时，不需要返回提示语
            return null;
        }

        return LocaleI18nResolver.resolve(DOWNLOAD_PROMPT_MSG_MAP.get(failCode));
    }
}
