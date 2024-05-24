package com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 * 
 * @author wjp
 */
public interface ServerModelBusinessKey {

    String RCO_GET_SERVER_MODEL_OR_CMS_CMSCOMPONENT_FAIL = "rco_get_server_model_or_cms_cmscomponent_fail";

    String RCO_OBTAIN_LICENSE_INFO_FAIL = "rco_obtain_license_info_fail";
    /* 获取教育版证书失败*/
    String RCO_OBTAIN_EDULICENSE_INFO_FAIL = "rco_obtain_edulicense_info_fail";
    /** 镜像类型信息异常 */
    String RCO_IMAGE_TEMPLATE_VALID_IMAGETYPE_ERROR = "23201017";
    /** 镜像操作类型信息异常 */
    String RCO_IMAGE_TEMPLATE_VALID_OSTYPE_ERROR = "23201018";
    /** 镜像CPU信息异常 */
    String RCO_IMAGE_TEMPLATE_VALID_CPU_ERROR = "23201019";
    /** 镜像内存信息异常 */
    String RCO_IMAGE_TEMPLATE_VALID_MEMORY_ERROR = "23201020";
    /** 终端类型信息异常 */
    String RCO_TERMINAL_MODEL_VALID_PLATFORM_ERROR = "rco_terminal_model_valid_platform_error";
    /** 镜像系统盘信息异常 */
    String RCO_IMAGE_TEMPLATE_VALID_SYSTEM_SIZE_ERROR = "23201021";
    /** 镜像数据盘大小异常 */
    String RCO_IMAGE_TEMPLATE_VALID_DATA_SIZE_ERROR = "23201022";

    String RCO_IMAGE_TEMPLATE_VALID_DATA_COUNT_ERROR = "23201023";

    String RCO_IMAGE_TEMPLATE_VALID_DATA_DISK_SYMBOL_ERROR = "23201024";
    String RCO_IMAGE_TEMPLATE_VALID_COMPUTER_NAME_ERROR = "23201138";
    String RCO_IMAGE_TEMPLATE_VALID_MULTIPLE_VERSION_ERROR = "23201199";

    /** 镜像类型转换失败，镜像未就绪*/
    String RCDC_IMAGE_USAGE_TRANSFER_FAIL_BY_IMAGE_NOT_STEADY =
            "rcdc_image_usage_transfer_fail_by_image_not_steady";
}
