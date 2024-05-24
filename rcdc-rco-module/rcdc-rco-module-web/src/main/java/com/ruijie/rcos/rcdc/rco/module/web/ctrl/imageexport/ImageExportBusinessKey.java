package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/13 13:58
 *
 * @author ketb
 */
public interface ImageExportBusinessKey {

    // 导出和下载
    /**删除导出镜像任务名*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_DELETE_TASK_NAME =
            "rcdc_clouddesktop_exportimage_batch_delete_task_name";
    /**删除导出镜像文件成功*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_TASK_SUCCESS = "rcdc_clouddesktop_exportimage_delete_task_success";
    /**删除导出镜像文件失败*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_TASK_FAIL = "rcdc_clouddesktop_exportimage_delete_task_fail";
    /**删除导出镜像文件失败*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_TASK_FAIL_WITH_NAME = "rcdc_clouddesktop_exportimage_delete_task_fail_with_name";
    /**删除导出镜像文件成功日志*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_SUCCESS_LOG = "rcdc_clouddesktop_exportimage_delete_success_log";
    /**删除导出镜像文件失败日志*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_FAIL_LOG = "23201119";
    /**删除导出镜像文件失败日志*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_FAIL_BY_FILE_NOT_EXIST =
            "23201118";
    /**删除导出镜像文件任务失败描述*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_ITEM_FAIL_DESC = "rcdc_clouddesktop_exportimage_delete_item_fail_desc";
    /**删除导出镜像文件任务失败描述*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_ITEM_FAIL_DESC_WITH_NAME =
            "rcdc_clouddesktop_exportimage_delete_item_fail_desc_with_name";
    /**批量删除导出镜像文件任务描述*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_DELETE_TASK_DESC =
            "rcdc_clouddesktop_exportimage_batch_delete_task_desc";
    /**删除导出镜像文件结果*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DELETE_RESULT = "rcdc_clouddesktop_exportimage_delete_result";
    /**导出镜像任务名*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_TASK_NAME =
            "rcdc_clouddesktop_exportimage_batch_task_name";
    /**批量导出镜像文件任务描述*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_TASK_DESC =
            "rcdc_clouddesktop_exportimage_batch_task_desc";
    /**导出镜像失败，同时导出数量超出限制*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_FAIL_BY_EXPORTING_LIMIT = "23200892";
    /**导出镜像失败，镜像不存在*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_FAIL_BY_IMAGE_NOT_EXIST = "23201120";
    /**导出镜像失败，镜像未就绪*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_FAIL_BY_IMAGE_NOT_STEADY =
            "23201121";
    String RCDC_CLOUDDESKTOP_EXPORT_VERSION_FAIL_BY_IMAGE_NOT_STEADY = "23201122";
    /**删除导出镜像文件成功日志*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_SUCCESS_LOG = "rcdc_clouddesktop_exportimage_success_log";

    String RCDC_CLOUDDESKTOP_EXPORT_VERSION_SUCCESS_LOG = "rcdc_clouddesktop_export_version_success_log";
    /**导出镜像描述*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DESCRIPTION = "rcdc_clouddesktop_exportimage_description";
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_ITEM_FAIL_DESC_WITH_NAME = "rcdc_clouddesktop_exportimage_item_fail_desc_with_name";
    String RCDC_CLOUDDESKTOP_EXPORT_VERSION_ITEM_FAIL_DESC_WITH_NAME = "rcdc_clouddesktop_export_version_item_fail_desc_with_name";
    /**导出镜像文件结果*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_RESULT = "rcdc_clouddesktop_exportimage_result";
    /**导出镜像文件失败*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_TASK_FAIL = "rcdc_clouddesktop_exportimage_task_fail";

    String RCDC_CLOUDDESKTOP_EXPORT_VERSION_TASK_FAIL = "rcdc_clouddesktop_export_version_task_fail";
    /**导出镜像文件成功*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_TASK_SUCCESS = "rcdc_clouddesktop_exportimage_task_success";

    String RCDC_CLOUDDESKTOP_EXPORT_VERSION_TASK_SUCCESS = "rcdc_clouddesktop_export_version_task_success";
    /**下载失败，镜像文件不存在*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_EXIST = "23200893";
    /**下载失败，镜像文件未就绪*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_STEADY =
            "23200894";

    /**校验失败，镜像文件未就绪*/
    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_CHECK_FAIL_BY_NOT_EXIST =
            "23200895";

    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_DATA_PART = "rcdc_clouddesktop_exportimage_data_part";

    String RCDC_CLOUDDESKTOP_EXPORTIMAGE_SYSTEM_PART = "rcdc_clouddesktop_exportimage_system_part";
}
