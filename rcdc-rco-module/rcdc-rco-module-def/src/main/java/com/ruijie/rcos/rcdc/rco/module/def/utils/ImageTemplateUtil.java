package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.IDVImageFileDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Description: 镜像模板工具类
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022/9/27 <br>
 *
 * @author zwf
 */
public class ImageTemplateUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(ImageTemplateUtil.class);

    /**
     * 获取镜像文件大小
     *
     * @param idvImageFileDTO cbbdto
     * @return 文件大小
     */
    public static long getImageSizeFromDTO(IDVImageFileDTO idvImageFileDTO) {
        Assert.notNull(idvImageFileDTO, "idvImageFileDTO cant be null");
        long fileSize = idvImageFileDTO.getFileSize();
        if (idvImageFileDTO.getBackingFile() != null) {
            fileSize += getImageSizeFromDTO(idvImageFileDTO.getBackingFile());
        }
        return fileSize;
    }
}
