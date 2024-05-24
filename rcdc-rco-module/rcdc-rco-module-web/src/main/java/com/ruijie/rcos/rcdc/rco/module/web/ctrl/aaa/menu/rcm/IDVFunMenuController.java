package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.menu.rcm;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermission;
import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermissions;
import com.ruijie.rcos.gss.base.iac.module.annotation.PermissionTag;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;

import io.swagger.annotations.Api;

import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.SECADMIN;
import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.SYSADMIN;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/13 9:54
 *
 * @author linrenjian
 */

@Api(tags = "功能菜单信息")
@Controller
@RequestMapping("/rco/menu")
@BuiltInPermissions({
    // --------- START 首页 ---------
    // --------- END 首页 ---------
    // --------- START 云桌面管理 ---------
    // 云桌面管理 云桌面管理 回收站 必选项目 ENABLE_NEED


    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "powerOff", name = "关机",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "102")},
            uri = "/rco/user/cloudDesktop/shutdown"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "restore", name = "还原",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "105")},
            uri = "/rco/user/cloudDesktop/revert"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "editCloudDesktopStrategy", name = "编辑云桌面策略",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "106")},
            uri = "/rco/user/cloudDesktop/strategy/edit"),
    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "revertAppDisk", name = "还原应用磁盘",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "107")},
            uri = "/rco/user/cloudDesktop/revertAppDisk"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "cancelReport", name = "取消报障",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "112")},
            uri = "/rco/user/cloudDesktop/relieveFault"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "remoteAssistance", name = "远程协助",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "113")},
            uri = "/rco/user/remoteAssist/assistRequest"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "forcedShutdown", name = "强制关机",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "114")},
            uri = "/rco/user/cloudDesktop/powerOff"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "cloudDesktopLogCollect", name = "云桌面日志收集",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "115")},
            uri = "/rco/clouddesktop/GTlog/collect"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "checkDeskPort", name = "检测端口",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                 @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                 @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                 @PermissionTag(key = FunTypes.ORDER, value = "117")},
            uri = "/rco/user/cloudDesktop/checkDeskPort"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "changeCloudDesktopImageTemplate", name = "变更镜像模板",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "119")},
            uri = "/rco/user/cloudDesktop/editImage"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "editComputerName", name = "编辑计算机名",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "120")},
            uri = "/rco/deskStrategy/computerName/edit"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "exportCloudDesktopInfo", name = "导出桌面信息",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "131")},
            uri = "/rco/user/cloudDesktop/export"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "editSoftwareStrategy", name = "编辑软件管控策略",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "133"),
                @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH_KEY, value = Constants.ENABLE_SOFTWARE_STRATEGY)},
            uri = "/rco/user/cloudDesktop/editSoftwareStrategy"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "editUserProfileStrategy", name = "编辑个性化配置策略",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "134")},
            uri = "/rco/user/cloudDesktop/editUserProfileStrategy"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "idvConfigNetwork", name = "设置IDV云桌面网络",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "135")},
            uri = "/rco/user/cloudDesktop/idv/editNetwork"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "idvConfigDNS", name = "设置IDV云桌面DNS",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "136")},
            uri = "/rco/user/cloudDesktop/idv/editDns"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "allIdvConfigDNS", name = "设置全部IDV云桌面DNS",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "137")},
            uri = "/rco/user/cloudDesktop/idv/editDnsAll"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "editDeskstopRole", name = "设置VIP云桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "139")},
            uri = "/rco/user/cloudDesktop/role/edit"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "intoMaintainMode", name = "进入维护模式",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "140")},
            uri = "/rco/user/cloudDesktop/enableMaintenanceMode"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "outMaintainMode", name = "退出维护模式",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "141")},
            uri = "/rco/user/cloudDesktop/disableMaintenanceMode"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "moveDesktop", name = "移动分组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "141")},
        uri = "/rco/user/cloudDesktop/moveDesktop"),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "resetDesktopWinPwd", name = "重置云桌面windows密码",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "132")},
            uri = "/rco/user/cloudDesktop/resetWinPwd"),

    // VDI才有回收站

    // --------- END 云桌面管理 ---------
    // --------- START 镜像管理 ---------

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateCreate", name = "创建",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "203")},
            uri = "/rco/clouddesktop/imageTemplate/create"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateCopy", name = "复制模板",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "204")},
            uri = "/rco/clouddesktop/imageTemplate/clone"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "205")},
            uri = "/rco/clouddesktop/imageTemplate/delete"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateDriverInstall", name = "驱动安装",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "206")},
            uri = "/rco/clouddesktop/imageTemplate/startTerminalImage"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "editImageOnServer", name = "编辑镜像",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "207")},
            uri = "/rco/clouddesktop/imageTemplate/edit"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplatePublish", name = "发布",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "208")},
            uri = {"/rco/clouddesktop/imageTemplate/publish", "/rco/clouddesktop/imageTemplate/createOrUpdatePublishTask"}),

    @BuiltInPermission(parentCode = "imageTemplate", code = "unlockedLocalEditImageTemplate", name = "取消锁定",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "210")},
            uri = "/rco/clouddesktop/imageTemplate/unlockedLocalEditImageTemplate"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateAbort", name = "取消上传",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "211")},
            uri = {"/rco/clouddesktop/imageTemplate/abortLocalEditImageTemplate", "/rco/clouddesktop/imageTemplate/abortLocalEditNewImageTemplate"}),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateGiveUp", name = "放弃",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "212")},
            uri = "/rco/clouddesktop/imageTemplate/giveup"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateStart", name = "启动",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "213")},
            uri = {"/rco/clouddesktop/imageTemplate/run"}),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplatePowerOff", name = "关机",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "214")},
            uri = "/rco/clouddesktop/imageTemplate/stop"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageFileExport", name = "导出",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "215")},
            uri = "/rco/imageExport/export"),
    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateRollback", name = "回退",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "216")},
            uri = "/rco/clouddesktop/imageTemplate/rollback"),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateVersionManage", name = "版本管理",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "217")},
            uri = "/rco/clouddesktop/imageTemplate/list"),
    @BuiltInPermission(parentCode = "imageTemplateVersionManage", code = "imageTemplateVersionQuery", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "218")}),
    @BuiltInPermission(parentCode = "imageTemplateVersionManage", code = "imageTemplateVersionDelivery", name = "交付",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "219")},
            uri = "/rco/clouddesktop/imageTemplate/delivery"),
    @BuiltInPermission(parentCode = "imageTemplateVersionManage", code = "imageTemplateVersionCopy", name = "复制模板",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "220")},
            uri = "/rco/clouddesktop/imageTemplate/clone"),
    @BuiltInPermission(parentCode = "imageTemplateVersionManage", code = "imageTemplateVersionEditBaseInfo", name = "编辑基本信息",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "221")},
            uri = "/rco/clouddesktop/imageTemplate/editBaseInfo"),
    @BuiltInPermission(parentCode = "imageTemplateVersionManage", code = "imageTemplateVersionDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "222")},
            uri = "/rco/clouddesktop/imageTemplate/delete"),
    @BuiltInPermission(parentCode = "imageTemplateVersionManage", code = "imageTemplateVersionExport", name = "导出",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "223")},
            uri = "/rco/imageExport/export"),

    @BuiltInPermission(parentCode = "imageFiles", code = "imageFileDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "234")},
            uri = "/rco/clouddesktop/osFile/delete"),

    @BuiltInPermission(parentCode = "imageFiles", code = "imageFileEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "233")},
            uri = "/rco/clouddesktop/osFile/edit"),

    @BuiltInPermission(parentCode = "imageFiles", code = "imageFileUpload", name = "上传",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "232")},
            uri = {"/rco/clouddesktop/osFile/create", "/rco/clouddesktop/osFile/iso/create"}),


    @BuiltInPermission(parentCode = "installationPackage", code = "installationPackageUpload", name = "上传",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "252")},
            uri = "/rco/deskSoft/create"),

    @BuiltInPermission(parentCode = "installationPackage", code = "installationPackageEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "253")},
            uri = "/rco/deskSoft/edit"),

    @BuiltInPermission(parentCode = "installationPackage", code = "installationPackageDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "254")},
            uri = "/rco/deskSoft/delete"),


    @BuiltInPermission(parentCode = "imageTemplate", code = "listSnapshot", name = "快照管理",
            roles = {SYSADMIN},
            uri = "/rco/clouddesktop/imageTemplate/listSnapshot",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "261")}),

    @BuiltInPermission(parentCode = "listSnapshot", code = "snapshotQuery", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ORDER, value = "262")}),

    @BuiltInPermission(parentCode = "listSnapshot", code = "restoreSnapshot", name = "恢复",
            roles = {SYSADMIN},
            uri = "/rco/clouddesktop/imageTemplate/createOrUpdateSnapshotRestoreTask",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "263")}),
    @BuiltInPermission(parentCode = "listSnapshot", code = "lockSnapshot", name = "锁定和解锁",
            roles = {SYSADMIN},
            uri = "/rco/clouddesktop/imageTemplate/lockSnapshot",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "264")}),
    @BuiltInPermission(parentCode = "listSnapshot", code = "deleteSnapshot", name = "删除",
            roles = {SYSADMIN},
            uri = "/rco/clouddesktop/imageTemplate/deleteSnapshot",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "265")}),
    @BuiltInPermission(parentCode = "listSnapshot", code = "renameSnapshot", name = "重命名",
            roles = {SYSADMIN},
            uri = "/rco/clouddesktop/imageTemplate/renameSnapshot",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "266")}),
    @BuiltInPermission(parentCode = "listSnapshot", code = "configSnapshotLimit", name = "设置",
            roles = {SYSADMIN},
            uri = "/rco/clouddesktop/imageTemplate/configSnapshotLimit",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "267")}),

    @BuiltInPermission(parentCode = "imageTemplate", code = "resetImageWinPwd", name = "重置镜像windows密码",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "269")},
            uri = "/rco/clouddesktop/imageTemplate/resetWinPwd"),

    // --------- START 驱动管理 ---------
    @BuiltInPermission(parentCode = "imageDriverManagement", code = "imageDriverQuery", name = "查看",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "270"),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES) }),
    @BuiltInPermission(parentCode = "imageDriverManagement", code = "imageDriverUpload", name = "上传",
            uri = "/rco/clouddesktop/imageDriver/create",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ORDER, value = "271"),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES)}),
    @BuiltInPermission(parentCode = "imageDriverManagement", code = "imageDriverDelete", name = "删除",
            uri = "/rco/clouddesktop/imageDriver/delete",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ORDER, value = "272"),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES) }),
    @BuiltInPermission(parentCode = "imageDriverManagement", code = "imageDriverEdit", name = "编辑",
            uri = "/rco/clouddesktop/imageDriver/edit",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ORDER, value = "273"),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES)}),
    // --------- END 驱动管理 ---------

    // 驱动管理超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    // --------- END 镜像管理 ---------
    // --------- START 安全组管理 ---------
    @BuiltInPermission(parentCode = "adGroupManage", code = "batchCreateAdGroup", name = "添加",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "303")},
            uri = "/rco/adGroup/batchCreate"),
    @BuiltInPermission(parentCode = "adGroupManage", code = "delAdGroup", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "304")},
            uri = "/rco/adGroup/delete"),
    @BuiltInPermission(parentCode = "adGroupManage", code = "syncAdGroup", name = "同步",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "305")},
            uri = "/rco/adGroup/syncAdGroup"),
    // --------- END 安全组管理 ---------
    // --------- START 用户管理 ---------
    // 用户管理 必选项目 ENABLE_NEED 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN

    @BuiltInPermission(parentCode = "userManage", code = "createUser", name = "创建用户",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "303")},
            uri = "/rco/user/create"),

    @BuiltInPermission(parentCode = "userManage", code = "editUserBasicInfo", name = "编辑基本信息",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "304")},
            uri = "/rco/user/edit"),

    @BuiltInPermission(parentCode = "userManage", code = "deleteUser", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "305")},
            uri = "/rco/user/delete"),

    @BuiltInPermission(parentCode = "userManage", code = "sendMessage", name = "发送消息",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "306")},
            uri = "/rco/user/message/create"),

    @BuiltInPermission(parentCode = "userManage", code = "resetPassword", name = "重置密码",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "308")},
            uri = "/rco/user/resetPassword"),


    @BuiltInPermission(parentCode = "userManage", code = "unlockAccount", name = "解除登录限制",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "309")},
            uri = "/rco/certificationStrategy/unLockUser"),

    @BuiltInPermission(parentCode = "userManage", code = "upgradeToAdministrator", name = "设置为管理员",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "310")},
            uri = "/rco/admin/upgradeAdmin"),

    @BuiltInPermission(parentCode = "userManage", code = "userTemplateDownload", name = "用户模板下载",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "311")},
            uri = "/rco/user/downloadTemplate"),

    @BuiltInPermission(parentCode = "userManage", code = "userTemplateImport", name = "用户模板导入",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "312")},
            uri = "/rco/user/importUser"),

    @BuiltInPermission(parentCode = "userManage", code = "moveGroup", name = "移动分组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "315")},
            uri = "/rco/user/modifyUserGroup"),

    @BuiltInPermission(parentCode = "userManage", code = "userOneTimePasswordManager", name = "动态口令密钥重置",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "317")},
            uri = "/rco/user/otpCertification/reset"),

    @BuiltInPermission(parentCode = "userManage", code = "editUserPermission", name = "编辑用户权限",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "318")},
            uri = "/rco/user/adUserAuthority/edit"),

    @BuiltInPermission(parentCode = "userManage", code = "editIdvCloudDesktop", name = "编辑IDV云桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "320")},
            uri = "/rco/user/idvConfig/edit"),

    @BuiltInPermission(parentCode = "userManage", code = "editTciCloudDesktop", name = "编辑TCI云桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "321")},
            uri = "/rco/user/voiConfig/edit"),

    @BuiltInPermission(parentCode = "userManage", code = "configureUserGroupsAndUser", name = "配置用户组及用户",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "324")},
            uri = "/rco/user/batch/certification"),

    @BuiltInPermission(parentCode = "userManage", code = "userInfoEditImport", name = "编辑用户信息导入",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "325")},
            uri = "/rco/user/editUser"),

    @BuiltInPermission(parentCode = "userManage", code = "userInfoDownload", name = "下载用户信息",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "326")},
            uri = "/rco/user/exportUser"),

    @BuiltInPermission(parentCode = "userManage", code = "enableUser", name = "启用",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "328")},
            uri = "/rco/user/enableUser"),

    @BuiltInPermission(parentCode = "userManage", code = "disableUser", name = "禁用",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "329")},
            uri = "/rco/user/disableUser"),

    @BuiltInPermission(parentCode = "userGroup", code = "createUserGroup", name = "创建用户组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "332")},
            uri = "/rco/user/group/create"),

    @BuiltInPermission(parentCode = "userGroup", code = "sendUserGroupMessage", name = "发送消息",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "333")},
            uri = "/rco/user/message/userGroupMessage/create"),

    @BuiltInPermission(parentCode = "userGroup", code = "userGroupTemplateDownload", name = "用户组模板下载",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "334")},
            uri = "/rco/user/group/downloadTemplate"),

    @BuiltInPermission(parentCode = "userGroup", code = "userGroupTemplateImport", name = "用户组模板导入",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "335")},
            uri = "/rco/user/group/importUserGroup"),

    @BuiltInPermission(parentCode = "userGroup", code = "userGroupTemplateEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "336")},
            uri = "/rco/user/group/edit"),

    @BuiltInPermission(parentCode = "userGroup", code = "deleteUserGroup", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "337")},
            uri = "/rco/user/group/delete"),

    @BuiltInPermission(parentCode = "userGroup", code = "editUserGroupPermission", name = "编辑用户组权限",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "338")},
            uri = "/rco/user/group/adUserAuthority/edit"),


    @BuiltInPermission(parentCode = "userGroup", code = "batchUseIdvCloudDesktop", name = "批量应用IDV云桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "339")},
            uri = "/rco/user/group/applyIDVDesktop"),

    @BuiltInPermission(parentCode = "userGroup", code = "batchUseTciCloudDesktop", name = "批量应用TCI云桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "340")},
            uri = "/rco/user/group/applyVOIDesktop"),

    @BuiltInPermission(parentCode = "userManage", code = "recoverInvalidTime", name = "解除失效",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "341")},
            uri = "/rco/user/recoverInvalidTime"),


    // --------- END 用户管理 ---------
    // --------- START 终端管理 ---------
    // 终端管理 必选项目 ENABLE_NEED

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalPowerOff", name = "关机",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "432")},
            uri = "/rco/terminal/idv/shutdown"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalReboot", name = "重启",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "433")},
            uri = "/rco/terminal/idv/restart"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "435")},
            uri = "/rco/terminal/idv/delete"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "436")},
            uri = "/rco/terminal/idv/edit"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalRestoreCloudDesktop", name = "还原云桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "437")},
            uri = "/rco/user/cloudDesktop/idv/revert"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalInit", name = "终端初始化",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "438")},
            uri = "/rco/terminal/idv/init"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalLogCollect", name = "收集日志",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "439")},
            uri = "/rco/terminal/idv/collectLog"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalCleanDDisk", name = "本地盘清空",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "439")},
            uri = "/rco/terminal/idv/diskClear"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalReleasePassword", name = "解除密码锁定",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "440")},
            uri = "/rco/certificationStrategy/idv/unlockTerminalMngPwd"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalMoveGroup", name = "移动分组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "441")},
            uri = "/rco/terminal/idv/updateGroup"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "configIDVNicWorkMode", name = "设置云桌面网卡模式",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "442")},
            uri = "/rco/terminal/idv/configNicWorkMode"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "openFullSystemDisk", name = "系统盘自动扩容",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "443"),
                @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH_KEY, value = Constants.ENABLE_FULL_SYSTEM_DISK_GLOBAL_STRATEGY)},
            uri = "/rco/terminal/idv/fullSystemDisk/edit"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvUserChange", name = "用户变更",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "444")},
            uri = "/rco/terminal/idv/bindUser"),

    @BuiltInPermission(parentCode = "idvTerminal", code = "configTerminalNetwork", name = "设置终端网络",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "445")},
            uri = {"/rco/terminal/idv/editNetwork", "/rco/terminal/idv/editIp"}),
    @BuiltInPermission(parentCode = "idvTerminal", code = "changeServerIp", name = "修改终端云服务器地址",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "446")},
            uri = "/rco/terminal/idv/changeServerIp"),


    @BuiltInPermission(parentCode = "idvTerminalGroup", code = "createIdvTerminalGroup", name = "创建终端组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "447")},
            uri = "/rco/terminal/group/idv/create"),

    @BuiltInPermission(parentCode = "idvTerminalGroup", code = "editIdvTerminalGroup", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "448")},
            uri = "/rco/terminal/group/idv/edit"),

    @BuiltInPermission(parentCode = "idvTerminalGroup", code = "deleteIdvTerminalGroup", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "449")},
            uri = "/rco/terminal/group/idv/delete"),

    @BuiltInPermission(parentCode = "idvTerminalGroup", code = "configAllTerminalNetwork", name = "设置终端网络",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "451")},
            uri = "/rco/terminal/idv/editIpAll"),

    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "472")},
            uri = "/rco/computer/edit"),

    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "473")},
            uri = "/rco/computer/delete"),

    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalRemoteAssistance", name = "远程协助",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "474")},
            uri = "/rco/computer/remoteAssist/assistRequest"),

    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalPowerOff", name = "关机",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "474")},
            uri = "/rco/computer/batchShutdown"),

    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalCancelReport", name = "取消报障",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "476")},
            uri = "/rco/computer/relieveFault"),

    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalMove", name = "移动分组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "477")},
            uri = "/rco/computer/updateGroup"),


    @BuiltInPermission(parentCode = "pcTerminalGroup", code = "createPcTerminalGroup", name = "创建终端组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "483")},
            uri = "/rco/terminal/group/pc/create"),

    @BuiltInPermission(parentCode = "pcTerminalGroup", code = "editPcTerminalGroup", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "484")},
            uri = "/rco/terminal/group/pc/edit"),

    @BuiltInPermission(parentCode = "pcTerminalGroup", code = "deletePcTerminalGroup", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "485")},
            uri = "/rco/terminal/group/pc/delete"),

    // --------- END 终端管理 ---------
    // --------- START 策略管理 ---------
    @BuiltInPermission(parentCode = "cloudDesktopStrategy", code = "cloudDesktopStrategyCreate", name = "创建",
            roles = {SYSADMIN},
            uri = {"/rco/deskStrategy/create/idv", "/rco/deskStrategy/create/vdi", "/rco/deskStrategy/create/voi"},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "502")}),

    @BuiltInPermission(parentCode = "cloudDesktopStrategy", code = "cloudDesktopStrategyEdit", name = "编辑",
            roles = {SYSADMIN},
            uri = {"/rco/deskStrategy/edit/idv", "/rco/deskStrategy/edit/vdi", "/rco/deskStrategy/edit/voi"},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "503")}),


    @BuiltInPermission(parentCode = "cloudDesktopStrategy", code = "cloudDesktopStrategyDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "504")},
            uri = "/rco/deskStrategy/delete"),

    @BuiltInPermission(parentCode = "networkStrategy", code = "networkStrategyCreate", name = "创建",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "512")},
            uri = "/rco/clouddesktop/deskNetwork/create"),

    @BuiltInPermission(parentCode = "networkStrategy", code = "networkStrategyEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "513")},
            uri = "/rco/clouddesktop/deskNetwork/edit"),

    @BuiltInPermission(parentCode = "networkStrategy", code = "networkStrategyDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "514")},
            uri = "/rco/clouddesktop/deskNetwork/delete"),


    @BuiltInPermission(parentCode = "usbManage", code = "usbManageCreate", name = "创建",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "532")},
            uri = "/rco/usbType/create"),

    @BuiltInPermission(parentCode = "usbManage", code = "usbManageEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "533")},
            uri = "/rco/usbType/edit"),

    @BuiltInPermission(parentCode = "usbManage", code = "usbManageDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "534")},
            uri = "/rco/usbType/delete"),


    // --------- END 策略管理 ---------
    // --------- START 系统配置 ---------
    // 系统配置 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    // --------- END 系统配置 ---------
    @BuiltInPermission(parentCode = "auditLogs", code = "auditLogCycle", name = "日志周期设置",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "732")},
            uri = "/rco/logInterval/edit"),
    // --------- START 系统运维 ---------

    // --------- END 系统运维 ---------


    // start 云应用管理


    // end 云应用管理

    // start 应用中心
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageMake", name = "制作",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "851")},
            uri = {"/rco/appCenter/appStore/pushInstallPackage/create", "/rco/app/disk/create"}),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageInfoEdit", name = "编辑",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "852")},
            uri = {"/rco/app/disk/edit", "/rco/appCenter/appStore/pushInstallPackage/edit"}),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageCopy", name = "复制",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "853")},
            uri = "/rco/app/disk/clone"),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageRollback", name = "回退",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "854")},
            uri = "/rco/app/disk/rollback"),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageContinueMake", name = "继续制作",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "855")},
            uri = "/rco/app/disk/finish"),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageGiveUpEdit", name = "放弃制作",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "857")},
            uri = {"/rco/app/disk/abort", "/rco/appCenter/appStore/pushInstallPackage/giveUp"}),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "858")},
            uri = "/rco/appCenter/appStore/delete"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestTaskCreate", name = "新建测试任务",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "859")},
            uri = "/rco/appCenter/appTest/create"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestTaskEdit", name = "编辑测试任务",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "860")},
            uri = "/rco/appCenter/appTest/edit"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestTaskFinish", name = "完成测试任务",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "861")},
            uri = "/rco/appCenter/appTest/complete"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestTaskDelete", name = "删除测试任务",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "862")},
            uri = "/rco/appCenter/appTest/delete"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestDeskCreate", name = "新增测试桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "863")},
            uri = "/rco/appCenter/appTest/addDesktop"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestDeskFinish", name = "结束桌面测试",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "864")},
            uri = "/rco/appCenter/appTest/desktop/complete"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestDeskRetest", name = "重新测试",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "866")},
            uri = "/rco/appCenter/appTest/enter"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestDeskDelete", name = "删除测试桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "867")},
            uri = "/rco/appCenter/appTest/desktop/delete"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestDeskCollectLog", name = "收集日志",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "868")},
            uri = "/rco/appCenter/appTest/collect/log"),
    @BuiltInPermission(parentCode = "appTest", code = "appTestDeskDownloadLog", name = "下载日志",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "869")},
            uri = "/rco/appCenter/appTest/downloadLog"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryAppDiskCreate", name = "创建应用磁盘交付组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "870")},
            uri = "/rco/appCenter/appDelivery/appDisk/create"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryPushInstallPackageCreate", name = "创建推送安装包交付组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "871")},
            uri = "/rco/appCenter/appDelivery/pushInstallPackage/create"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryAppDiskEdit", name = "编辑应用磁盘交付组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "872")},
            uri = "/rco/appCenter/appDelivery/appDisk/edit"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryPushInstallPackageEdit", name = "编辑推送安装包交付组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "873")},
            uri = "/rco/appCenter/appDelivery/pushInstallPackage/edit"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryDelete", name = "删除交付组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "874")},
            uri = "/rco/appCenter/appDelivery/delete"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryAppDiskDeskCreate", name = "应用磁盘新增桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "875")},
            uri = "/rco/appCenter/appDelivery/appDisk/deliveryObject/add"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryPushInstallPackageDeskCreate", name = "推送安装包新增桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "876")},
            uri = "/rco/appCenter/appDelivery/pushInstallPackage/deliveryObject/add"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryDeskDelete", name = "删除桌面",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "877")},
            uri = "/rco/appCenter/appDelivery/deliveryObject/delete"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryDeskRedeliver", name = "桌面重新交付",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "879")},
            uri = "/rco/appCenter/appDelivery/deliveryObject/redeliveryObject"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryAppRedeliver", name = "应用重新交付",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "880")},
            uri = "/rco/appCenter/appDelivery/deliveryObjectDetail/redeliveryApp"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryAppDiskAppCreate", name = "应用磁盘新增应用",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "881")},
            uri = "/rco/appCenter/appDelivery/appDisk/deliveryApp/add"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryPushInstallPackageAppCreate", name = "推送安装包新增应用",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "882")},
            uri = "/rco/appCenter/appDelivery/pushInstallPackage/deliveryApp/add"),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliveryAppDelete", name = "删除应用",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "883")},
            uri = "/rco/appCenter/appDelivery/deliveryApp/delete"),
    @BuiltInPermission(parentCode = "fileDistribution", code = "fileDistributionCreate", name = "创建任务",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "884")}),
    @BuiltInPermission(parentCode = "fileDistribution", code = "fileDistributionCopy", name = "复制",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "885")}),
    @BuiltInPermission(parentCode = "fileDistribution", code = "fileDistributionDelete", name = "删除",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "886")},
            uri = "/rco/fileDistribute/task/delete"),
    @BuiltInPermission(parentCode = "fileDistribution", code = "fileDistributionCancel", name = "取消",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "888")},
            uri = "/rco/fileDistribute/task/cancel"),
    @BuiltInPermission(parentCode = "fileDistribution", code = "fileDistributionList", name = "文件列表",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "889")}),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackageUpload", name = "上传文件",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "892")}),
    @BuiltInPermission(parentCode = "appRepo", code = "appPackagePublish", name = "发布",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "893")},
            uri = {"/rco/app/disk/publish", "/rco/appCenter/appStore/pushInstallPackage/publish"}),
    @BuiltInPermission(parentCode = "appTest", code = "appTestTaskRedistribute", name = "应用重新下发",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "894")},
            uri = "/rco/appCenter/appTest/redistribute/appDisk"),

})
public class IDVFunMenuController {
}
