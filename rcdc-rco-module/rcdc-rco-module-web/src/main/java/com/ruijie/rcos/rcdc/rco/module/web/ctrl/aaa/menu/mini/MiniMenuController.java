package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.menu.mini;


import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermission;
import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermissions;
import com.ruijie.rcos.gss.base.iac.module.annotation.PermissionTag;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.AUDADMIN;
import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.SECADMIN;
import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.SYSADMIN;

/**
 * Description: 统一管理菜单
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/14 17:08
 *
 * @author linrenjian
 */
@Api(tags = "菜单信息")
@Controller
@RequestMapping("/rco/menu")
@BuiltInPermissions({
    @BuiltInPermission(code = "CDC", name = "云办公管理平台",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ORDER, value = "0")}),

    // --------- START 首页 ---------
    @BuiltInPermission(code = "dashboard", name = "首页",
            parentCode = "CDC",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "0")}),
    // --------- END 首页 ---------
    // --------- START 云桌面管理 ---------
    // 云桌面管理 云桌面管理 回收站 必选项目 ENABLE_NEED
    @BuiltInPermission(code = "cloudDesktopManagement", name = "云桌面管理",
            parentCode = "CDC",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "100")}),

    @BuiltInPermission(parentCode = "cloudDesktopManagement", code = "cloudDesktopManage", name = "云桌面管理",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "101")}),

    @BuiltInPermission(parentCode = "cloudDesktopManage", code = "cloudDesktopManageSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "102")}),


    // --------- END 云桌面管理 ---------
    // --------- START 镜像管理 ---------
    @BuiltInPermission(code = "imageManage", name = "镜像管理",
            roles = {SYSADMIN},
            parentCode = "CDC",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "200")}),

    @BuiltInPermission(parentCode = "imageManage", code = "imageTemplate", name = "镜像模板",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "201")}),

    @BuiltInPermission(parentCode = "imageTemplate", code = "imageTemplateSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "202")}),


    @BuiltInPermission(parentCode = "imageManage", code = "imageFiles", name = "镜像文件",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "230")}),

    @BuiltInPermission(parentCode = "imageFiles", code = "imageFilesSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "231")}),

    @BuiltInPermission(parentCode = "imageManage", code = "installationPackage", name = "共享文件空间",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "250")}),

    @BuiltInPermission(parentCode = "installationPackage", code = "installationPackageSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "251")}),

    // 驱动管理超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "imageManage", code = "imageDriverManagement", name = "驱动管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "252")}),

    // --------- END 镜像管理 ---------
    // --------- START 用户管理 ---------
    // 用户管理 必选项目 ENABLE_NEED 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(code = "userManagement", name = "用户管理",
            parentCode = "CDC",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "300")}),

    @BuiltInPermission(parentCode = "userManagement", code = "userManage", name = "用户管理",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "301")}),

    @BuiltInPermission(parentCode = "userManage", code = "userManageSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "302")}),

    @BuiltInPermission(parentCode = "userManage", code = "messageRecord", name = "消息记录",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "307")}),

    @BuiltInPermission(parentCode = "userManage", code = "userGroup", name = "用户组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "330")}),

    @BuiltInPermission(parentCode = "userGroup", code = "userGroupSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "302")}),


    // --------- END 用户管理 ---------
    // --------- START 终端管理 ---------
    // 终端管理 必选项目 ENABLE_NEED
    @BuiltInPermission(code = "terminalManage", name = "终端管理",
            roles = {SYSADMIN},
            parentCode = "CDC",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "400")}),

    @BuiltInPermission(parentCode = "terminalManage", code = "idvTerminal", name = "胖终端（IDV/TCI）",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "430")}),
    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "431")}),

    @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalGroup", name = "终端组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "445")}),

    @BuiltInPermission(parentCode = "idvTerminalGroup", code = "idvTerminalGroupSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "446")}),

    @BuiltInPermission(parentCode = "terminalManage", code = "pcTerminal", name = "PC终端",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "470")}),

    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "471")}),


    @BuiltInPermission(parentCode = "pcTerminal", code = "pcTerminalGroup", name = "终端组",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "480")}),

    @BuiltInPermission(parentCode = "pcTerminalGroup", code = "pcTerminalGroupSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "481")}),

    // --------- END 终端管理 ---------
    // --------- START 策略管理 ---------
    @BuiltInPermission(code = "strategyManage", name = "策略管理",
            parentCode = "CDC",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "500")}),

    @BuiltInPermission(parentCode = "strategyManage", code = "cloudDesktopStrategy", name = "云桌面策略",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "501")}),

    @BuiltInPermission(parentCode = "cloudDesktopStrategy", code = "cloudDesktopStrategySelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "502")}),

    @BuiltInPermission(parentCode = "strategyManage", code = "networkStrategy", name = "网络策略",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "510")}),

    @BuiltInPermission(parentCode = "networkStrategy", code = "networkStrategySelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "511")}),

    // 全局策略 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "strategyManage", code = "globalStrategy", name = "全局策略",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "520")}),

    @BuiltInPermission(parentCode = "strategyManage", code = "usbManage", name = "USB外设管理",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "530")}),

    @BuiltInPermission(parentCode = "usbManage", code = "usbManageSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "531")}),

    @BuiltInPermission(parentCode = "usbManage", code = "usbManageSettings", name = "高级配置",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "535")}),


    // 打印机管理超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "strategyManage", code = "printerManage", name = "打印机管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "540")}),

    // 主题策略 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "strategyManage", code = "themeStrategy", name = "主题策略",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "550")}),

    // 软件管控管理 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "strategyManage", code = "softwareControlManage", name = "软件管控管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "560"),
                    @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH_KEY, value = Constants.ENABLE_SOFTWARE_STRATEGY)}),

    @BuiltInPermission(parentCode = "softwareControlManage", code = "softwareManage", name = "软件库管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "561"),
                    @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH_KEY, value = Constants.ENABLE_SOFTWARE_STRATEGY)}),

    @BuiltInPermission(parentCode = "softwareControlManage", code = "softwareStrategy", name = "软件管控策略",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "562"),
                    @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.GLOBAL_ENABLE_SEARCH_KEY, value = Constants.ENABLE_SOFTWARE_STRATEGY)}),

    @BuiltInPermission(parentCode = "strategyManage", code = "userProfileManage", name = "个性化配置管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "570")}),

    @BuiltInPermission(parentCode = "userProfileManage", code = "userProfilePathManage", name = "配置库管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "571")}),

    @BuiltInPermission(parentCode = "userProfileManage", code = "userProfileStrategy", name = "个性化配置策略",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ORDER, value = "572")}),


    // --------- END 策略管理 ---------
    // --------- START 系统配置 ---------
    // 系统配置 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(code = "systemSetting", name = "系统设置",
            parentCode = "CDC",
            roles = {SECADMIN,AUDADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "600")}),

    // 高级设置 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "systemSetting", code = "advancedConfig", name = "高级设置",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "602")}),
        

    // --------- END 系统配置 ---------
    // --------- START 系统运维 ---------
    @BuiltInPermission(code = "systemMaintenance", name = "系统运维",
            parentCode = "CDC",
            roles = {AUDADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_AUD_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "700")}),

    // 告警监控超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "systemMaintenance", code = "alarmMonitor", name = "告警监控",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "701")}),

    @BuiltInPermission(parentCode = "alarmMonitor", code = "alarmLog", name = "告警列表",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "702"),
                    @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES)}),


    // --------- START 升级管理 ---------
    // 告警监控 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "CDC", code = "updateManage", name = "升级管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "712")}),

    // 升级管理 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    // 终端升级
    @BuiltInPermission(parentCode = "updateManage", code = "terminalUpgrade", name = "终端升级",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "710")}),

    // 终端系统
    @BuiltInPermission(parentCode = "terminalUpgrade", code = "terminalSystem", name = "终端系统",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "711")}),

    // 终端组件
    @BuiltInPermission(parentCode = "terminalUpgrade", code = "terminalUpgradeWithSoloComp", name = "终端组件",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "712"),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES)}),

    // 客户端升级
    @BuiltInPermission(parentCode = "updateManage", code = "clientUpgrade", name = "客户端升级",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "914")}),

    // 升级包管理
    @BuiltInPermission(parentCode = "clientUpgrade", code = "upgradePacketManage", name = "升级包管理",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "912")}),
    // 升级记录
    @BuiltInPermission(parentCode = "clientUpgrade", code = "upgradePacketRecord", name = "升级记录",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "913")}),
    // --------- START 升级管理 ---------

    // 服务器数据备份超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "systemMaintenance", code = "serverBackupRecovery", name = "云办公数据备份",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "720")}),

    // 审计日志 必选项目 ENABLE_NEED
    @BuiltInPermission(parentCode = "systemMaintenance", code = "auditLogs", name = "审计日志",
            roles = {AUDADMIN, SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_AUD_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "730")}),

    @BuiltInPermission(parentCode = "auditLogs", code = "auditLogsSelect", name = "查看",
            roles = {AUDADMIN, SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_AUD_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "731")}),
    // 定时任务超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
    @BuiltInPermission(parentCode = "systemMaintenance", code = "timedTask", name = "定时任务",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "740")}),

    @BuiltInPermission(parentCode = "systemMaintenance", code = "maintenanceTools", name = "维护调试",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "750")}),

    @BuiltInPermission(parentCode = "systemMaintenance", code = "customInfo", name = "客户信息",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "760")}),



    // 由于多个功能对应一个URI冲突， 冲突的列入到这边 或者无实际用处 只是给前端显示的
    @BuiltInPermission(parentCode = "imageTemplate", code = "downloadGoldenImage", name = "下载黄金镜像",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "203")}),

    // --------- START 云应用管理 ---------
    @BuiltInPermission(code = "unifiedAppMng", name = "应用中心",
            roles = {SYSADMIN},
            parentCode = "CDC",
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "770")}),
    @BuiltInPermission(parentCode = "unifiedAppMng", code = "appCenter", name = "应用中心",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "771")}),
    @BuiltInPermission(parentCode = "unifiedAppMng", code = "desktopApp", name = "桌面应用",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "773")}),
    @BuiltInPermission(parentCode = "desktopApp", code = "desktopAppSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "774")}),
    @BuiltInPermission(parentCode = "desktopApp", code = "appRepo", name = "应用仓库",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "776")}),
    @BuiltInPermission(parentCode = "appRepo", code = "appRepoSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "777")}),
    @BuiltInPermission(parentCode = "desktopApp", code = "appTest", name = "应用测试",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "778")}),
    @BuiltInPermission(parentCode = "appTest", code = "appTestSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "779")}),
    @BuiltInPermission(parentCode = "desktopApp", code = "appDelivery", name = "应用交付",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "780")}),
    @BuiltInPermission(parentCode = "appDelivery", code = "appDeliverySelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "781")}),

    // 云应用 start
    // 云应用 end


    @BuiltInPermission(parentCode = "unifiedAppMng", code = "assist", name = "辅助功能",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "833")}),
    @BuiltInPermission(parentCode = "assist", code = "assistSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "835")}),
    @BuiltInPermission(parentCode = "assist", code = "fileDistribution", name = "文件分发",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "836")}),
    @BuiltInPermission(parentCode = "fileDistribution", code = "fileDistributionSelect", name = "查看",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                    @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "837")}),
// --------- END 应用中心 ---------

// --------- START 监控平台 ---------
    @BuiltInPermission(code = "largeScreen", name = "监控平台",
            parentCode = "CDC",
            roles = {SYSADMIN},
            tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value =
                    FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "1000")}),
// --------- END 监控平台 ---------


})

public class MiniMenuController {
}
