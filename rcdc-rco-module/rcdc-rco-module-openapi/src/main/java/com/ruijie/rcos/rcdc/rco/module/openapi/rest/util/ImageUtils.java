package com.ruijie.rcos.rcdc.rco.module.openapi.rest.util;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.filesystem.SkyengineFile;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/05/24
 *
 * @author chenl
 */
public class ImageUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

    /** 镜像文件路径 */
    @Deprecated
    public static final String FILE_PATH = "/opt/ftp/image/";


    /**
     * 镜像文件删除
     * 
     * @throws BusinessException ex
     * @param filePath filePath
     */
    public static void deleteFile(String filePath) throws BusinessException {
        Assert.hasText(filePath, "filePath can not be empty");

        Path dirPath = Paths.get(filePath).getParent();
        File dirFile = dirPath.toFile();
        if (!dirFile.exists()) {
            LOGGER.error("镜像存储目录[{}]不存在或未挂载", Paths.get(filePath).getParent().toString());
            throw new BusinessException("23250309");
        }
        SkyengineFile skyengineFile = new SkyengineFile(filePath);
        skyengineFile.delete(false);
    }


}
