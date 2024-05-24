package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.menu.vdi;


import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermission;
import com.ruijie.rcos.gss.base.iac.module.annotation.BuiltInPermissions;
import com.ruijie.rcos.gss.base.iac.module.annotation.PermissionTag;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.ruijie.rcos.gss.base.iac.module.enums.SystemDefaultRole.*;

/**
 * Description: GSS 菜单
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/7
 *
 * @author jarman
 */
@Api(tags = "VDI GSS菜单信息")
@Controller
@RequestMapping("/rco/menu")
@BuiltInPermissions({

        @BuiltInPermission(parentCode = "CDC", code = "iacInit", name = "身份认证",
                roles = {SECADMIN,SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_AUD_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "301")}),

        // --------- START 管理员管理 菜单 ---------

        @BuiltInPermission(parentCode = "iacInit", code = "systemUserManage", name = "管理员管理",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6100")}),

        @BuiltInPermission(
                code = "listSystemUser",
                name = "查看",
                parentCode = "systemUserManage",
                uri = "/iac/admin/list",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6101")
                }
        ),

        @BuiltInPermission(
                code = "createSystemUser",
                name = "创建",
                parentCode = "systemUserManage",
                uri = "/iac/admin/create",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6102")
                }
        ),

        @BuiltInPermission(
                code = "editSystemUser",
                name = "编辑基本信息",
                parentCode = "systemUserManage",
                uri = "/iac/admin/edit",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6103")
                }
        ),

        @BuiltInPermission(
                code = "deleteSystemUser",
                name = "注销",
                parentCode = "systemUserManage",
                uri = "/iac/admin/delete",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6104")
                }
        ),

        @BuiltInPermission(
                code = "enableSystemUser",
                name = "启用",
                parentCode = "systemUserManage",
                uri = "/iac/admin/enableAdmin",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6105")
                }
        ),

        @BuiltInPermission(
                code = "disableSystemUser",
                name = "禁用",
                parentCode = "systemUserManage",
                uri = "/iac/admin/disableAdmin",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6106")
                }
        ),

        @BuiltInPermission(
                code = "unLockSystemUser",
                name = "解除登录限制",
                parentCode = "systemUserManage",
                uri = "/iac/admin/unLock",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6107")
                }
        ),

        @BuiltInPermission(
                code = "resetAdminPassword",
                name = "重置密码",
                parentCode = "systemUserManage",
                uri = "/iac/admin/modifyOtherAdminPwd",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6108")
                }
        ),
        @BuiltInPermission(
                code = "editSystemUserPermission",
                name = "编辑管理员数据权限",
                parentCode = "systemUserManage",
                uri = "/rco/admin/dataPermission/edit",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6109")
                }
        ),


        // --------- END 管理员管理 菜单 ---------


        // --------- START 角色/权限管理 菜单 ---------
        @BuiltInPermission(parentCode = "iacInit", code = "systemRoleManage", name = "角色/权限管理",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6200")}),

        @BuiltInPermission(
                code = "listSystemRole",
                name = "查看",
                parentCode = "systemRoleManage",
                uri = "/iac/role/list",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "121"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),

        @BuiltInPermission(
                code = "createSystemRole",
                name = "创建",
                parentCode = "systemRoleManage",
                uri = "/iac/role/create",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "122")
                }
        ),

        @BuiltInPermission(
                code = "editSystemRole",
                name = "编辑",
                parentCode = "systemRoleManage",
                uri = "/iac/role/edit",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "123")
                }
        ),

        @BuiltInPermission(
                code = "deleteSystemRole",
                name = "删除",
                parentCode = "systemRoleManage",
                uri = "/iac/role/delete",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "124")
                }
        ),


        // --------- END 角色/权限管理 菜单 ---------

        // --------- START 身份中心管理 菜单 ---------
        @BuiltInPermission(parentCode = "iacInit", code = "iacManage", name = "身份中心管理",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6300")}),

        @BuiltInPermission(parentCode = "iacManage", code = "adManage", name = "AD域管理",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6310")}),


        @BuiltInPermission(parentCode = "adManage", code = "adManageConnection", name = "AD域对接",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6320")}),

        @BuiltInPermission(parentCode = "adManage", code = "adGroupManage", name = "安全组管理",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6330")}),


        @BuiltInPermission(
                code = "listadGroupManage",
                name = "查看",
                uri = "/iac/adGroup/list",
                parentCode = "adGroupManage",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ORDER, value = "6331"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),
        @BuiltInPermission(
                code = "createadGroupManage",
                name = "添加",
                uri = "/iac/adGroup/batchCreate",
                parentCode = "adGroupManage",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "6332"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES)
                }
        ),
        @BuiltInPermission(
                code = "deleteadGroupManage",
                name = "删除",
                uri = "/iac/adGroup/delete",
                parentCode = "adGroupManage",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "6333"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES)
                }
        ),
        @BuiltInPermission(
                code = "syncadGroupManage",
                name = "同步",
                uri = "/iac/adGroup/syncAdGroup",
                parentCode = "adGroupManage",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "6334"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES)
                }
        ),

        // LDAP
        @BuiltInPermission(parentCode = "iacManage", code = "ldapManage", name = "LDAP对接",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6410")}),


        // --------- END 身份中管理 菜单 ---------


        // --------- END 认证方式配置 菜单 ---------
        @BuiltInPermission(parentCode = "iacInit", code = "authTypeManage", name = "认证方式配置",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6400")}),

        @BuiltInPermission(parentCode = "authTypeManage",
                code = "thirdPartyQrAuth",
                name = "扫码认证",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6401")}),

        @BuiltInPermission(parentCode = "thirdPartyQrAuth",
                code = "detailThirdPartyQrAuth",
                name = "查看",
                uri = {"/iac/thirdPartyQrAuth/getWorkWexinConfig",
                        "/iac/thirdPartyQrAuth/getFeishuConfig",
                        "/iac/thirdPartyQrAuth/getDingdingConfig"},
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6402")}),


        @BuiltInPermission(parentCode = "thirdPartyQrAuth",
                code = "editThirdPartyQrAuth",
                name = "编辑",
                uri = {"/iac/thirdPartyQrAuth/editWorkWexinConfig",
                        "/iac/thirdPartyQrAuth/editFeishuConfig",
                        "/iac/thirdPartyQrAuth/editDingdingConfig"},
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6403")}),

        @BuiltInPermission(parentCode = "authTypeManage",
                code = "smsCertification",
                name = "短信认证",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6600")}),

        @BuiltInPermission(parentCode = "smsCertification", code = "detailSmsCertification", name = "查看",
                uri = "/iac/smsCertification/detail",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6601")}),

        @BuiltInPermission(parentCode = "smsCertification", code = "editSmsCertification", name = "编辑",
                uri = "/rco/smsCertification/edit",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6602")}),

        @BuiltInPermission(parentCode = "smsCertification", code = "testSmsCertification", name = "测试短信认证",
                uri = "/iac/smsCertification/test",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6603")}),

        @BuiltInPermission(parentCode = "authTypeManage", code = "mfaAuthManage", name = "锐捷动态口令",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6700")}),

        @BuiltInPermission(parentCode = "mfaAuthManage", code = "listMfaAuthManage", name = "查看",
                uri = "/rco/otpCertification/detail",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6701")}),

        @BuiltInPermission(parentCode = "mfaAuthManage", code = "editMfaAuthManage", name = "编辑",
                uri = "/rco/otpCertification/edit",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6702")}),
// 企金2.0版本先注释掉，后续版本实现
//        @BuiltInPermission(parentCode = "authTypeManage",
//                code = "rjQrCodeAuthManage",
//                name = "锐捷客户端扫码",
//                roles = {SYSADMIN},
//                tags = {
//                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
//                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
//                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
//                        @PermissionTag(key = FunTypes.ORDER, value = "6500")}),
//
//        @BuiltInPermission(parentCode = "rjQrCodeAuthManage", code = "getQrCodeStrategy", name = "查看",
//                uri = "/iac/authStrategy/getQrCodeStrategy",
//                roles = {SYSADMIN},
//                tags = {
//                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
//                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
//                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
//                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
//                        @PermissionTag(key = FunTypes.ORDER, value = "6501")}),
//
//        @BuiltInPermission(parentCode = "rjQrCodeAuthManage", code = "editQrCodeStrategy", name = "编辑",
//                uri = "/iac/authStrategy/editQrCodeStrategy",
//                roles = {SYSADMIN},
//                tags = {
//                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
//                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
//                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
//                        @PermissionTag(key = FunTypes.ORDER, value = "6502")}),


        @BuiltInPermission(parentCode = "authTypeManage",
                code = "thirdPartyCertificationConfig",
                name = "Radius动态口令",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6800")}),

        @BuiltInPermission(parentCode = "thirdPartyCertificationConfig", code = "detailThirdPartyCertificationConfig", name = "查看",
                uri = "/iac/thirdPartyCertificationConfig/detail",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6801")}),

        @BuiltInPermission(parentCode = "thirdPartyCertificationConfig", code = "editThirdPartyCertificationConfig", name = "编辑",
                uri = "/iac/thirdPartyCertificationConfig/edit",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6802")}),

        @BuiltInPermission(parentCode = "thirdPartyCertificationConfig", code = "testThirdPartyCertificationConfig", name = "测试连通性",
                uri = "/iac/thirdPartyCertificationConfig/testConnection",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6803")}),

        @BuiltInPermission(parentCode = "authTypeManage",
                code = "hardwareCertification",
                name = "VDI硬件特征码",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6900")}),

        @BuiltInPermission(parentCode = "hardwareCertification", code = "detailHardwareCertification", name = "查看",
                uri = "/iac/hardwareCertification/detail",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6901")}),

        @BuiltInPermission(parentCode = "hardwareCertification", code = "edithardwareCertification", name = "编辑",
                uri = "/rco/hardwareCertification/edit",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "6902")}),

        @BuiltInPermission(parentCode = "authTypeManage",
                code = "oAuth2Config",
                name = "OAUTH2.0认证",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7200")}),


        @BuiltInPermission(parentCode = "oAuth2Config", code = "getOAuth2Config", name = "查看",
                uri = "/iac/thirdPartyQrAuth/getThirdPartyAuthConfig",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7201")}),

        @BuiltInPermission(parentCode = "oAuth2Config", code = "editOAuth2Config", name = "编辑",
                uri = "/iac/thirdPartyQrAuth/editThirdPartyAuthConfig",
                roles = {SYSADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_SYS_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7202")}),



        // --------- END 认证方式配置 菜单 ---------

        // --------- START 身份安全策略 菜单 ---------
        @BuiltInPermission(parentCode = "iacInit", code = "authSecurityManage", name = "身份安全策略",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7300")}),
        @BuiltInPermission(
                code = "accountSecurityManage",
                name = "账号锁定策略",
                parentCode = "authSecurityManage",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7300")
                }
        ),

        @BuiltInPermission(
                code = "listAccountSecurityManage",
                name = "查看",
                parentCode = "accountSecurityManage",
                uri = {"/iac/strategy/getAccountStrategy","/iac/captchaStrategy/detail"},
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "7301"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),

        @BuiltInPermission(
                code = "editAccountSecurityManage",
                name = "编辑",
                parentCode = "accountSecurityManage",
                uri = {"/iac/strategy/setAccountStrategy","/iac/captchaStrategy/edit"},
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7302")
                }
        ),


        @BuiltInPermission(
                code = "passwordSecurityManage",
                name = "密码安全策略",
                parentCode = "authSecurityManage",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7400")
                }
        ),
        @BuiltInPermission(
                code = "listPasswordSecurityManage",
                name = "查看",
                parentCode = "passwordSecurityManage",
                uri = "/iac/strategy/getPasswordStrategy",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "7401"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),

        @BuiltInPermission(
                code = "editPasswordSecurityManage",
                name = "编辑",
                parentCode = "passwordSecurityManage",
                uri = "/iac/strategy/setPasswordStrategy",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7402")
                }
        ),


        @BuiltInPermission(
                code = "weekPasswordSecurityManage",
                name = "弱密码策略",
                parentCode = "authSecurityManage",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7500")
                }
        ),

        @BuiltInPermission(
                code = "listWeekPwdSecurityManage",
                name = "查看",
                parentCode = "weekPasswordSecurityManage",
                uri = "/iac/strategy/getPasswordBlacklistPage",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "7501"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),

        @BuiltInPermission(
                code = "editWeekPwdSecurityManage",
                name = "编辑",
                parentCode = "weekPasswordSecurityManage",
                uri = "/iac/strategy/updatePasswordBlacklistStrategy",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7502")
                }
        ),

        @BuiltInPermission(
                code = "deleteWeekPwdSecurityManage",
                name = "删除",
                parentCode = "weekPasswordSecurityManage",
                uri = "/iac/strategy/deletePasswordBlacklist",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7503")
                }
        ),



        @BuiltInPermission(
                code = "loginIpSecurityManage",
                name = "登录IP限制策略",
                parentCode = "authSecurityManage",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7600")
                }
        ),
        @BuiltInPermission(
                code = "listIpSecurityManage",
                name = "查看",
                parentCode = "loginIpSecurityManage",
                uri = "/iac/strategy/getIpWhitelistStrategy",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "7601"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),

        @BuiltInPermission(
                code = "editIpSecurityManage",
                name = "编辑",
                parentCode = "loginIpSecurityManage",
                uri = "/iac/strategy/updateIpWhitelistStrategy",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7602")
                }
        ),

        @BuiltInPermission(
                code = "clientStrategy",
                name = "客户端安全策略",
                parentCode = "authSecurityManage",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7700")
                }
        ),

        @BuiltInPermission(
                code = "detailClientStrategy",
                name = "查看",
                parentCode = "clientStrategy",
                uri = "/rco/certifiedSecurity/clientDetail",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "7701"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),

        @BuiltInPermission(
                code = "editClientStrategy",
                name = "编辑",
                parentCode = "clientStrategy",
                uri = "/iac/strategy/edit",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7702")
                }
        ),



        @BuiltInPermission(
                code = "globalAuthConfig",
                name = "全局策略",
                parentCode = "authSecurityManage",
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.MENU),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7800")
                }
        ),

        @BuiltInPermission(
                code = "getAuthConfig",
                name = "查看",
                parentCode = "globalAuthConfig",
                uri = {"/iac/globalConfig/getAuthConfig","/iac/smsCertification/pwdRecover/detail"},
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ORDER, value = "7801"),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_LOOK, value = FunTypes.YES)
                }
        ),

        @BuiltInPermission(
                code = "editAuthConfig",
                name = "编辑",
                parentCode = "globalAuthConfig",
                uri = {"/iac/globalConfig/editAuthConfig","/iac/smsCertification/pwdRecover/edit"},
                roles = {SECADMIN},
                tags = {
                        @PermissionTag(key = FunTypes.FUNTYPE, value = FunTypes.FUN),
                        @PermissionTag(key = FunTypes.ENABLE_INNER_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ENABLE_SEC_ADMIN, value = FunTypes.YES),
                        @PermissionTag(key = FunTypes.ORDER, value = "7802")
                }
        ),


        // --------- END 身份安全策略 菜单 ---------


        // --------- END GSS 菜单 ---------


})
public class VDIGssMenuController {
}
