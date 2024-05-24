package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbRcaAppHandleSPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbImageTemplateNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.response.CbbImageTemplateDeleteNotifyResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IdvTerminalGroupDeskConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ImageDownloadStateDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserGroupDesktopConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IdvTerminalGroupDeskConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageDownloadStateEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserGroupDesktopConfigEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;

;

/**
 * 校验删除镜像模板策略
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月31日
 *
 * @author zhuangchenwu
 */
public class DeskImageTemplateNotifySPIImpl implements CbbImageTemplateNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskImageTemplateNotifySPIImpl.class);

    private static final String SPACE = " ";

    private static final int RETRY_DELETE_COUNT = 3;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private IdvTerminalGroupDeskConfigDAO idvTerminalGroupDeskConfigDAO;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private UserDesktopConfigDAO userDesktopConfigDAO;

    @Autowired
    private UserGroupDesktopConfigDAO userGroupDesktopConfigDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private ImageDownloadStateDAO imageDownloadStateDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private CbbRcaAppHandleSPI cbbRcaAppHandleSPI;

    @Override
    public CbbImageTemplateDeleteNotifyResponse beforeDeleteImageTemplate(UUID imageTemplateId) throws BusinessException {
        Assert.notNull(imageTemplateId, "request can not be null");
        LOGGER.info("开始校验删除镜像模板:[{}]", imageTemplateId);
        List<UUID> imageIdList = new ArrayList();
        imageIdList.add(imageTemplateId);
        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
        String rootImageName = imageTemplateInfo.getImageName();
        if (imageTemplateInfo.getImageRoleType() == ImageRoleType.VERSION) {
            //添加源镜像
            imageIdList.add(imageTemplateInfo.getRootImageId());
            CbbImageTemplateDetailDTO rootImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateInfo.getRootImageId());
            rootImageName = rootImageTemplateDetailDTO.getImageName();
        } else if (imageTemplateInfo.getEnableMultipleVersion()) {
            List<CbbImageTemplateDetailDTO> versionList = cbbImageTemplateMgmtAPI.listImageTemplateVersionByRootImageId(imageTemplateId);
            imageIdList.addAll(versionList.stream().map(CbbImageTemplateDetailDTO::getId).collect(Collectors.toList()));
        }

        String errorMessage = "";
        int count = 0;
        try {
            // 校验是否存在关联的用户
            checkRelativeUser(rootImageName, imageIdList);
        } catch (BusinessException e) {
            LOGGER.error("error", e);
            String msg = ++count + "." + e.getI18nMessage() + SPACE;
            errorMessage += msg;
        }

        try {
            // 校验是否存在关联的用户组
            checkRelativeUserGroup(rootImageName, imageIdList);
        } catch (BusinessException e) {
            LOGGER.error("error", e);
            String msg = ++count + "." + e.getI18nMessage() + SPACE;
            errorMessage += msg;
        }

        try {
            // 校验是否存在关联的idv终端组
            checkRelativeIdvTerminalGroup(imageTemplateId);
        } catch (BusinessException e) {
            LOGGER.error("error", e);
            String msg = ++count + "." + e.getI18nMessage() + SPACE;
            errorMessage += msg;
        }


        try {
            // 校验是否存在正在下载的终端
            checkRelativeImageDownload(imageTemplateId);
        } catch (BusinessException e) {
            LOGGER.error("error", e);
            String msg = ++count + "." + e.getI18nMessage() + SPACE;
            errorMessage += msg;
        }
        if (count > 0) {
            throw new BusinessException(BusinessKey.RCDC_RCO_BUSINESS_COMMON_MESSAGE_KEY, errorMessage);
        }

        LOGGER.info("校验删除镜像模板[{}]通过", imageTemplateId);
        return new CbbImageTemplateDeleteNotifyResponse();
    }

    @Override
    public Boolean customOperateAfterRestore(UUID imageId) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public boolean afterDeleteImageTemplateSnapshot(UUID snapshotId) {
        Assert.notNull(snapshotId, "snapshotId must not be null");

        LOGGER.info("收到删除镜像快照[{}]事件", snapshotId);
        UnifiedManageDataRequest request = new UnifiedManageDataRequest();
        request.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_SNAPSHOT);
        request.setRelatedId(snapshotId);

        // 清除应用表信息
        cbbRcaAppHandleSPI.deleteBySnapshotId(snapshotId);

        int retryCount = 0;
        // 重试删除三次
        while (retryCount < RETRY_DELETE_COUNT) {
            try {
                rccmManageAPI.deleteUnifiedManage(request);
                LOGGER.info("删除快照[{}]统一管理信息成功", snapshotId);
                return true;
            } catch (Exception e) {
                retryCount++;
                LOGGER.error("删除快照[{}]统一管理信息失败进行第[{}]重试，失败原因", snapshotId, retryCount, e);
            }
        }
        LOGGER.error("删除快照[{}]统一管理信息失败超过重试次数，不再进行重试", snapshotId);
        return false;
    }


    /**
     * 查询是否有终端正在下载当前镜像
     *
     * @param imageTemplateId 镜像ID
     * @throws BusinessException 业务异常
     */
    private void checkRelativeImageDownload(UUID imageTemplateId) throws BusinessException {
        List<ImageDownloadStateEntity> imageDownloadEntityList = imageDownloadStateDAO.findByImageIdAndDownloadState(imageTemplateId,
                DownloadStateEnum.START);
        if (CollectionUitls.isEmpty(imageDownloadEntityList)) {
            return;
        }

        StringBuilder terminalNameSb = new StringBuilder();
        for (ImageDownloadStateEntity imageDownloadEntity : imageDownloadEntityList) {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(imageDownloadEntity.getTerminalId());
            if (terminalNameSb.length() > 0) {
                terminalNameSb.append(",");
            }
            terminalNameSb.append(terminalBasicInfoDTO.getTerminalName());
        }
        String terminalNameStr = terminalNameSb.toString();
        LOGGER.warn("镜像模板[id:{}]存在关联,正在下载镜像的终端:[{}]", imageTemplateId, terminalNameStr);
        String imageTemplateName = getImageTemplateName(imageTemplateId);
        throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_HAS_RELATIVE_TERMINAL, imageTemplateName,
                terminalNameStr);
    }

    private void checkRelativeUser(String rootImageName, List<UUID> imageIdCheckList) throws BusinessException {
        UserDesktopConfigEntity configEntity = userDesktopConfigDAO.findFirstByImageTemplateIdIn(imageIdCheckList);
        if (configEntity != null) {
            boolean hasVersion = imageIdCheckList.size() > 1;
            IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(configEntity.getUserId());
            if (hasVersion) {
                String versionImageName = getImageTemplateName(configEntity.getImageTemplateId());
                LOGGER.warn("镜像模板[{}]版本[{}]存在关联的用户[{}]", rootImageName, versionImageName, userDetail.getUserName());
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_RELATIVE_USER, rootImageName,
                        versionImageName, userDetail.getUserName());
            } else {
                LOGGER.warn("镜像模板[{}]存在关联的用户[{}]", rootImageName, userDetail.getUserName());
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_RELATIVE_USER, rootImageName,
                        userDetail.getUserName());
            }
        }
    }

    private void checkRelativeUserGroup(String rootImageName, List<UUID> imageIdCheckList) throws BusinessException {
        UserGroupDesktopConfigEntity configEntity = userGroupDesktopConfigDAO.findFirstByImageTemplateIdIn(imageIdCheckList);
        if (configEntity != null) {
            boolean hasVersion = imageIdCheckList.size() > 1;
            IacUserGroupDetailDTO groupDetail = cbbUserGroupAPI.getUserGroupDetail(configEntity.getGroupId());
            if (hasVersion) {
                String versionImageName = getImageTemplateName(configEntity.getImageTemplateId());
                LOGGER.warn("镜像模板[{}]版本[{}]存在关联的用户组[{}]", rootImageName, versionImageName, groupDetail.getName());
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_RELATIVE_GROUP, rootImageName,
                        versionImageName, groupDetail.getName());
            } else {
                LOGGER.warn("镜像模板[{}]存在关联的用户组[{}]", rootImageName, groupDetail.getName());
                throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_RELATIVE_GROUP, rootImageName,
                        groupDetail.getName());
            }



        }
    }

    private void checkRelativeIdvTerminalGroup(UUID imageTemplateId) throws BusinessException {
        IdvTerminalGroupDeskConfigEntity entity = idvTerminalGroupDeskConfigDAO.findFirstByCbbIdvDesktopImageId(imageTemplateId);
        if (entity != null) {
            CbbTerminalGroupDetailDTO terminalGroupDTO = cbbTerminalGroupMgmtAPI.loadById(entity.getCbbTerminalGroupId());
            String imageTemplateName = getImageTemplateName(imageTemplateId);
            LOGGER.warn("镜像模板[{}]存在关联的idv终端组[{}]", imageTemplateName, terminalGroupDTO.getGroupName());
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_IMAGE_TEMPLATE_RELATIVE_IDV_GROUP,
                    imageTemplateName, terminalGroupDTO.getGroupName());
        }
    }

    private String getImageTemplateName(UUID imageTemplateId) throws BusinessException {
        CbbGetImageTemplateInfoDTO imageTemplateResp = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
        return imageTemplateResp.getImageName();
    }

}
