package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateEditDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: VDI编辑镜像辅助类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/5 11:50
 *
 * @author zhangyichi
 */
@Service
public class VDIEditImageHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(VDIEditImageHelper.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    /**
     * 检查并获取镜像编辑信息（假定镜像正在被编辑）
     * @param imageTemplateId 镜像ID
     * @param adminId 管理员ID
     * @param terminalId 终端ID
     * @return 镜像编辑信息
     */
    public CbbImageTemplateEditingInfoDTO getImageEditingInfoMustExist(UUID imageTemplateId, UUID adminId, String terminalId) {
        Assert.notNull(imageTemplateId, "imageTemplateId cannot be null!");
        Assert.notNull(adminId, "adminId cannot be null!");
        Assert.hasText(terminalId, "terminalId cannot be null!");

        CbbImageTemplateEditingInfoDTO imageTemplateEditingInfoDTO;
        try {
            imageTemplateEditingInfoDTO = getImageEditingInfoIfPresent(imageTemplateId, adminId, terminalId);

            return imageTemplateEditingInfoDTO;
        } catch (BusinessException e) {
            LOGGER.error("当前管理员不可操作目标镜像", e);
            // 镜像为处于编辑状态，返回空
            return null;
        }
    }

    /**
     * 检查是否有其它管理员编辑该镜像
     * @param imageTemplateId 镜像ID
     * @param adminId 管理员ID
     * @param terminalId 终端ID
     * @return 编辑信息
     * @throws BusinessException 异常
     */
    public CbbImageTemplateEditingInfoDTO getImageEditingInfoIfPresent(UUID imageTemplateId, UUID adminId, String terminalId)
            throws BusinessException {
        Assert.notNull(imageTemplateId, "imageTemplateId cannot be null!");
        Assert.notNull(adminId, "adminId cannot be null!");
        Assert.hasText(terminalId, "terminalId cannot be null!");

        LOGGER.debug("检查是否有其它管理员编辑镜像，镜像id[{}]，管理员id[{}]，终端id[{}]", imageTemplateId, adminId, terminalId);
        CbbImageTemplateEditDTO apiRequest = new CbbImageTemplateEditDTO();
        apiRequest.setImageTemplateId(imageTemplateId);
        apiRequest.setAdminId(adminId);
        apiRequest.setTerminalId(terminalId);
        return cbbImageTemplateMgmtAPI.checkImageTemplateEditing(apiRequest);
    }

    /**
     * 查询必要信息，记录操作日志
     * @param key 日志国际化key
     * @param imageId 镜像id
     * @param adminId 管理员id
     * @param terminalId 终端id
     */
    public void recordLog(String key, UUID adminId, UUID imageId, String terminalId) {
        Assert.hasText(key, "key cannot be null!");
        Assert.notNull(imageId, "imageId cannot be null!");
        Assert.notNull(adminId, "adminId cannot be null!");
        Assert.hasText(terminalId, "terminalId cannot be null!");
        recordLog(key, null, adminId, imageId, terminalId);
    }

    /**
     * 查询必要信息，记录操作日志
     * @param key 日志国际化key
     * @param  ex 异常
     * @param imageId 镜像id
     * @param adminId 管理员id
     * @param terminalId 终端id
     */
    public void recordLog(String key, @Nullable Exception ex, UUID adminId, UUID imageId, String terminalId) {
        Assert.hasText(key, "key cannot be null!");
        Assert.notNull(imageId, "imageId cannot be null!");
        Assert.notNull(adminId, "adminId cannot be null!");
        Assert.hasText(terminalId, "terminalId cannot be null!");

        String imageName = imageId.toString();
        String adminName = adminId.toString();
        String terminalMac = terminalId;
        try {
            imageName = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId).getImageName();
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(adminId);
            adminName = baseAdminDTO.getUserName();
            terminalMac = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId).getMacAddr();
        } catch (Exception e) {
            LOGGER.error("获取VDI编辑镜像操作日志信息异常", e);
        }

        if (Objects.nonNull(ex)) {
            auditLogAPI.recordLog(key, ex, adminName, imageName, terminalMac);
        } else {
            auditLogAPI.recordLog(key, adminName, imageName, terminalMac);
        }
    }

    /**
     * 查询必要信息，记录操作日志
     * @param key 日志国际化key
     * @param ex 异常
     * @param imageId 镜像id
     * @param adminId 管理员id
     * @param terminalId 终端id
     */
    public void recordLogUpperMac(String key, BusinessException ex, UUID adminId, UUID imageId, String terminalId) {
        Assert.hasText(key, "key cannot be null!");
        Assert.notNull(ex, "ex cannot be null!");
        Assert.notNull(imageId, "imageId cannot be null!");
        Assert.notNull(adminId, "adminId cannot be null!");
        Assert.hasText(terminalId, "terminalId cannot be null!");

        String imageName = imageId.toString();
        String adminName = adminId.toString();
        String terminalMac = terminalId;
        try {
            imageName = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId).getImageName();
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(adminId);
            adminName = baseAdminDTO.getUserName();
            terminalMac = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId).getUpperMacAddrOrTerminalId();
        } catch (Exception e) {
            LOGGER.error("获取VDI编辑镜像操作日志信息异常", e);
        }
        auditLogAPI.recordLog(key, ex, adminName, imageName, terminalMac, ex.getI18nMessage());
    }

    /**
     * 根据镜像ID获取虚机ID
     * @param imageId 镜像ID
     * @return 虚机ID
     */
    UUID getTempVmId(UUID imageId) throws BusinessException {
        CbbImageTemplateDetailDTO imageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        Assert.notNull(imageTemplateDetailDTO, "image template detail is null!");
        Assert.notNull(imageTemplateDetailDTO.getTempVmId(), "tempVmId is null!");
        return imageTemplateDetailDTO.getTempVmId();
    }
}
