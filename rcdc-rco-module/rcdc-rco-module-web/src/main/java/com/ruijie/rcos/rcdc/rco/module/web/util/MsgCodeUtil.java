package com.ruijie.rcos.rcdc.rco.module.web.util;

import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 批处理提示工具类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14 11:22
 *
 * @author yxq
 */
public class MsgCodeUtil {

    /**
     * key为提示语，value为返回给前端的码
     */
    private static final Map<String, String> MAP = new HashMap<>();

    static {
        MAP.put(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_WAKE_UP_TASK_NAME), Constants.SHOW_WAKE_UP_TI_CODE);
    }

    /**
     * 根据批处理的任务名，获取展示的CODE
     *
     * @param msgName 批处理的任务名
     * @return code
     */
    public static String getMsgCode(@Nullable String msgName) {
        return MAP.get(msgName);
    }
}
