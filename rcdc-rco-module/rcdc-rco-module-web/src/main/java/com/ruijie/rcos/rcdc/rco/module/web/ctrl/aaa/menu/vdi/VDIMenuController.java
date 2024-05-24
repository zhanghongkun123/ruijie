package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.menu.vdi;


import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.AUDADMIN;
import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.SYSADMIN;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermission;
import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermissions;
import com.ruijie.rcos.gss.base.iac.module.annotation.PermissionTag;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.AUDADMIN;
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
@BuiltInPermissions(value = {

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

        @BuiltInPermission(parentCode = "cloudDesktopManagement", code = "cloudDesktopPool", name = "桌面池管理",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "140")}),

        @BuiltInPermission(parentCode = "cloudDesktopPool", code = "cloudDesktopPoolSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ORDER, value = "141")}),

        @BuiltInPermission(parentCode = "cloudDesktopManagement", code = "cloudDiskPool", name = "磁盘池管理",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "160")}),

        @BuiltInPermission(parentCode = "cloudDiskPool", code = "diskPoolSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ORDER, value = "161")}),

        @BuiltInPermission(parentCode = "cloudDesktopManagement", code = "recycleBin", name = "回收站", roles = {SYSADMIN}, tags = {
                @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL), @PermissionTag(key = FunTypes.ORDER, value = "180")}),

        @BuiltInPermission(parentCode = "recycleBin", code = "recycleBinSelect", name = "查看", roles = {SYSADMIN}, tags = {
                @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL), @PermissionTag(key = FunTypes.ORDER, value = "181")}),


        // --------- END 云桌面管理 ---------
        // --------- START 镜像管理 ---------
        @BuiltInPermission(code = "imageManage", name = "镜像管理",
                parentCode = "CDC",
                roles = {SYSADMIN},
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
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "231")}),

        @BuiltInPermission(parentCode = "imageManage", code = "installationPackage", name = "共享文件空间",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "250")}),

        @BuiltInPermission(parentCode = "installationPackage", code = "installationPackageSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "251")}),

        // 驱动管理超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "imageManage", code = "imageDriverManagement", name = "驱动管理",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "252")}),

        // --------- END 镜像管理 ---------
        // --------- START 用户管理 ---------
        // 用户管理 必选项目 ENABLE_NEED 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "iacInit", code = "userManage", name = "用户管理",
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

        // --------- START 云应用管理 ---------
        @BuiltInPermission(code = "unifiedAppMng", name = "应用中心",
                parentCode = "CDC",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "400")}),
        @BuiltInPermission(parentCode = "unifiedAppMng", code = "appCenter", name = "应用中心",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "401")}),
        @BuiltInPermission(parentCode = "unifiedAppMng", code = "desktopApp", name = "桌面应用",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "402")}),
        @BuiltInPermission(parentCode = "desktopApp", code = "desktopAppSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "404")}),
        @BuiltInPermission(parentCode = "desktopApp", code = "appRepo", name = "应用仓库",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "405")}),
        @BuiltInPermission(parentCode = "appRepo", code = "appRepoSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "406")}),
        @BuiltInPermission(parentCode = "desktopApp", code = "appTest", name = "应用测试",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "407")}),
        @BuiltInPermission(parentCode = "appTest", code = "appTestSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "408")}),
        @BuiltInPermission(parentCode = "desktopApp", code = "appDelivery", name = "应用交付",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "409")}),
        @BuiltInPermission(parentCode = "appDelivery", code = "appDeliverySelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "410")}),


        // 云应用 start
        @BuiltInPermission(parentCode = "unifiedAppMng", code = "appManagement", name = "虚拟应用",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "415")}),

        @BuiltInPermission(parentCode = "appManagement", code = "rcaCloudDesktopManage", name = "应用主机管理",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "416")}),
        @BuiltInPermission(parentCode = "rcaCloudDesktopManage", code = "rcaCloudDesktopManageSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "417")}),

        @BuiltInPermission(parentCode = "appManagement", code = "appPoolManage", name = "应用池管理",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "418")}),
        @BuiltInPermission(parentCode = "appPoolManage", code = "appPoolManageSelect", name = "查看应用池",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "419")}),

        @BuiltInPermission(parentCode = "appManagement", code = "rcaSessionManage", name = "会话管理",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "420")}),
        @BuiltInPermission(parentCode = "rcaSessionManage", code = "rcaSessionManageSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "421")}),
        // 云应用 end


        @BuiltInPermission(parentCode = "unifiedAppMng", code = "assist", name = "辅助功能",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "427")}),
        @BuiltInPermission(parentCode = "assist", code = "assistSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "428")}),
        @BuiltInPermission(parentCode = "assist", code = "fileDistribution", name = "文件分发",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "429")}),
        @BuiltInPermission(parentCode = "fileDistribution", code = "fileDistributionSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "430")}),
// --------- END 应用中心 ---------


        // --------- START 终端管理 ---------
        // 终端管理 必选项目 ENABLE_NEED
        @BuiltInPermission(code = "terminalManage", name = "终端管理",
                parentCode = "CDC",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "440")}),

        @BuiltInPermission(parentCode = "terminalManage", code = "vdiTerminal", name = "瘦终端（VDI）",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "441")}),

        @BuiltInPermission(parentCode = "vdiTerminal", code = "vdiTerminalSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "442")}),

        @BuiltInPermission(parentCode = "vdiTerminal", code = "vdiTerminalCheckHistory", name = "检测历史记录",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ORDER, value = "443")}),

        @BuiltInPermission(parentCode = "vdiTerminal", code = "vdiTerminalGroup", name = "终端组",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "444")}),

        @BuiltInPermission(parentCode = "vdiTerminalGroup", code = "vdiTerminalGroupSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "445")}),

        @BuiltInPermission(parentCode = "terminalManage", code = "idvTerminal", name = "胖终端（IDV/TCI）",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "446")}),
        @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "447")}),

        @BuiltInPermission(parentCode = "idvTerminal", code = "idvTerminalGroup", name = "终端组",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "448")}),

        @BuiltInPermission(parentCode = "idvTerminalGroup", code = "idvTerminalGroupSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "449")}),

        @BuiltInPermission(parentCode = "terminalManage", code = "softTerminal", name = "云办公客户端",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "450")}),

        @BuiltInPermission(parentCode = "softTerminal", code = "softTerminalSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "451")}),

        @BuiltInPermission(parentCode = "softTerminal", code = "softTerminalGroup", name = "终端组",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "460")}),

        @BuiltInPermission(parentCode = "softTerminalGroup", code = "softTerminalGroupSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "461")}),

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


        // 管理超级管理员专属ENABLE_SUPER_ADMIN
        @BuiltInPermission(parentCode = "terminalManage", code = "terminalConfig", name = "终端配置管理", tags = {
                @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ENABLE_DEFAULT_SUPER_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ORDER, value = "491")}),

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

        @BuiltInPermission(parentCode = "strategyManage", code = "desktopTempPermission", name = "临时权限",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "506")}),

        @BuiltInPermission(parentCode = "desktopTempPermission", code = "desktopTempPermissionSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "507")}),


        @BuiltInPermission(parentCode = "strategyManage", code = "appStrategy", name = "云应用策略",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "510")}),

        @BuiltInPermission(parentCode = "appStrategy", code = "appUserStrategy", name = "云应用策略",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "511")}),

        @BuiltInPermission(parentCode = "appUserStrategy", code = "appUserStrategySelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "512")}),

        @BuiltInPermission(parentCode = "appStrategy", code = "rcaUsbStrategy", name = "云应用外设策略",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "513")}),
        @BuiltInPermission(parentCode = "rcaUsbStrategy", code = "rcaUsbStrategySelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "514")}),

        @BuiltInPermission(parentCode = "strategyManage", code = "networkStrategy", name = "网络策略",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "520")}),

        @BuiltInPermission(parentCode = "networkStrategy", code = "networkStrategySelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "521")}),

        // 全局策略 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "strategyManage", code = "globalStrategy", name = "全局策略",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "525")}),

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

        @BuiltInPermission(parentCode = "strategyManage", code = "softTerminalGlobalConfig", name = "客户端全局配置",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "590")}),


        // --------- END 策略管理 ---------
        // --------- START 系统配置 ---------
        // 系统配置 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(code = "systemSetting", name = "系统设置",
                parentCode = "CDC",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "600")}),

        // 高级设置 超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "systemSetting", code = "advancedConfig", name = "高级设置",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "603")}),

        // CMS管理超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "systemSetting", code = "cmsManage", name = "CMS管理", tags = {
                @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ENABLE_DEFAULT_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "601")}),

        @BuiltInPermission(parentCode = "systemSetting", code = "uwsManage", name = "UWS管理", tags = {
                @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                @PermissionTag(key = FunTypes.ENABLE_DEFAULT_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "602")}),


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

        @BuiltInPermission(parentCode = "systemMaintenance", code = "systemDashboard", name = "系统报表",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "704")}),

        @BuiltInPermission(parentCode = "systemDashboard", code = "desktopLicenseStat", name = "授权统计报表",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "708")}),


        @BuiltInPermission(parentCode = "systemDashboard", code = "userUseInfo", name = "用户使用信息",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "709")}),


        @BuiltInPermission(parentCode = "systemDashboard", code = "desktopUseStaticReport", name = "云桌面使用信息",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "713")}),

        // 审批中心
        @BuiltInPermission(parentCode = "systemMaintenance", code = "dataSecurityManage", name = "审批中心",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "720")}),

        @BuiltInPermission(parentCode = "dataSecurityManage", code = "auditFileManage", name = "文件导出审批",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "725")}),

        // 云平台管理
        @BuiltInPermission(parentCode = "systemMaintenance", code = "platformManage", name = "云平台管理",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "730")}),

        @BuiltInPermission(parentCode = "platformManage", code = "platformManageSelect", name = "查看",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "731")}),

        // 服务器数据备份超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "systemMaintenance", code = "serverBackupRecovery", name = "云办公数据备份",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "735")}),
        // 服务器数据备份超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "systemMaintenance", code = "cloudPlatformRecovery", name = "云平台数据恢复",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "736")}),

        // 审计日志 必选项目 ENABLE_NEED
        @BuiltInPermission(parentCode = "systemMaintenance", code = "auditLogs", name = "审计日志",
                roles = {AUDADMIN, SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_AUD_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "740")}),

        @BuiltInPermission(parentCode = "auditLogs", code = "auditLogsSelect", name = "查看",
                roles = {AUDADMIN, SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_AUD_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_RCDC_NEED, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_NEED, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "741")}),
        // 定时任务超级管理员专属ENABLE_SUPER_ADMIN 不属于高级系统管理员NO_ENABLE_SYS_ADMIN
        @BuiltInPermission(parentCode = "systemMaintenance", code = "timedTask", name = "定时任务",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "745")}),

        @BuiltInPermission(parentCode = "systemMaintenance", code = "maintenanceTools", name = "维护调试",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.ORDER, value = "750")}),


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


        // 由于多个功能对应一个URI冲突， 冲突的列入到这边 或者无实际用处 只是给前端显示的
        @BuiltInPermission(parentCode = "imageTemplate", code = "downloadGoldenImage", name = "下载黄金镜像",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "203")}),



// --------- START 监控平台 ---------
        @BuiltInPermission(code = "largeScreen", name = "监控平台",
                parentCode = "CDC",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.CASCADING_SELECTION, value = "dashboard"),
                        @PermissionTag(key = FunTypes.ORDER, value = "1000")}),
// --------- END 监控平台 ---------


        //外置存储管理
        @BuiltInPermission(parentCode = "systemMaintenance", code = "externalStorageManage", name = "文件服务器",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU), @PermissionTag(key = FunTypes.FREE_DOM, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "950")}),

        @BuiltInPermission(parentCode = "externalStorageManage", code = "externalStorageSelect", name = "查看",
                roles = {SYSADMIN},
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN), @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.FREE_DOM_END, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.VDI_SERVER_MODEL, value = FunTypes.VDI_SERVER_MODEL),
                        @PermissionTag(key = FunTypes.ORDER, value = "951")}),

        //桌面恢复
        @BuiltInPermission(parentCode = "systemMaintenance", code = "cloudDesktopRecover", name = "桌面恢复",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.NO_ENABLE_SYS_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "770")}),

        // 客户信息
        @BuiltInPermission(parentCode = "systemMaintenance", code = "customInfo", name = "客户信息",
                tags = {@PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SUPER_ADMIN, value = FunTypes.YES), @PermissionTag(key = FunTypes.ORDER, value = "960")}),

})

public class VDIMenuController {
}
