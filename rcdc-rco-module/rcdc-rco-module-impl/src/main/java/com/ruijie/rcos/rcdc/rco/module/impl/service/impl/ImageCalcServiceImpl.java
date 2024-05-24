package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbIDVDeskImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.IDVImageFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbIDVImageDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageCalcService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * <br>
 * Description: 镜像计算工作 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021.04.25 <br>
 *
 * @author linhj
 */
@Service
public class ImageCalcServiceImpl implements ImageCalcService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ImageCalcServiceImpl.class);

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Override
    public int getImageFileSize(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId must not be null");

        CbbIDVDeskImageTemplateDTO templateDTOArr;
        try {
            templateDTOArr = cbbIDVDeskMgmtAPI.getIDVDeskImageTemplate(imageId);
        } catch (Exception ex) {
            LOGGER.error("调用cbbIDVDeskMgmtAPI获取的镜像信息异常，镜像ID " + imageId);
            throw new BusinessException(BusinessKey.RCDC_NOT_FIND_IMAGE_FILE, ex);
        }
        List<CbbIDVImageDiskDTO> imageDiskList = Optional.ofNullable(templateDTOArr.getImageDiskList()).orElse(new ArrayList<>());
        Long imageFileSize = 0L;
        for (CbbIDVImageDiskDTO idvImageDiskDTO : imageDiskList) {
            IDVImageFileDTO idvImageFileDTO = idvImageDiskDTO.getIdvImageFileDTO();
            if (ObjectUtils.isEmpty(idvImageFileDTO)) {
                continue;
            }
            // 种子文件为空，该镜像没有发布，不计算空间大小
            if (StringUtils.isAnyBlank(idvImageFileDTO.getTorrentFileMD5(), //
                    idvImageFileDTO.getTorrentFileName(), //
                    idvImageFileDTO.getTorrentFilePath())) { //
                continue;
            }

            File instanceFile = new File(idvImageFileDTO.getFilePath());
            if (!instanceFile.exists() || !instanceFile.isFile()) {
                LOGGER.error("instanceFile is not exist " + idvImageFileDTO.getFilePath());
                throw new BusinessException(BusinessKey.RCDC_NOT_FIND_IMAGE_FILE);
            }
            imageFileSize += instanceFile.length();

            File baseFile = new File(idvImageFileDTO.getBackingFile().getFilePath());
            if (!baseFile.exists() || !baseFile.isFile()) {
                LOGGER.error("baseFile is not exist " + idvImageFileDTO.getFilePath());
                throw new BusinessException(BusinessKey.RCDC_NOT_FIND_IMAGE_FILE);
            }
            imageFileSize += baseFile.length();
        }

        return CapacityUnitUtils.byte2Gb(imageFileSize);
    }


}
