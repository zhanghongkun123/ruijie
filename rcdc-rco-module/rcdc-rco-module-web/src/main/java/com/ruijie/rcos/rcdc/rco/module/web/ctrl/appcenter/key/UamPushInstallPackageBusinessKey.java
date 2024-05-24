package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/16 15:31
 *
 * @author coderLee23
 */
public interface UamPushInstallPackageBusinessKey {

    /**
     * 非待发布或发布状态，不允许编辑
     */
    String RCDC_UAM_EDIT_STATUS_ERROR = "23201072";


    /**
     * 编辑推送安装包[{0}]成功
     */
    String RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_SUCCESS_LOG = "rcdc_uam_edit_push_install_package_success_log";


    /**
     * 编辑推送安装包[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_FAIL_LOG = "rcdc_uam_edit_push_install_package_fail_log";

    /**
     * 编辑推送安装包失败，失败原因：{0}
     */
    String RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG = "rcdc_uam_edit_push_install_package_not_exists_log";



    /**
     * 创建推送安装包[{0}]成功
     */
    String RCDC_UAM_CREATE_PUSH_INSTALL_PACKAGE_SUCCESS_LOG = "rcdc_uam_create_push_install_package_success_log";

    /**
     * 创建推送安装包[{0}]失败，失败原因：{0}
     */
    String RCDC_UAM_CREATE_PUSH_INSTALL_PACKAGE_FAIL_LOG = "rcdc_uam_create_push_install_package_fail_log";


    /**
     * 放弃编辑推送安装包[{0}]成功
     */
    String RCDC_UAM_GIVE_UP_PUSH_INSTALL_PACKAGE_SUCCESS_LOG = "rcdc_uam_give_up_push_install_package_success_log";


    /**
     * 放弃编辑推送安装包[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_GIVE_UP_PUSH_INSTALL_PACKAGE_FAIL_LOG = "rcdc_uam_give_up_push_install_package_fail_log";

    /**
     * 放弃编辑推送安装包失败，失败原因：{0}
     */
    String RCDC_UAM_GIVE_UP_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG = "rcdc_uam_give_up_push_install_package_not_exists_log";

    /**
     * 发布推送安装包[{0}]成功
     */
    String RCDC_UAM_PUBLISH_PUSH_INSTALL_PACKAGE_SUCCESS_LOG = "rcdc_uam_publish_push_install_package_success_log";


    /**
     * 发布推送安装包[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_PUBLISH_PUSH_INSTALL_PACKAGE_FAIL_LOG = "rcdc_uam_publish_push_install_package_fail_log";

    /**
     * 发布推送安装包失败，失败原因：{0}
     */
    String RCDC_UAM_PUBLISH_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG = "rcdc_uam_publish_push_install_package_not_exists_log";

    /**
     * 推送安装包[{0}]的状态非制种失败，无需进行重制种子
     */
    String RCDC_UAM_PUSH_INSTALL_PACKAGE_NOT_NEED_REMAKE_SEED = "23201087";


    /**
     * 推送安装包发起重新制作种子文件[{0}]成功
     */
    String RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_SUCCESS_LOG = "rcdc_uam_remake_seed_push_install_package_success_log";


    /**
     * 推送安装包重新制作种子文件[{0}]失败，失败原因：{1}
     */
    String RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_FAIL_LOG = "rcdc_uam_remake_seed_push_install_package_fail_log";

    /**
     * 推送安装包重新制作种子文件失败，失败原因：{0}
     */
    String RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG = "rcdc_uam_remake_seed_push_install_package_not_exists_log";

    /**
     * 推送安装包[{0}]已绑定交付组，不支持修改操作系统类型
     */
    String RCDC_UAM_INSTALL_PACKAGE_EXIST_USED = "23201151";
}
