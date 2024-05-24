package com.ruijie.rcos.rcdc.rco.module.web;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月25日
 *
 * @author luojianmo
 */
public interface PublicBusinessKey {

    String RCDC_RCO_PUBLIC_PAGE_SORT_FIELD_VALIDATION_FAIL = "23200897";

    String RCDC_TIME_PATTERN_ILLEGAL = "23200898";

    String RCDC_RCO_CLUSTER_NOT_NULL_ERROR = "23200899";

    String RCDC_RCO_STORAGE_POOL_NOT_NULL_ERROR = "23200900";


    /**
     * 路径必须以盘符开头，如C:\。
     */
    String RCDC_WINDOWS_PATH_DRIVE_LETTER_ERROR = "23200901";


    /**
     * 文件或文件夹名称不能以空格开头或结尾，不能以点号结尾。
     */
    String RCDC_WINDOWS_PATH_CANNOT_STARTSWITH_OR_ENDSWITH_CHARACTERS = "23200903";

    /**
     * 文件或文件夹名称中不能包含以下字符：/ \ : * ? " < > |。
     */
    String RCDC_WINDOWS_PATH_CANNOT_CONTAIN_FOLLOWING_CHARACTERS = "23200902";

    /**
     * 文件或文件夹名称长度不能超过128个字符。
     */
    String RCDC_WINDOWS_PATH_CANNOT_EXCEED_CHARACTERS = "23200904";

    /**
     * 文件或文件夹名称不能是以下保留字符：CON、PRN、AUX、NUL、以及COM0~COM9和LPT0~LPT9。
     */
    String RCDC_WINDOWS_PATH_CANNOT_FOLLOWING_RESERVED_CHARACTERS = "23200905";


    /**
     * 路径必须以盘符开头，如C:\。
     */
    String RCDC_LINUX_PATH_DRIVE_LETTER_ERROR = "23200906";


    /**
     * 文件名不能以空格开头或结尾，不能以点号结尾。
     */
    String RCDC_LINUX_PATH_CANNOT_STARTSWITH_OR_ENDSWITH_CHARACTERS = "23200908";

    /**
     * 路径中不能包含以下字符：/ : * ? " < > |。
     */
    String RCDC_LINUX_PATH_CANNOT_CONTAIN_FOLLOWING_CHARACTERS = "23200907";

    /**
     * 文件夹名称长度不能超过128个字符。
     */
    String RCDC_LINUX_PATH_CANNOT_EXCEED_CHARACTERS = "23200910";

    /**
     * 文件夹名不能只含以下保留字符：CON、PRN、AUX、NUL、COM1-9、LPT1-9.
     */
    String RCDC_LINUX_PATH_CANNOT_STARTSWITH = "23200909";


}
