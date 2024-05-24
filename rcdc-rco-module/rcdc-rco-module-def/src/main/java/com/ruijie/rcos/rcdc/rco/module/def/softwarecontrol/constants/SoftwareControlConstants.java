package com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月11日
 *
 * @author lihengjing
 */
public interface SoftwareControlConstants {

    /** 软件分组名称（必填） */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_GROUP_NAME = "rcdc_rco_export_columns_software_group_name";

    /** 软件分组类型（必填）*/
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_GROUP_TYPE = "rcdc_rco_export_columns_software_group_type";

    /** 软件分组描述(非必填）*/
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_GROUP_DESC = "rcdc_rco_export_columns_software_group_desc";

    /** 软件名称（必填）*/
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_NAME = "rcdc_rco_export_columns_software_name";

    /** 软件描述(非必填） */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_DESC = "rcdc_rco_export_columns_software_desc";

    /** 厂商数字签名(非必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_DIGITAL_SIGN = "rcdc_rco_export_columns_software_digital_sign";

    /** 厂商数字签名标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_DIGITAL_SIGN_FLAG = "rcdc_rco_export_columns_software_digital_sign_flag";

    /** 产品名称(非必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PRODUCT_NAME = "rcdc_rco_export_columns_software_product_name";

    /** 产品名称标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PRODUCT_NAME_FLAG = "rcdc_rco_export_columns_software_product_name_flag";

    /** 进程名(非必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PROCESS_NAME = "rcdc_rco_export_columns_software_process_name";

    /** 进程名标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_PROCESS_NAME_FLAG = "rcdc_rco_export_columns_software_process_name_flag";

    /** 原始文件名(非必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_ORIGINAL_FILE_NAME = "rcdc_rco_export_columns_software_original_file_name";

    /** 原始文件名标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_ORIGINAL_FILE_NAME_FLAG = "rcdc_rco_export_columns_software_original_file_name_flag";

    /** 文件特征码(非必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_CUSTOM_MD5 = "rcdc_rco_export_columns_software_file_custom_md5";

    /** 文件特征码标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_CUSTOM_MD5_FLAG = "rcdc_rco_export_columns_software_file_custom_md5_flag";

    /** 是否为文件夹(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_IS_DIRECTORY = "rcdc_rco_export_columns_software_file_is_directory";

    /** 所在文件夹标识(非必填，绿色软件时需填写) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_PARENT_ID = "rcdc_rco_export_columns_software_file_parent_id";

    /** 文件夹标识(非必填，绿色软件时需填写) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_FILE_SOFTWARE_ID = "rcdc_rco_export_columns_software_file_software_id";

    /** 黑名单厂商数字签名标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_DIGITAL_SIGN_FLAG = "rcdc_rco_export_columns_software_black_digital_sign_flag";

    /** 黑名单产品名称标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_PRODUCT_NAME_FLAG = "rcdc_rco_export_columns_software_black_product_name_flag";

    /** 黑名单进程名标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_PROCESS_NAME_FLAG = "rcdc_rco_export_columns_software_black_process_name_flag";

    /** 黑名单原始文件名标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_ORIGINAL_FILE_NAME_FLAG = "rcdc_rco_export_columns_software_black_original_file_name_flag";

    /** 黑名单文件特征码标志(必填) */
    String RCDC_RCO_EXPORT_COLUMNS_SOFTWARE_BLACK_FILE_CUSTOM_MD5_FLAG = "rcdc_rco_export_columns_software_black_file_custom_md5_flag";


    /** 软件信息属性 为空时 导出空字符 */
    String SOFTWARE_INFO_FIELD_EXPORT_NULL = "";

    /** 软件信息属性名以Flag结尾 */
    String SOFTWARE_INFO_FIELD_END_STRING = "Flag";

    /** 软件信息属性名get方法开头 */
    String SOFTWARE_INFO_FIELD_GET_METHOD = "get";

    /** 软件导入 软件信息标志 选项是 */
    String SOFTWARE_IMPORT_FLAG_TRUE = "是";

    /** 软件导入 软件信息标志 选项否 */
    String SOFTWARE_IMPORT_FLAG_FALSE = "否";

    /** 软件导入 软件类型 选项自定义软件 */
    String SOFTWARE_IMPORT_SOFTWARE_TYPE_CUSTOM = "自定义软件";

    /** 软件导入 软件类型 选项内置软件 */
    String SOFTWARE_IMPORT_SOFTWARE_TYPE_DEFAULT = "内置软件";

    /** 软件导入、导出时 软件分组软件数量为空时 软件专用名称占位 */
    String SOFTWARE_IMPORT_SOFTWARE_NULL_NAME = "###空分组专用软件名称###";

    /** 软件组默认组 未分组*/
    String DEFAULT_SOFTWARE_GROUP_NAME = "未分组";

}
