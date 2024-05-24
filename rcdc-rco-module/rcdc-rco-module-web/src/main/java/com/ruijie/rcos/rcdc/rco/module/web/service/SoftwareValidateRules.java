package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 导入的用户数据校验规则
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */
public class SoftwareValidateRules {

    /* 厂商数字签名(长度与GT保持一致) */
    public static final int DIGITAL_SIGN_SIZE = 260;

    /* 产品名称(长度与GT保持一致) */
    public static final int PRODUCT_NAME_SIZE = 260;

    /* 进程名(长度与GT保持一致) */
    public static final int PROCESS_NAME_SIZE = 260;

    /* 原始文件名(长度与GT保持一致) */
    public static final int ORIGINAL_FILE_NAME_SIZE = 260;

    /* 文件自定义md5信息(长度与GT保持一致) */
    public static final int FILE_CUSTOM_MD5_SIZE = 32;

    public static final int INSTALL_PATH_SIZE = 260;

    private SoftwareValidateRules() {
        throw new IllegalStateException("SoftwareValidateRules Utility class");
    }

    /* 软件名称 */
    public static final int SOFTWARE_NAME_SIZE = 64;

    /*软件描述 */
    public static final int SOFTWARE_DESC_SIZE = 128;

    public static final int SOFTWARE_GROUP_NAME_SIZE = 64;

    public static final int SOFTWARE_GROUP_DESC_SIZE = 128;

    /**
     * 最多允许数据行
     */
    public static final int ALLOW_MAX_ROW = 1000;


    public static final List<String> DEFAULT_FLAGS = new ArrayList<>();

    public static final List<String> DEFAULT_SOFTWARE_GROUP_TYPE = new ArrayList<>();

    static {
        DEFAULT_FLAGS.add(SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE);
        DEFAULT_FLAGS.add(SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_FALSE);
        DEFAULT_SOFTWARE_GROUP_TYPE.add(SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_TYPE_DEFAULT);
        DEFAULT_SOFTWARE_GROUP_TYPE.add(SoftwareControlConstants.SOFTWARE_IMPORT_SOFTWARE_TYPE_CUSTOM);
    }
}
