package com.ruijie.rcos.rcdc.rco.module.def.api.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;

/**
 * Description: 所有菜单 目前只是放些菜单的枚举 提供引用 还有提供不支持菜单列表
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月12日
 *
 * @author xiejian
 */
public enum MenuType {
    /** 如果父亲节点选中，则底下所有节点都选择 */
    // 首页
    DASHBOARD("dashboard", getAllServerModel()),
    // 云桌面管理
    CLOUD_DESKTOP_MANAGEMENT("cloudDesktopManagement", getAllServerModel()),
    // 云桌面管理
    CLOUD_DESKTOP_MANAGE("cloudDesktopManage", getAllServerModel()),
    // 云桌面管理 -查看
    CLOUD_DESKTOP_MANAGE_SELECT("cloudDesktopManageSelect", getAllServerModel()),
    // 回收站 -
    RECYCLE_BIN("recycleBin", getOnlyVdiServerModel()),
    // 回收站 -查看
    RECYCLE_BIN_SELECT("recycleBinSelect", getOnlyVdiServerModel()),
    // 镜像模板 -
    IMAGE_MANAGE("imageManage", getAllServerModel()),
    // 镜像模板 -
    IMAGE_TEMPLATE("imageTemplate", getAllServerModel()),
    // 镜像模板 -查看
    IMAGE_TEMPLATE_SELECT("imageTemplateSelect", getAllServerModel()),
    // 镜像文件
    IMAGE_FILES("imageFiles", getAllServerModel()),
    // 镜像文件 -查看
    IMAGE_FILES_SELECT("imageFilesSelect", getAllServerModel()),
    // 软件安装包
    INSTALLATION_PACKAGE("installationPackage", getAllServerModel()),
    // 驱动管理
    IMAGE_DRIVER_MANAGEMENT("imageDriverManagement", getAllServerModel()),

    // 快照管理
    LIST_SNAPSHOT("listSnapshot", getAllServerModel()),
    // 快照管理 - 查看
    SNAPSHOT_QUERY("snapshotQuery", getAllServerModel()),


    // 软件安装包 -查看
    INSTALLATION_PACKAGE_SELECT("installationPackageSelect", getAllServerModel()),
    // 用户管理
    USER_MANAGEMENT("userManagement", getAllServerModel()),
    // 用户管理
    USER_MANAGE("userManage", getAllServerModel()),
    // 用户管理 - 查看
    USER_MANAGE_SELECT("userManageSelect", getAllServerModel()),
    // AD
    AD_DOMAIN("adDomain", getAllServerModel()),
    // LDAP
    LDAP_DOMAIN("ldap", getAllServerModel()),
    // 第三方认证
    THIRD_PARTY_AUTH("thirdPartyAuth", getOnlyVdiServerModel()),
    THIRD_PARTY_AUTH_EDIT("thirdPartyConfigEdit", getOnlyVdiServerModel()),
    THIRD_PARTY_AUTH_TEST("thirdPartyConfigTest", getOnlyVdiServerModel()),
    THIRD_PARTY_AUTH_SYNC("syncThirdPartyUser", getOnlyVdiServerModel()),

    // 虚拟应用
    APP_MANAGEMENT("appManagement", getAllServerModel()),
    // 应用主机管理
    RCA_CLOUD_DESKTOP_MANAGE("rcaCloudDesktopManage", getAllServerModel()),
    // 应用池管理
    APP_POOL_MANAGE("appPoolManage", getAllServerModel()),
    // 应用会话管理
    RCA_SESSION_MANAGE("rcaSessionManage", getAllServerModel()),

    // 终端管理
    TERMINAL_MANAGE("terminalManage", getAllServerModel()),
    // VDI终端
    VDI_TERMINAL("vdiTerminal", getOnlyVdiServerModel()),
    // VDI终端 - 查看
    VDI_TERMINAL_SELECT("vdiTerminalSelect", getOnlyVdiServerModel()),
    // VDI终端组
    VDI_TERMINAL_GROUP("vdiTerminalGroup", getOnlyVdiServerModel()),
    // VDI终端组 - 查看
    VDI_TERMINAL_GROUP_SELECT("vdiTerminalGroupSelect", getOnlyVdiServerModel()),
    // IDV终端
    IDV_TERMINAL("idvTerminal", getAllServerModel()),
    // IDV终端- 查看
    IDV_TERMINAL_SELECT("idvTerminalSelect", getAllServerModel()),
    // IDV终端组
    IDV_TERMINAL_GROUP("idvTerminalGroup", getAllServerModel()),
    // IDV终端组- 查看
    IDV_TERMINAL_GROUP_SELECT("idvTerminalGroupSelect", getAllServerModel()),
    // 软终端
    SOFT_TERMINAL("softTerminal", getOnlyVdiServerModel()),
    // 软终端 - 查看
    SOFT_TERMINAL_SELECT("softTerminalSelect", getOnlyVdiServerModel()),
    // 软终端组
    SOFT_TERMINAL_GROUP("softTerminalGroup", getOnlyVdiServerModel()),
    // 软终端组 - 查看
    SOFT_TERMINAL_GROUP_SELECT("softTerminalGroupSelect", getOnlyVdiServerModel()),
    // PC终端
    PC_TERMINAL("pcTerminal", getAllServerModel()),
    // PC终端- 查看
    PC_TERMINAL_SELECT("pcTerminalSelect", getAllServerModel()),
    // 应用客户端
    APP_TERMINAL("appTerminal", getAllServerModel()),
    // 应用客户端- 查看
    APP_TERMINAL_SELECT("appTerminalSelect", getAllServerModel()),
    // PC终端端组
    PC_TERMINAL_GROUP("pcTerminalGroup", getAllServerModel()),
    // PC终端组 - 查看
    PC_TERMINAL_GROUP_SELECT("pcTerminalGroupSelect", getAllServerModel()),
    // 终端配置管理
    TERMINAL_CONFIG("terminalConfig", getAllServerModel()),
    // 策略管理
    STRATEGY_MANAGE("strategyManage", getAllServerModel()),

    //临时权限
    DESKTOP_TEMP_PERMISSION("desktopTempPermission", getOnlyVdiServerModel()),
    //临时权限
    DESKTOP_TEMP_PERMISSION_SELECT("desktopTempPermissionSelect", getOnlyVdiServerModel()),

    // 云桌面策略管理
    CLOUD_DESKTOP_STRATEGY("cloudDesktopStrategy", getAllServerModel()),
    // 云桌面策略管理 - 查看
    CLOUD_DESKTOP_STRATEGY_SELECT("cloudDesktopStrategySelect", getAllServerModel()),
    // 网络策略
    NETWORK_STRATEGY("networkStrategy", getAllServerModel()),
    // 网络策略- 查看
    NETWORK_STRATEGY_SELECT("networkStrategySelect", getAllServerModel()),
    // 全局策略
    GLOBAL_STRATEGY("globalStrategy", getAllServerModel()),
    // USB策略
    USB_MANAGE("usbManage", getAllServerModel()),
    // USB策略 - 查看
    USB_MANAGE_SELECT("usbManageSelect", getAllServerModel()),

    // 云应用策略
    APP_STRATEGY("appStrategy", getAllServerModel()),
    // 云应用策略
    APP_USER_STRATEGY("appUserStrategy", getAllServerModel()),
    // 云应用策略 - 查看
    APP_USER_STRATEGY_SELECT("appUserStrategySelect", getAllServerModel()),

    // 云应用外设策略
    RCA_USB_STRATEGY("rcaUsbStrategy", getAllServerModel()),
    // 云应用外设策略 - 查看
    RCA_USB_STRATEGY_SELECT("rcaUsbStrategySelect", getAllServerModel()),

    // 打印机管理
    PRINTER_MANAGE("printerManage", getAllServerModel()),
    // 主题管理
    THEME_STRATEGY("themeStrategy", getAllServerModel()),
    // 软件管控管理
    SOFTWARE_CONTROL_MANAGE("softwareControlManage", getAllServerModel()),
    // 软件库管理
    SOFTWARE_MANAGE("softwareManage", getAllServerModel()),
    // 软件管控策略
    SOFTWARE_STRATEGY("softwareStrategy", getAllServerModel()),
    //软终端全局配置
    SOFT_TERMINAL_GLOBAL_CONFIG("softTerminalGlobalConfig", getAllServerModel()),

    // 系统设置
    SYSTEM_SETTING("systemSetting", getAllServerModel()),
//    SYSTEM_CONFIG("systemConfig", getAllServerModel()),
    // CMC设置
    CMS_MANAGE("cmsManage", getVdiAndIdvServerModel()),
    // UWS管理
    UWS_MANAGE("uwsManage", getAllServerModel()),
    // 系统管理员
    SYSTEM_USER("systemUser", getAllServerModel()),
    // 系统管理员
    SYSTEM_USER_MANAGE("systemUserManage", getAllServerModel()),
    // 系统运维
    SYSTEM_MAINTENANCE("systemMaintenance", getAllServerModel()),
    // 系统报表
    SYSTEM_DASHBOARD("systemDashboard", getOnlyVdiServerModel()),
    // 桌面池使用报表
    DESKTOP_USE_STATIC_REPORT("desktopUseStaticReport", getOnlyVdiServerModel()),
    // 授权报表
    DESKTOP_LICENSE_STAT("desktopLicenseStat", getOnlyVdiServerModel()),

    // 服务器数据备份
    SERVER_BACKUP_RECOVERY("serverBackupRecovery", getAllServerModel()),

    ALARM_MONITOR("alarmMonitor", getAllServerModel()),
    ALARM_LOG("alarmLog", getAllServerModel()),
    // 升级管理
    UPDATE_MANAGE("updateManage", getAllServerModel()),
    // 终端升级
    TERMINAL_UPGRADE("terminalUpgrade", getAllServerModel()),
    // 独立升级
    TERMINAL_UPGRADE_WITH_SOLO_COMP("terminalUpgradeWithSoloComp", getAllServerModel()),
    // 审计日志
    AUDIT_LOGS("auditLogs", getAllServerModel()),
    TIMED_TASK("timedTask", getAllServerModel()),
    MAINTENANCE_TOOLS("maintenanceTools", getAllServerModel()),
    // 5.4新增菜单
    // 系统配置 -- 系统管理员 -- 角色管理
    SYSTEM_ROLE_MANAGE("systemRoleManage", getAllServerModel()),
    // 系统运维 -- 客户信息
    CUSTOM_INFO("customInfo", getAllServerModel()),
    // 系统运维 -- 云平台配置
    PLATFORM_MANAGER("platformManage", getOnlyVdiServerModel()),


    // 功能按钮清单
    // 云桌面管理 -关机
    POWER_OFF("powerOff", getAllServerModel()),
    // 云桌面管理 -开机 (VDI特有)
    POWER_ON("powerOn", getOnlyVdiServerModel()),
    // 云桌面管理 -重启
    REBOOT("reboot", getOnlyVdiServerModel()),
    // 云桌面管理 -还原
    RESTORE("restore", getAllServerModel()),
    // 云桌面管理 -导出桌面信息
    EXPORT_CLOUD_DESKTOP_INFO("exportCloudDesktopInfo", getAllServerModel()),
    // 云桌面管理 -编辑云桌面策略
    EDIT_CLOUD_DESKTOP_STRATEGY("editVdiCloudDesktopStrategy", getAllServerModel()),
    // 云桌面管理 -编辑VDI网络策略 (VDI特有)
    EDIT_VDI_NETWORK_STRATEGY("editVdiNetworkStrategy", getOnlyVdiServerModel()),
    // 云桌面管理 -编辑云桌面规格 (VDI特有)
    ALONE_CONFIGURE_CLOUD_DESKTOP_POLICY("aloneConfigureCloudDesktopPolicy", getOnlyVdiServerModel()),
    // 云桌面管理 -故障恢复
    FAILURE_RECOVERY("failureRecovery", getOnlyVdiServerModel()),
    // 云桌面管理 -删除
    DELETE_CLOUD_DESKTOP("deleteCloudDesktop", getOnlyVdiServerModel()),
    // 云桌面管理 -取消报障
    CANCEL_REPORT("cancelReport", getAllServerModel()),
    // 云桌面管理 -远程协助
    REMOTE_ASSISTANCE("remoteAssistance", getAllServerModel()),
    // 云桌面管理 -强制关机
    FORCED_SHUTDOWN("forcedShutdown", getAllServerModel()),
    // 云桌面管理 -云桌面日志收集
    CLOUD_DESKTOP_LOG_COLLECT("cloudDesktopLogCollect", getAllServerModel()),
    // 云桌面管理 -重置MAC地址
    CREATE_NEW_MAC_ADDRESS("createNewMACAddress", getOnlyVdiServerModel()),
    // 云桌面管理 -编辑云桌面标签
    CLOUD_DESKTOP_TAG_EDIT("cloudDesktopTagEdit", getOnlyVdiServerModel()),
    // 云桌面管理 - 立即备份
    CLOUD_DESKTOP_BACK_UP("cloudDesktopBackUp", getOnlyVdiServerModel()),
    // 云桌面管理 - 云桌面备份编辑cloudDesktopBackUpEdit
    CLOUD_DESKTOP_BACK_UP_EDIT("cloudDesktopBackUpEdit", getOnlyVdiServerModel()),
    // 云桌面管理 - 云桌面备份恢复cloudDesktopBackUpRecovery
    CLOUD_DESKTOP_BACK_UP_RECOVERY("cloudDesktopBackUpRecovery", getOnlyVdiServerModel()),
    // 云桌面管理 - 云桌面备份删除 cloudDesktopBackUpDelete
    CLOUD_DESKTOP_BACK_UP_DELETE("cloudDesktopBackUpDelete", getOnlyVdiServerModel()),
    // 云桌面管理 - 云桌面备份取消 cloudDesktopBackUpCancel
    CLOUD_DESKTOP_BACK_UP_CANCEL("cloudDesktopBackUpCancel", getOnlyVdiServerModel()),
    // 云桌面管理 -变更镜像模板
    CHANGE_CLOUD_DESKTOP_IMAGE_TEMPLATE("changeCloudDesktopImageTemplate", getAllServerModel()),
    // 云桌面管理 -编辑计算机名称
    EDIT_COMPUTER_NAME("editComputerName", getAllServerModel()),
    // 云桌面管理 -快照制作
    SNAPSHOT_CREATE("snapshotCreate", getOnlyVdiServerModel()),
    // 云桌面管理 -快照编辑
    SNAPSHOT_EDIT("snapshotEdit", getOnlyVdiServerModel()),
    // 云桌面管理 -快照恢复
    SNAPSHOT_RECOVERY("snapshotRecovery", getOnlyVdiServerModel()),
    // 云桌面管理 -快照删除
    SNAPSHOT_DELETE("snapshotDelete", getOnlyVdiServerModel()),
    // 云桌面管理 -批量生效云桌面策略
    CLOUD_DESKTOP_STRATEGY_REFRESH("cloudDesktopStrategyRefresh", getOnlyVdiServerModel()),
    // 云桌面管理 -编辑VDI软件管控策略
    EDIT_SOFTWARE_STRATEGY("editSoftwareStrategy", getAllServerModel()),
    // 云桌面管理 - 维护模式
    CLOUD_DESKTOP_OPEN_MAINTENANCE("openCloudDesktopMaintenance", getOnlyVdiServerModel()),
    CLOUD_DESKTOP_CLOSE_MAINTENANCE("closeCloudDesktopMaintenance", getOnlyVdiServerModel()),
    // 云桌面管理 -重置windows密码
    RESET_DESKTOP_WINDOWS_PASSWORD("resetDesktopWinPwd", getAllServerModel()),

    // 回收站 -删除
    RECYCLE_BIN_DELETE("recycleBinDelete", getOnlyVdiServerModel()),
    // 回收站 -云桌面恢复
    RECYCLE_BIN_CLOUD_DESKTOP_RECOVERY("recycleBinCloudDesktopRecovery", getOnlyVdiServerModel()),
    // 回收站 -指定用户恢复云桌面
    SPECIFIED_USER_RESTORES_CLOUD_DESKTOP("specifiedUserRestoresCloudDesktop", getOnlyVdiServerModel()),
    // 回收站 -指定桌面池恢复云桌面
    SPECIFIED_DESKTOP_POOL_RESTORES_CLOUD_DESKTOP("specifiedDesktopPoolRestoresCloudDesktop", getOnlyVdiServerModel()),
    // 回收站 -清空回收站
    EMPTY_TRASH("emptyTrash", getOnlyVdiServerModel()),
    // 回收站 -设置自动清理
    SET_AUTOMATIC_CLEANUP("setAutomaticCleanup", getOnlyVdiServerModel()),


    // 镜像模板 -创建
    IMAGE_TEMPLATE_CREATE("imageTemplateCreate", getAllServerModel()),
    // 镜像模板 -下载黄金镜像
    DOWNLOAD_GOLDEN_IMAGE("downloadGoldenImage", getAllServerModel()),
    // 镜像模板 -复制模板
    IMAGE_TEMPLATE_COPY("imageTemplateCopy", getAllServerModel()),
    // 镜像模板 -删除
    IMAGE_TEMPLATE_DELETE("imageTemplateDelete", getAllServerModel()),
    // 镜像模板 -驱动安装
    IMAGE_TEMPLATE_DRIVER_INSTALL("imageTemplateDriverInstall", getAllServerModel()),
    // 镜像模板 -服务器上编辑镜像
    EDIT_IMAGE_ON_SERVER("editImageOnServer", getAllServerModel()),
    // 镜像模板 -发布
    IMAGE_TEMPLATE_PUBLISH("imageTemplatePublish", getAllServerModel()),
    // 镜像模板 -放弃
    IMAGE_TEMPLATE_GIVE_UP("imageTemplateGiveUp", getAllServerModel()),
    // 镜像模板 -取消锁定
    UNLOCKED_LOCAL_EDIT_IMAGE_TEMPLATE("unlockedLocalEditImageTemplate", getAllServerModel()),
    // 镜像模板 -取消上传
    IMAGE_TEMPLATE_ABORT("imageTemplateAbort", getAllServerModel()),
    // 镜像模板 -取消定时发布
    IMAGE_TEMPLATE_PUBLISH_TASK_CANCEL("imageTemplatePublishTaskCancel", getOnlyVdiServerModel()),
    // 镜像模板 -启动
    IMAGE_TEMPLATE_START("imageTemplateStart", getAllServerModel()),
    // 镜像模板 -关机
    IMAGE_TEMPLATE_POWER_OFF("imageTemplatePowerOff", getAllServerModel()),
    // 镜像模板 -镜像导出
    IMAGE_FILE_EXPORT("imageFileExport", getAllServerModel()),
    // 镜像模板 -驱动管理-删除
    IMAGE_DRIVER_DELETE("imageDriverDelete", getAllServerModel()),
    // 镜像模板 -上传
    IMAGE_DRIVER_UPLOAD("imageDriverUpload", getAllServerModel()),

    // 镜像文件 -上传
    IMAGE_FILE_UPLOAD("imageFileUpload", getAllServerModel()),
    // 镜像模板 -编辑
    IMAGE_FILE_EDIT("imageFileEdit", getAllServerModel()),
    // 镜像模板 -删除
    IMAGE_FILE_DELETE("imageFileDelete", getAllServerModel()),



    // 镜像快照 -恢复
    RESTORE_SNAPSHOT("restoreSnapshot", getAllServerModel()),
    // 镜像快照 -锁定和解锁
    LOCK_SNAPSHOT("lockSnapshot", getAllServerModel()),
    // 镜像快照 -删除
    DELETE_SNAPSHOT("deleteSnapshot", getAllServerModel()),
    // 镜像快照 -重命名
    RENAME_SNAPSHOT("renameSnapshot", getAllServerModel()),
    // 镜像快照 -设置
    CONFIG_SNAPSHOT_LIMIT("configSnapshotLimit", getAllServerModel()),
    // 镜像快照 -取消定时恢复
    CANCEL_SNAPSHOT_RESTORE_TASK("cancelSnapshotRestoreTask", getOnlyVdiServerModel()),

    // 镜像管理 -重置windows密码
    RESET_IMAGE_WINDOWS_PASSWORD("resetImageWinPwd", getAllServerModel()),

    // 软件安装包 -上传
    INSTALLATION_PACKAGE_UPLOAD("installationPackageUpload", getAllServerModel()),
    // 软件安装包 -编辑
    INSTALLATION_PACKAGE_EDIT("installationPackageEdit", getAllServerModel()),
    // 软件安装包 -删除
    INSTALLATION_PACKAGE_DELETE("installationPackageDelete", getAllServerModel()),

    // 用户管理 -创建用户
    CREATE_USER("createUser", getAllServerModel()),
    // 用户管理 -发送消息
    SEND_MESSAGE("sendMessage", getAllServerModel()),
    // 用户管理 -消息记录
    MESSAGE_RECORD("messageRecord", getAllServerModel()),
    // 用户管理 -重置密码
    RESET_PASSWORD("resetPassword", getAllServerModel()),
    // 用户管理 -账户解锁
    UNLOCK_ACCOUNT("unlockAccount", getAllServerModel()),
    // 用户管理 -升级为管理员
    UPGRADE_TO_ADMINISTRATOR("upgradeToAdministrator", getAllServerModel()),
    // 用户管理 -用户模板导入
    USER_TEMPLATE_IMPORT("userTemplateImport", getAllServerModel()),
    // 用户管理 -用户模板下载
    USER_TEMPLATE_DOWNLOAD("userTemplateDownload", getAllServerModel()),
    // 用户管理 -云桌面模板导入
    CLOUD_DESKTOP_TEMPLATE_IMPORT("cloudDesktopTemplateImport", getOnlyVdiServerModel()),
    // 用户管理 -云桌面模板下载
    CLOUD_DESKTOP_TEMPLATE_DOWNLOAD("cloudDesktopTemplateDownload", getOnlyVdiServerModel()),
    // 用户管理 -移动分组
    MOVE_GROUP("moveGroup", getAllServerModel()),
    // 用户管理 -硬件特征码管理
    USER_HARDWARE_SIGNATURE_MANAGER("userHardwareSignatureManager", getOnlyVdiServerModel()),
    // 用户管理 -动态口令密钥重置
    USER_ONE_TIME_PASSWORD_MANAGER("userOneTimePasswordManager", getAllServerModel()),
    // 用户管理 -编辑用户权限
    EDIT_USER_PERMISSION("editUserPermission", getAllServerModel()),
    // 用户管理 -编辑双网身份认证
    EDIT_DOUBLE_NETWORK_IDENTITY_AUTH("editDoubleNetworkIdentityAuth", getOnlyVdiServerModel()),
    // 用户管理 -删除
    DELETE_USER("deleteUser", getAllServerModel()),
    // 用户管理 -编辑用户基本信息
    EDIT_USER_BASIC_INFO("editUserBasicInfo", getAllServerModel()),
    // 用户管理 -编辑IDV云桌面
    EDIT_IDV_CLOUD_DESKTOP("editIdvCloudDesktop", getAllServerModel()),
    // 用户管理 -编辑TCI云桌面
    EDIT_TCI_CLOUD_DESKTOP("editTciCloudDesktop", getAllServerModel()),
    // 用户管理 -添加VDI云桌面
    EDIT_VDI_CLOUD_DESKTOP("editVdiCloudDesktop", getOnlyVdiServerModel()),
    // 用户管理 -从回收站恢复
    RECOVER_FROM_RECYCLE_BIN("recoverFromRecycleBin", getOnlyVdiServerModel()),
    // 用户管理 -配置用户组及用户
    CONFIGURE_USER_GROUPS_ADN_USER("configureUserGroupsAndUser", getAllServerModel()),
    // 用户管理 -编辑用户信息导入
    USER_INFO_EDIT_IMPORT("userInfoEditImport", getAllServerModel()),
    // 用户管理 -下载用户信息
    USER_INFO_DOWNLOAD("userInfoDownload", getAllServerModel()),
    // 用户管理 -下载全部用户信息
    USER_INFO_ALL_DOWNLOAD("userInfoAllDownload", getAllServerModel()),
    // 用户管理 -启用用户
    ENABLE_USER("enableUser", getAllServerModel()),
    // 用户管理 -禁用用户
    DISABLE_USER("disableUser", getAllServerModel()),

    // 用户管理 -用户组- 查看
    USER_GROUP_SELECT("userGroupSelect", getAllServerModel()),
    // 用户管理 -用户组- 创建用户组
    CREATE_USER_GROUP("createUserGroup", getAllServerModel()),
    // 用户管理 -用户组 - 发送消息
    SEND_USER_GROUP_MESSAGE("sendUserGroupMessage", getAllServerModel()),
    // 用户管理 -用户组 - 用户组模板导入
    USER_GROUP_TEMPLATE_IMPORT("userGroupTemplateImport", getAllServerModel()),
    // 用户管理 -用户组 - 用户组模板下载
    USER_GROUP_TEMPLATE_DOWNLOAD("userGroupTemplateDownload", getAllServerModel()),
    // 用户管理 -用户组 - 编辑
    USER_GROUP_TEMPLATE_EDIT("userGroupTemplateEdit", getAllServerModel()),
    // 用户管理 -用户组 - 刷新
    USER_GROUP_REFRESH("userGroupRefresh", getAllServerModel()),
    // 用户管理 -用户组 - 编辑用户组权限
    EDIT_USER_GROUP_PERMISSION("editUserGroupPermission", getAllServerModel()),
    // 用户管理 -用户组 - 批量应用VDI云桌面
    BATCH_USE_VDI_CLOUD_DESKTOP("batchUseVdiCloudDesktop", getOnlyVdiServerModel()),
    // 用户管理 -用户组 - 批量应用TCI云桌面
    BATCH_USE_TCI_CLOUD_DESKTOP("batchUseTciCloudDesktop", getAllServerModel()),
    // 用户管理 -用户组 - 批量应用IDV云桌面
    BATCH_USE_IDV_CLOUD_DESKTOP("batchUseIdvCloudDesktop", getAllServerModel()),

    // 应用管理 -编辑应用
    EDIT_APP("editApp", getAllServerModel()),
    // 应用管理 -发布
    PUBLISH_APP("publishApp", getAllServerModel()),
    // 应用管理 -下架
    WITHDRAW_APP("withdrawApp", getAllServerModel()),
    // 应用管理 -应用主机列表
    LIST_APP_HOST("listAppHost", getAllServerModel()),
    // 应用管理 -应用主机列表移除应用主机
    DELETE_APP_POOL_APP_HOST("deleteAppPoolAppHost", getAllServerModel()),
    // 应用管理 -创建应用组
    CREATE_APP_POOL("createAppPool", getAllServerModel()),
    // 应用管理 -删除应用组
    DELETE_APP_POOL("deleteAppPool", getAllServerModel()),
    // 应用管理 -编辑应用组
    EDIT_APP_POOL("editAppPool", getAllServerModel()),

    // 应用组管理 -创建应用组
    CREATE_APP_GROUP("createAppGroup", getAllServerModel()),
    // 应用组管理 -删除应用组
    DELETE_APP_GROUP("deleteAppGroup", getAllServerModel()),
    // 应用组管理 -编辑应用组
    EDIT_APP_GROUP("editAppGroup", getAllServerModel()),
    // 应用组管理 -删除应用
    DELETE_APP("deleteApp", getAllServerModel()),
    // 应用组管理 -分配用户
    ASSIGN_USER("assignUser", getAllServerModel()),
    // 应用组管理 -已分配用户
    ALREADY_ASSIGNED_USER("alreadyAssignedUser", getAllServerModel()),

    // 应用主机管理 -导出桌面信息
    EXPORT_APPLICATION_HOST_INFO("exportApplicationHostInfo", getAllServerModel()),
    // 应用主机管理 -添加应用主机
    ADD_APPLICATION_HOST_INFO("addApplicationHostInfo", getAllServerModel()),
    // 应用主机管理 -删除应用主机
    DELETE_APPLICATION_HOST_INFO("deleteApplicationHostInfo", getAllServerModel()),
    // 应用主机管理 -编辑应用主机
    EDIT_APPLICATION_HOST_INFO("editApplicationHostInfo", getAllServerModel()),
    // 应用主机管理 -编辑应用主机会话
    EDIT_APPLICATION_HOST_SESSION_INFO("editApplicationHostSessionInfo", getAllServerModel()),
    // 应用主机管理 -收集日志
    APPLICATION_HOST_COLLECT_LOG("applicationHostCollectLog", getAllServerModel()),
    // 应用主机管理 -下发升级
    HOST_AGENT_ISSUED_UPGRADE("hostAgentIssuedUpgrade", getAllServerModel()),

    // 终端管理 -瘦终端（VDI） - 关机
    VDI_TERMINAL_POWER_OFF("vdiTerminalPowerOff", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 重启
    VDI_TERMINAL_REBOOT("vdiTerminalReboot", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 终端检测
    VDI_TERMINAL_CHECK("vdiTerminalCheck", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 检测历史记录
    VDI_TERMINAL_CHECK_HISTORY("vdiTerminalCheckHistory", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 收集日志
    VDI_TERMINAL_LOG_COLLECT("vdiTerminalLogCollect", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 解除密码锁定
    VDI_TERMINAL_RELEASE_PASSWORD("vdiTerminalReleasePassword", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 移动分组
    VDI_TERMINAL_MOVE_GROUP("vdiTerminalMoveGroup", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 删除
    VDI_TERMINAL_DELETE("vdiTerminalDelete", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 编辑
    VDI_TERMINAL_EDIT("vdiTerminalEdit", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 终端组-创建终端组
    CREATE_VDI_TERMINAL_GROUP("createVdiTerminalGroup", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 终端组-编辑
    EDIT_VDI_TERMINAL_GROUP("editVdiTerminalGroup", getOnlyVdiServerModel()),
    // 终端管理 -瘦终端（VDI） - 终端组-删除
    DELETE_VDI_TERMINAL_GROUP("deleteVdiTerminalGroup", getOnlyVdiServerModel()),

    // 终端管理 -胖终端（IDV/TCI） - 关机
    IDV_TERMINAL_POWER_OFF("idvTerminalPowerOff", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 重启
    IDV_TERMINAL_REBOOT("idvTerminalReboot", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 离线登录设置
    IDV_TERMINAL_OFFLINE_LOGIN_SETTINGS("idvTerminalOfflineLoginSettings", getAllServerModel()),
    // 终端管理 --胖终端（IDV/TCI） - 删除
    IDV_TERMINAL_DELETE("idvTerminalDelete", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 编辑
    IDV_TERMINAL_EDIT("idvTerminalEdit", getAllServerModel()),
    // 终端管理 --胖终端（IDV/TCI） - 还原云桌面
    IDV_TERMINAL_RESTORE_CLOUD_DESKTOP("idvTerminalRestoreCloudDesktop", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 终端初始化
    IDV_TERMINAL_INIT("idvTerminalInit", getAllServerModel()),
    // 终端管理 --胖终端（IDV/TCI） - D盘清空
    IDV_TERMINAL_CLEAN_D_DISK("idvTerminalCleanDDisk", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 收集日志
    IDV_TERMINAL_LOG_COLLECT("idvTerminalLogCollect", getAllServerModel()),
    // 终端管理 --胖终端（IDV/TCI） - 解除密码锁定
    IDV_TERMINAL_RELEASE_PASSWORD("idvTerminalReleasePassword", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 搜索
    IDV_TERMINAL_SEARCH("idvTerminalSearch", getAllServerModel()),
    // 终端管理 --胖终端（IDV/TCI） - IDV终端批量清空D盘
    BATCH_IDV_TERMINAL_CLEAN_D_DISK("batchIdvTerminalCleanDDisk", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - IDV终端批量移动分组
    BATCH_IDV_TERMINAL_MOVE_GROUP("batchIdvTerminalMoveGroup", getAllServerModel()),
    // 终端管理 --胖终端（IDV/TCI） - IDV终端批量还原桌面
    BATCH_IDV_TERMINAL_RESTORE_CLOUD_DESKTOP("batchIdvTerminalRestoreCloudDesktop", getAllServerModel()),
    // 终端管理 --胖终端（IDV/TCI） - IDV终批量初始化终端
    BATCH_IDV_TERMINAL_INIT("batchIdvTerminalInit", getAllServerModel()),
    // 终端管理 --终端组-- 创建终端组
    CREATE_IDV_TERMINAL_GROUP("createIdvTerminalGroup", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 编辑
    EDIT_IDV_TERMINAL_GROUP("editIdvTerminalGroup", getAllServerModel()),
    // 终端管理 -胖终端（IDV/TCI） - 删除
    DELETE_IDV_TERMINAL_GROUP("deleteIdvTerminalGroup", getAllServerModel()),

    // 终端管理 --云办公客户端 - 编辑
    SOFT_TERMINAL_EDIT("softTerminalEdit", getOnlyVdiServerModel()),
    // 终端管理 --云办公客户端 - 删除
    SOFT_TERMINAL_DELETE("softTerminalDelete", getOnlyVdiServerModel()),
    // 终端管理 --云办公客户端 - 移动分组
    SOFT_TERMINAL_MOVE("softTerminalMove", getOnlyVdiServerModel()),
    // 终端管理 --云办公客户端 - 收集日志
    SOFT_TERMINAL_LOG_COLLECT("softTerminalLogCollect", getOnlyVdiServerModel()),
    // 终端管理 --云应用客户端 - 收集日志
    APP_TERMINAL_LOG_COLLECT("appTerminalLogCollect", getAllServerModel()),
    // 终端管理 --云应用客户端 - 删除
    APP_TERMINAL_DELETE("appTerminalDelete", getAllServerModel()),
    // 终端管理 --云办公客户端 - 解除密码锁定
    SOFT_TERMINAL_RELEASE_PASSWORD("softTerminalReleasePassword", getOnlyVdiServerModel()),
    // 终端管理 --云办公客户端 - 终端组-创建终端组
    CREATE_SOFT_TERMINAL_GROUP("createSoftTerminalGroup", getOnlyVdiServerModel()),
    // 终端管理 --云办公客户端 - 终端组-编辑
    EDIT_SOFT_TERMINAL_GROUP("editSoftTerminalGroup", getOnlyVdiServerModel()),
    // 终端管理 --云办公客户端 - 终端组-删除
    DELETE_SOFT_TERMINAL_GROUP("deleteSoftTerminalGroup", getOnlyVdiServerModel()),

    // 终端管理 --PC终端 - 编辑
    PC_TERMINAL_EDIT("pcTerminalEdit", getAllServerModel()),
    // 终端管理 --PC终端 - 删除
    PC_TERMINAL_DELETE("pcTerminalDelete", getAllServerModel()),
    // 终端管理 --PC终端 - 关机
    PC_TERMINAL_POWER_OFF("pcTerminalPowerOff", getAllServerModel()),
    // 终端管理 --PC终端 - 远程协助
    PC_TERMINAL_REMOTE_ASSISTANCE("pcTerminalRemoteAssistance", getAllServerModel()),
    // 终端管理 --PC终端 - 取消报障
    PC_TERMINAL_CANCEL_REPORT("pcTerminalCancelReport", getAllServerModel()),
    // 终端管理 --PC终端 - 移动分组
    PC_TERMINAL_MOVE("pcTerminalMove", getAllServerModel()),
    // 终端管理 --PC终端 - 搜索
    PC_TERMINAL_SEARCH("pcTerminalSearch", getAllServerModel()),
    // 终端管理 --PC终端 - 终端组-创建终端组
    CREATE_PC_TERMINAL_GROUP("createPcTerminalGroup", getAllServerModel()),
    // 终端管理 --PC终端 - 终端组-编辑
    EDIT_PC_TERMINAL_GROUP("editPcTerminalGroup", getAllServerModel()),
    // 终端管理 --PC终端 - 终端组-删除
    DELETE_PC_TERMINAL_GROUP("deletePcTerminalGroup", getAllServerModel()),
    // 终端管理 --PC终端 - 终端组-刷新
    REFRESH_PC_TERMINAL_GROUP("refreshPcTerminalGroup", getAllServerModel()),


    // 策略管理 --云桌面策略 - 创建
    CLOUD_DESKTOP_STRATEGY_CREATE("cloudDesktopStrategyCreate", getAllServerModel()),
    // 策略管理 --云桌面策略 - 编辑
    CLOUD_DESKTOP_STRATEGY_EDIT("cloudDesktopStrategyEdit", getAllServerModel()),
    // 策略管理 --云桌面策略 - 删除
    CLOUD_DESKTOP_STRATEGY_DELETE("cloudDesktopStrategyDelete", getAllServerModel()),


    // 策略管理 --临时权限策略 - 创建
    DESKTOP_TEMP_PERMISSION_CREATE("desktopTempPermissionCreate", getOnlyVdiServerModel()),
    // 策略管理 --临时权限策略 - 编辑
    DESKTOP_TEMP_PERMISSION_UPDATE("desktopTempPermissionUpdate", getOnlyVdiServerModel()),
    // 策略管理 --临时权限策略 - 删除
    DESKTOP_TEMP_PERMISSION_DELETE("desktopTempPermissionDelete", getOnlyVdiServerModel()),


    // 策略管理 --网络策略 - 创建
    NETWORK_STRATEGY_CREATE("networkStrategyCreate", getAllServerModel()),
    // 策略管理 --网络策略 - 编辑
    NETWORK_STRATEGY_EDIT("networkStrategyEdit", getAllServerModel()),
    // 策略管理 --网络策略 - 删除
    NETWORK_STRATEGY_DELETE("networkStrategyDelete", getAllServerModel()),

    // 策略管理 --云应用策略 - 创建
    APP_USER_STRATEGY_CREATE("appUserStrategyCreate", getAllServerModel()),
    // 策略管理 --云应用策略 - 编辑
    APP_USER_STRATEGY_EDIT("appUserStrategyEdit", getAllServerModel()),
    // 策略管理 --云应用策略 - 删除
    APP_USER_STRATEGY_DELETE("appUserStrategyDelete", getAllServerModel()),
    // 策略管理 --云应用策略 - 复制
    APP_USER_STRATEGY_COPY("appUserStrategyCopy", getAllServerModel()),

    // 策略管理 --云应用外设策略 - 创建
    RCA_USB_STRATEGY_CREATE("rcaUsbStrategyCreate", getAllServerModel()),
    // 策略管理 --云应用外设策略 - 编辑
    RCA_USB_STRATEGY_EDIT("rcaUsbStrategyEdit", getAllServerModel()),
    // 策略管理 --云应用外设策略 - 删除
    RCA_USB_STRATEGY_DELETE("rcaUsbStrategyDelete", getAllServerModel()),
    // 策略管理 --云应用外设策略 - 复制
    RCA_USB_STRATEGY_COPY("rcaUsbStrategyCopy", getAllServerModel()),

    // 策略管理 --USB外设管理 - 创建
    USB_MANAGE_CREATE("usbManageCreate", getAllServerModel()),
    // 策略管理 --USB外设管理 - 删除
    USB_MANAGE_DELETE("usbManageDelete", getAllServerModel()),
    // 策略管理 --USB外设管理 - 编辑
    USB_MANAGE_EDIT("usbManageEdit", getAllServerModel()),
    // 策略管理 --USB外设管理 - 高级配置
    USB_MANAGE_SETTINGS("usbManageSettings", getAllServerModel()),

    // 池
    CLOUD_DESKTOP_POOL("cloudDesktopPool", getOnlyVdiServerModel()),
    // 池 查看
    CLOUD_DESKTOP_POOL_SELECT("cloudDesktopPoolSelect", getOnlyVdiServerModel()),

    // 磁盘池
    CLOUD_DISK_POOL("cloudDiskPool", getOnlyVdiServerModel()),
    // 磁盘池查看
    CLOUD_DISK_POOL_SELECT("diskPoolSelect", getOnlyVdiServerModel()),
    
    // 外置存储
    EXTERNAL_STORAGE_MANAGE("externalStorageManage", getOnlyVdiServerModel()),
    // 外置存储-查看
    EXTERNAL_STORAGE_SELECT("externalStorageSelect", getOnlyVdiServerModel()),

    // 审计中心
    DATA_SECURITY_MANAGE("dataSecurityManage", getOnlyVdiServerModel()),
    // 审计中心-文件导出审批
    AUDIT_FILE_MANAGE("auditFileManage", getOnlyVdiServerModel()),

    /**
     * 桌面恢复
     */
    CLOUD_DESKTOP_RECOVER("cloudDesktopRecover", getOnlyVdiServerModel());

    private String menuName;

    private List<ServerModelEnum> supportServerModel;

    MenuType(String menuName, List<ServerModelEnum> supportServerModel) {
        this.menuName = menuName;
        this.supportServerModel = supportServerModel;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public List<ServerModelEnum> getSupportServerModel() {
        return this.supportServerModel;
    }

    private static List<ServerModelEnum> getAllServerModel() {
        return Lists.newArrayList(ServerModelEnum.VDI_SERVER_MODEL, ServerModelEnum.IDV_SERVER_MODEL,
                ServerModelEnum.MINI_SERVER_MODEL);
    }

    private static List<ServerModelEnum> getVdiAndIdvServerModel() {
        return Lists.newArrayList(ServerModelEnum.VDI_SERVER_MODEL, ServerModelEnum.IDV_SERVER_MODEL);
    }

    private static List<ServerModelEnum> getIdvAndMiniServerModel() {
        return Lists.newArrayList(ServerModelEnum.IDV_SERVER_MODEL, ServerModelEnum.MINI_SERVER_MODEL);
    }

    private static List<ServerModelEnum> getOnlyVdiServerModel() {
        return Lists.newArrayList(ServerModelEnum.VDI_SERVER_MODEL);
    }

    /**
     * 判断 是否系统管理员有权限的菜单
     * @return boolean
     */
    public Boolean isSysadminMenuType() {
        return this != MenuType.SYSTEM_USER_MANAGE
                && this != MenuType.USER_MANAGEMENT
                && this != MenuType.AD_DOMAIN
                && this != MenuType.LDAP_DOMAIN
                && this != MenuType.SYSTEM_USER
                && this != MenuType.THEME_STRATEGY
                && this != MenuType.SYSTEM_SETTING
                && this != MenuType.STRATEGY_MANAGE
                && this != MenuType.GLOBAL_STRATEGY
                && this != MenuType.SYSTEM_MAINTENANCE
                && this != MenuType.UPDATE_MANAGE
                && this != MenuType.TERMINAL_UPGRADE
                && this != MenuType.TERMINAL_UPGRADE_WITH_SOLO_COMP
                && this != MenuType.CMS_MANAGE
                && this != MenuType.PRINTER_MANAGE;
    }

    /**
     * 获取系统管理员有权限的菜单
     * @return string列表
     */
    public static List<String> getSysadminMenuTypeList() {
        return Arrays.stream(MenuType.values()).filter(MenuType::isSysadminMenuType)
                .map(MenuType::getMenuName)
                .collect(Collectors.toList());
    }

    /**
     * 判断是否审计管理员有权限的菜单
     * @return boolean
     */
    public Boolean isAudadminrMenuType() {
        return this == MenuType.AUDIT_LOGS;
    }

    /**
     * 获取所有审计管理员有权限菜单
     * @return 列表
     */
    public static List<String> getAudadminMenuTypeList() {
        List<String> list = new ArrayList<>();
        list.add(MenuType.AUDIT_LOGS.getMenuName());
        return list;
    }

    /**
     * 判断是否安全管理员有权限菜单
     * @return 列表
     */
    public Boolean isSecadminMenuType() {
        return this == MenuType.SYSTEM_USER_MANAGE || this == MenuType.SYSTEM_USER;
    }

    /**
     * 获取所有安全管理员有权限菜单
     * @return 列表
     */
    public static List<String> getSecadminMenuTypeList() {
        List<String> list = new ArrayList<>();
        list.add(MenuType.SYSTEM_USER_MANAGE.getMenuName());
        list.add(MenuType.SYSTEM_USER.getMenuName());
        return list;
    }

    /**
     * 判断menuName是否合法
     * @param menuName 菜单名称string
     * @return boolean 是否合法
     */
    public static boolean isLegalMenuName(String menuName) {
        Assert.notNull(menuName, "menuName is not null!");

        boolean isLegal = false;
        for (MenuType menuType : MenuType.values()) {
            if (menuType.getMenuName().equals(menuName)) {
                isLegal = true;
                break;
            }
        }

        return isLegal;
    }

    /**
     * 获取必选菜单列表
     * @return 列表
     */
    public static List<String> getDefaultMenuTypeList() {
        List<String> list = new ArrayList<>();
        list.add(MenuType.CLOUD_DESKTOP_MANAGEMENT.getMenuName());
        list.add(MenuType.CLOUD_DESKTOP_MANAGE.getMenuName());
        list.add(MenuType.RECYCLE_BIN.getMenuName());
        list.add(MenuType.USER_MANAGE.getMenuName());
        list.add(MenuType.TERMINAL_MANAGE.getMenuName());
        list.add(MenuType.VDI_TERMINAL.getMenuName());
        list.add(MenuType.IDV_TERMINAL.getMenuName());
        list.add(MenuType.SOFT_TERMINAL.getMenuName());
        list.add(MenuType.PC_TERMINAL.getMenuName());
        list.add(MenuType.AUDIT_LOGS.getMenuName());
        return list;
    }
}
