package com.ruijie.rcos.rcdc.rco.module.web.util;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.web.PublicBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/07/18
 *
 * @author ypp
 */
public class LinuxPathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxPathUtils.class);


    public static final int LINUX_PATH_ARR_LENGTH = 128;

    private static final String PRE_LINUX_FILE_PATH_PATTERN = "^/.*";

    private static final String LINUX_FILE_PATH_PATTERN = "^[^*?\"'<>|:;$!`&#\\\\]*$";

    /**
     * 校验windows路径是否合法
     *
     * @param linuxPath 路径
     * @throws BusinessException 业务异常
     */
    public static void checkLinuxPath(String linuxPath) throws BusinessException {
        Assert.hasText(linuxPath, "linuxPath must not be null");

        boolean isPreFilePatchMatches = linuxPath.matches(PRE_LINUX_FILE_PATH_PATTERN);
        if (!isPreFilePatchMatches) {
            LOGGER.error("路径{}必须是绝对路径，如/home。", linuxPath);
            throw new BusinessException(PublicBusinessKey.RCDC_LINUX_PATH_DRIVE_LETTER_ERROR);
        }
        
        String[] linuxPathArr = linuxPath.split("/");
        
        for (String path : linuxPathArr) {

            if (path.length() > LINUX_PATH_ARR_LENGTH) {
                LOGGER.error("路径{}长度不能超过128个字符。", linuxPath);
                throw new BusinessException(PublicBusinessKey.RCDC_LINUX_PATH_CANNOT_EXCEED_CHARACTERS);
            }

            if (path.startsWith(" ") || path.endsWith(" ")) {
                LOGGER.error("文件名{}不能以空格开头或结尾。", linuxPath);
                throw new BusinessException(PublicBusinessKey.RCDC_LINUX_PATH_CANNOT_STARTSWITH_OR_ENDSWITH_CHARACTERS);
            }

            boolean isMatches = path.matches(LINUX_FILE_PATH_PATTERN);
            if (!isMatches) {
                LOGGER.error("路径{}中不能包含以下字符：*?\"'(){}<>|\\:;&#=\\[\\]", linuxPath);
                throw new BusinessException(PublicBusinessKey.RCDC_LINUX_PATH_CANNOT_CONTAIN_FOLLOWING_CHARACTERS);
            }

        }

    }
}
