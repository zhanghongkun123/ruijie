package com.ruijie.rcos.rcdc.rco.module.impl.version;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacUpdateRoleRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.tool.IacMigratePermissionTool;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.AdminMgmtServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.vc.VersionedProcessor;

/**
 * Description:5.4版本升级 数据库升级
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/18 20:34
 *
 * @author linrenjian
 */
@Service
public class RcoVersionProcessor implements VersionedProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoVersionProcessor.class);

    @Autowired
    private IacMigratePermissionTool permissionTool;

    @Autowired
    private PermissionMgmtAPI permissionMgmtAPI;

    @Autowired
    private AdminMgmtServiceTx adminMgmtServiceTx;

    @Override
    public String version() {
        return "5.4.0";
    }

    @Override
    public void process() {

        List<IacRoleDTO> baseRoleDTOList = permissionTool.getCandidateRoles(SubSystem.CDC);
        // 批量更新角色请求列表
        List<IacUpdateRoleRequest> baseUpdateRoleRequestList = new ArrayList<>();
        try {
            // 候选角色过滤 超级管理员
            for (IacRoleDTO baseRoleDTO : baseRoleDTOList) {
                LOGGER.info("当前处理的角色信息:{}", JSON.toJSONString(baseRoleDTO));
                // 不修改超级管理员 补齐内置角色信息 并且权限集合不为空
                if (!RoleType.ADMIN.getName().equals(baseRoleDTO.getRoleName()) && ArrayUtils.isNotEmpty(baseRoleDTO.getPermissionIdArr())) {
                    List<IacPermissionDTO> basePermissionDTOList =
                            permissionMgmtAPI.listPermissionByIdArrAndServerModel(baseRoleDTO.getPermissionIdArr());
                    // 过滤掉超级管理员的权限
                    LOGGER.info(" 未过滤的权限basePermissionDTOList:{}", JSONObject.toJSONString(basePermissionDTOList));

                    List<UUID> uuidList = null;
                    // 如果是安全管理员 不需要进行过滤 直接赋值
                    if (RoleType.SECADMIN.getName().equals(baseRoleDTO.getRoleName())) {
                        LOGGER.info("当前处理是安全管理员角色");
                        uuidList = basePermissionDTOList.stream().filter(basePermissionDTO -> basePermissionDTO.getTags() != null)
                                .map(IacPermissionDTO::getId).collect(Collectors.toList());
                    } else if (RoleType.SYSADMIN.getName().equals(baseRoleDTO.getRoleName())) {
                        LOGGER.info("当前处理是系统管理员角色");
                        uuidList = buildSysadminBasePermissionDTOList(basePermissionDTOList);
                    } else {
                        uuidList = basePermissionDTOList.stream()
                                .filter(basePermissionDTO -> basePermissionDTO.getTags() != null
                                        && !FunTypes.YES.equals(((JSONObject) basePermissionDTO.getTags()).getString(FunTypes.ENABLE_SUPER_ADMIN)))
                                .map(IacPermissionDTO::getId).collect(Collectors.toList());
                    }

                    LOGGER.info("过滤后的权限basePermissionDTOStream:{}", JSONObject.toJSONString(uuidList));
                    // 设置权限
                    baseRoleDTO.setPermissionIdArr(uuidList.toArray(new UUID[0]));
                    IacUpdateRoleRequest baseUpdateRoleRequest = new IacUpdateRoleRequest();
                    // 将更新修正的角色信息
                    BeanUtils.copyProperties(baseRoleDTO, baseUpdateRoleRequest);
                    // 强制修改默认角色
                    baseUpdateRoleRequest.setForceDefault(Boolean.TRUE);
                    baseUpdateRoleRequestList.add(baseUpdateRoleRequest);
                }
            }
            // 需要更新的角色不为空 进行更新
            if (CollectionUtils.isNotEmpty(baseUpdateRoleRequestList)) {
                adminMgmtServiceTx.updateRoleRequestList(baseUpdateRoleRequestList);
            }

        } catch (BusinessException e) {
            LOGGER.error("5.4升级时，菜单自动补齐过滤失败", e);
        }
    }


    /**
     * 由于安全管理员 只有四个权限菜单 系统配置 系统管理员 系统管理员 角色管理 这些又是超级管理员专属 不允许其他管理员勾选 需要进行特性判断
     * 
     * @param basePermissionDTOList
     * @return
     */
    private boolean enableSecadmin(List<IacPermissionDTO> basePermissionDTOList) {
        // 只要有一个系统设置权限 就是安全管理员
        return basePermissionDTOList.stream()
                .anyMatch(basePermissionDTO -> MenuType.SYSTEM_USER.getMenuName().equals(basePermissionDTO.getPermissionCode()));
    }


    /**
     * 
     * @param basePermissionDTOList 构建系统管理员权限列表
     * @return UUID
     */
    private List<UUID> buildSysadminBasePermissionDTOList(List<IacPermissionDTO> basePermissionDTOList) {
        // 不赋值超级管理员菜单 但是要赋值赋值定时任务 告警监控 告警列表等菜单 如果有打 ENABLE_SYS_ADMIN FunTypes.YES 说明可以加入权限
        return basePermissionDTOList.stream()
                .filter(basePermissionDTO -> basePermissionDTO.getTags() != null
                        && (!FunTypes.YES.equals(((JSONObject) basePermissionDTO.getTags()).getString(FunTypes.ENABLE_SUPER_ADMIN))
                                || FunTypes.YES.equals(((JSONObject) basePermissionDTO.getTags()).getString(FunTypes.ENABLE_SYS_ADMIN))))
                .map(IacPermissionDTO::getId).collect(Collectors.toList());

    }
}
