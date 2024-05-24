package com.ruijie.rcos.rcdc.rco.module.openapi.service.image.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGetOsFileResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.ImportImageOsFileRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.image.ImageOsFileService;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Paths;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@Service
public class ImageOsFileServiceImpl implements ImageOsFileService {

    private final Logger logger = LoggerFactory.getLogger(ImageOsFileServiceImpl.class);

    private static final String QCOW2_SUFFIX = ".qcow2";

    @Value("${file.busiz.dir.clouddesktop.qcow2:/external_share/qcow2/}")
    private String qcow2TempPath;

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    // MD5 计算得出的长度
    private static final Integer MD5_EQUALS_LENGTH = 32;

    @Override
    public void validateImageOsFile(ImportImageOsFileRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");

        try {
            if (!request.getFileName().toLowerCase().endsWith(QCOW2_SUFFIX)) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_FILE_SUFFIX_ERROR);
            }

            logger.info("查询镜像文件日志路径：{}", qcow2TempPath + request.getFileName());
            File osFile = Paths.get(qcow2TempPath + request.getFileName()).toFile();
            if (!osFile.exists()) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_FILE_NOT_EXISTS);
            }

            if (request.getFileMD5().length() != MD5_EQUALS_LENGTH) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_FILE_MD5_INPUT_ERROR);
            }

            CbbGetOsFileResultDTO osFileDTO = cbbOsFileMgmtAPI.findByName(request.getFileName());
            if (osFileDTO != null) {
                SystemBusinessMappingDTO systemBusinessMappingDTO = systemBusinessMappingAPI.findSystemBusinessMapping(
                        SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, SyncUpgradeConsts.BUSINESS_TYPE_IMAGE, request.getFileName());
                // 映射关系存在，并且镜像标识关联正确，且 MD5 一致
                if (systemBusinessMappingDTO != null && osFileDTO.getId().toString().equals(systemBusinessMappingDTO.getDestId())
                        && osFileDTO.getFileMd5().equals(request.getFileMD5())) {
                    throw new BusinessException(RestErrorCode.RCDC_CODE_FILENAME_EXISTS);
                } else {
                    throw new BusinessException(RestErrorCode.RCDC_CODE_FILMD5_EXISTS);
                }
            }

            if (StringUtils.isNotBlank(request.getDescription()) && request.getDescription().length() > TextMedium.TEXT_SIZE) {
                throw new BusinessException(RestErrorCode.RCDC_CODE_FILE_NOTE_TOO_LONG);
            }

            CbbGetOsFileResultDTO cbbOsFileResultByFileMd5 = cbbOsFileMgmtAPI.findByFileMd5(request.getFileMD5());
            if (cbbOsFileResultByFileMd5 != null && !cbbOsFileResultByFileMd5.getImageFileName().equals(request.getFileName())) {
                // 存在md5相同的镜像文件记录，且文件名不同，可能为通过web界面手动上传
                throw new BusinessException(RestErrorCode.RCDC_CODE_EXIST_SAME_MD5_OS_FILE, cbbOsFileResultByFileMd5.getImageFileName());
            }

        } catch (BusinessException ex) {
            logger.error("镜像文件参数验证错误", ex);
            throw RestErrorCodeMapping.convert(ex);
        }
    }
}
