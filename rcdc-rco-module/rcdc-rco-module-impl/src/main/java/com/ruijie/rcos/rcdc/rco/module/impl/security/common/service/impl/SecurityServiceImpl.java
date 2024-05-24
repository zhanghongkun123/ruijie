package com.ruijie.rcos.rcdc.rco.module.impl.security.common.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.role.IacGetRolePageRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.SecurityConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.util.SecurityUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyAuditLogStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.constants.AuditFileConstants;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.service.SecurityService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月02日
 *
 * @author lihengjing
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);

    private static final Integer FIRST_AUDITOR_LEVEL = 1;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private IacPermissionMgmtAPI basePermissionMgmtAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private CbbClusterServerMgmtAPI clusterServerMgmtAPI;


    @Override
    public List<AuditApplyAuditLogDTO> generateAuditLogByApply(AuditApplyDetailDTO auditApplyDetailDTO) {
        Assert.notNull(auditApplyDetailDTO, "applyId not be null");
        UUID applyId = auditApplyDetailDTO.getId();
        Assert.notNull(applyId, "applyId not be null");

        IacRoleDTO roleDTO = null;
        try {
            roleDTO = baseRoleMgmtAPI.getRoleByRoleName(RoleType.ADMIN.getName(), SubSystem.CDC);

            List<IacPermissionDTO> basePermissionList = basePermissionMgmtAPI.listAllPermissionBySubSystem(SubSystem.CDC);
            List<IacPermissionDTO> auditFileApplyHandlePermissionList = basePermissionList.stream().filter(
                basePermissionDTO -> AuditFileConstants.AUDIT_FILE_APPLY_HANDLE_PERMISSION_CODE.equals(basePermissionDTO.getPermissionCode()))
                .collect(Collectors.toList());
            if (!auditFileApplyHandlePermissionList.isEmpty()) {
                IacGetRolePageRequest rolePageRequest = new IacGetRolePageRequest();
                rolePageRequest.setPage(0);
                rolePageRequest.setLimit(Integer.MAX_VALUE);
                rolePageRequest.setHasDefault(false);
                rolePageRequest.setSubSystem(SubSystem.CDC);
                DefaultPageResponse<IacRoleDTO> rolePage = baseRoleMgmtAPI.getRolePage(rolePageRequest);
                if (rolePage.getTotal() > 0) {
                    List<IacRoleDTO> applyHandleRoleList = Arrays.stream(rolePage.getItemArr())
                            .filter(baseRoleDTO -> Arrays.stream(baseRoleDTO.getPermissionIdArr())
                                    .anyMatch(permissionId -> auditFileApplyHandlePermissionList.get(0).getId().equals(permissionId)))
                            .collect(Collectors.toList());

                    if (!applyHandleRoleList.isEmpty()) {
                        roleDTO = applyHandleRoleList.get(0);
                    }
                }
            }
        } catch (BusinessException e) {
            LOGGER.error("审批流程获取审批角色信息过程中出错", e);
        }

        AuditApplyAuditLogDTO auditApplyAuditLogDTO = new AuditApplyAuditLogDTO();
        auditApplyAuditLogDTO.setId(UUID.randomUUID());
        auditApplyAuditLogDTO.setApplyId(applyId);
        auditApplyAuditLogDTO.setAuditorLevel(FIRST_AUDITOR_LEVEL);
        auditApplyAuditLogDTO.setIsLastAuditor(true);
        auditApplyAuditLogDTO.setAuditorState(AuditApplyAuditLogStateEnum.PENDING_APPROVAL);
        auditApplyAuditLogDTO.setCreateTime(new Date());
        if (roleDTO != null) {
            auditApplyAuditLogDTO.setRoleId(roleDTO.getId());
            auditApplyAuditLogDTO.setRoleName(roleDTO.getRoleName());
        }
        return Collections.singletonList(auditApplyAuditLogDTO);
    }

    @Override
    public FtpConfigDTO obtainAuditFileEncryptFtpInfo() {
        FtpConfigDTO ftpConfigDTO = obtainAuditFileFtpInfo();
        CbbClusterVirtualIpDTO clusterVirtualIpDTO = null;
        try {
            clusterVirtualIpDTO = clusterServerMgmtAPI.getClusterVirtualIp();
        } catch (BusinessException e) {
            LOGGER.error("获取文件导出审计全局策略FTP信息时，获取VIP失败", e);
        }
        if (clusterVirtualIpDTO != null) {
            ftpConfigDTO.setFtpServerIP(clusterVirtualIpDTO.getClusterVirtualIp());
        }
        ftpConfigDTO.setFtpUserName(SecurityUtils.encryptXOR(ftpConfigDTO.getFtpUserName(), RedLineUtil.getAuditFtpRedLine()));
        return ftpConfigDTO;
    }

    @Override
    public FtpConfigDTO obtainAuditFileFtpInfo() {
        String ftpConfigInfo = globalParameterService.findParameter(SecurityConstants.AUDIT_FILE_FTP_CONFIG_KEY);
        return JSONObject.parseObject(ftpConfigInfo, FtpConfigDTO.class);
    }


}
