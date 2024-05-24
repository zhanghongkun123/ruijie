package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.impl;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetOsFileResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageSpaceInfoResponseDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbEditOsFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbImportOsFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbFileSourceType;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.MtoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.ImageOsFileServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.DirectorSpaceInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.ImportImageOsFileRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.common.AbstractServerImpl;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.image.ImageOsFileService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@Service
public class ImageOsFileServerImpl extends AbstractServerImpl implements ImageOsFileServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageOsFileServerImpl.class);

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private ImageOsFileService imageOsFileService;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Value("${file.busiz.dir.clouddesktop.qcow2:/external_share/qcow2/}")
    private String qcow2TempPath;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    // 业务锁前缀
    private final static String PREFIX_LOCK_NAME = "ImageOsFileServerImpl";

    @Override
    public void importImageOsFile(ImportImageOsFileRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");
        invoke(request, PREFIX_LOCK_NAME + request.getFileName(), () -> {
            try {
                doImportImageOsFile(request);
            } catch (BusinessException ex) {
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_OS_UPLOAD_FAIL_LOG, ex, request.getFileName(), ex.getI18nMessage());
                throw ex;
            }
        });
    }

    @Override
    public DirectorSpaceInfoDTO getImageSpaceInfo() throws BusinessException {
        try {
            CbbQueryImageSpaceInfoResponseDTO cbbQueryImageSpaceInfoResponseDTO = cbbImageTemplateMgmtAPI.queryImageSpaceInfo();
            DirectorSpaceInfoDTO directorSpaceInfoDTO = new DirectorSpaceInfoDTO();
            Long totalSpace = Optional.ofNullable(cbbQueryImageSpaceInfoResponseDTO.getTotalSpace()).orElse(0L);
            Long usableSpace = Optional.ofNullable(cbbQueryImageSpaceInfoResponseDTO.getUsableSpace()).orElse(0L);
            directorSpaceInfoDTO.setTotalSpace(totalSpace);
            directorSpaceInfoDTO.setUsedSpace(totalSpace - usableSpace);
            return directorSpaceInfoDTO;
        } catch (Throwable ex) {
            throw RestErrorCodeMapping.convert2BusinessException(ex);
        }
    }

    // 处理业务逻辑操作
    private void doImportImageOsFile(ImportImageOsFileRequest request) throws BusinessException {

        // 验证镜像文件相关业务
        imageOsFileService.validateImageOsFile(request);
        // 重新保存映射
        SystemBusinessMappingDTO systemBusinessMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                SyncUpgradeConsts.BUSINESS_TYPE_IMAGE, request.getFileName());
        //优先判断fileMd5是否存在，如果存在则直接保存mapping
        CbbGetOsFileResultDTO cbbGetOsFileResultDTO = cbbOsFileMgmtAPI.findByFileMd5(request.getFileMD5());
        if (cbbGetOsFileResultDTO != null) {
            //判断拷贝的镜像映射是否已经存在, 若存在就退出
            if (systemBusinessMapping != null &&
                    systemBusinessMapping.getDestId().equals(cbbGetOsFileResultDTO.getId().toString())) {
                return;
            }

            CbbEditOsFileDTO cbbEditOsFileDTO = new CbbEditOsFileDTO();
            cbbEditOsFileDTO.setId(cbbGetOsFileResultDTO.getId());
            cbbEditOsFileDTO.setNote(StringUtils.defaultString(cbbGetOsFileResultDTO.getNote()) + "\n" + request.getFileName());
            cbbOsFileMgmtAPI.editOsFile(cbbEditOsFileDTO);

        } else {
            // 调用镜像文件导入接口
            CbbImportOsFileDTO cbbImportOsFileDTO = new CbbImportOsFileDTO();
            cbbImportOsFileDTO.setOsFileId(UUID.randomUUID());
            cbbImportOsFileDTO.setOsFileMD5(request.getFileMD5());
            cbbImportOsFileDTO.setOsFileName(request.getFileName());
            cbbImportOsFileDTO.setOsFilePath(qcow2TempPath + request.getFileName());
            cbbImportOsFileDTO.setOsFileSize(request.getFileSize());
            cbbImportOsFileDTO.setNote(request.getDescription());
            cbbImportOsFileDTO.setOsType(request.getOsType());
            cbbImportOsFileDTO.setSourceType(CbbFileSourceType.MTOOL);
            cbbOsFileMgmtAPI.importOsFile(cbbImportOsFileDTO);

            // 导入成功之后查询对应镜像文件
            cbbGetOsFileResultDTO = cbbOsFileMgmtAPI.findByName(request.getFileName());
            if (cbbGetOsFileResultDTO == null) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_FILE_NOT_EXISTS);
            }
        }

        if (systemBusinessMapping == null) {
            systemBusinessMapping = new SystemBusinessMappingDTO();
            systemBusinessMapping.setId(UUID.randomUUID());
            systemBusinessMapping.setSystemType(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL);
            systemBusinessMapping.setBusinessType(SyncUpgradeConsts.BUSINESS_TYPE_IMAGE);
            systemBusinessMapping.setSrcId(request.getFileName());
        }
        systemBusinessMapping.setDestId(cbbGetOsFileResultDTO.getId().toString());
        systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMapping);
        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_OS_UPLOAD_SUCCESS_LOG, request.getFileName());
    }
}
