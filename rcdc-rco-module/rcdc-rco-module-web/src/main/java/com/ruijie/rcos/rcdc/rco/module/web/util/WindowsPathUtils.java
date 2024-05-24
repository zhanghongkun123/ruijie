package com.ruijie.rcos.rcdc.rco.module.web.util;

import com.ruijie.rcos.rcdc.rco.module.web.PublicBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/03/06 22:36
 *
 * @author coderLee23
 */
public class WindowsPathUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WindowsPathUtils.class);

    private static final String PRE_WIN_FILE_PATH_PATTERN = "^[a-zA-Z]:$";

    // 匹配以连续两个"\\"结尾的路径
    private static final String PATH_END_WITH_PATTERN = "^(.*)\\\\\\\\$";

    private static final String WIN_FILE_PATH_PATTERN = "^[^/:*?\"<>|]*$";

    private static final String FILTER_PATH_NAME_KEYWORD = "(^(COM|LPT)[0-9](\\..+)?)|(^(CON|PRN|AUX|NUL)(\\..+)?)$";

    private static final  Pattern FILTER_PATH_NAME_KEYWORD_PATTERN = Pattern.compile(FILTER_PATH_NAME_KEYWORD);

    public static final int WINDOWS_PATH_ARR_LENGTH = 128;

    private WindowsPathUtils() {
        throw new UnsupportedOperationException("不支持的操作！");
    }

    /**
     * 校验windows路径是否合法
     *
     * @param windowsPath 路径
     * @throws BusinessException 业务异常
     */
    public static void checkWindowsPath(String windowsPath) throws BusinessException {
        Assert.hasText(windowsPath, "windowsPath must not be null");

        // 路径中出现"\\"会导致gt下载失败，下面判空的操作可以检验这种场景，但是末尾的"\\"无法被识别
        // 修订bug: 667293, 判断如果超过两个“\\”结尾 提示异常信息，和前端保持一致
        if (windowsPath.matches(PATH_END_WITH_PATTERN)) {
            LOGGER.error("文件或文件夹{}名称中不能包含以下字符：/ \\ : * ? \" < > |。", windowsPath);
            throw new BusinessException(PublicBusinessKey.RCDC_WINDOWS_PATH_CANNOT_CONTAIN_FOLLOWING_CHARACTERS);
        }
        String[] windowsPathArr = windowsPath.split("\\\\");
        boolean isFirstFlag = true;
        for (String path : windowsPathArr) {
            if (isFirstFlag) {
                boolean isPreFilePatchMatches = path.matches(PRE_WIN_FILE_PATH_PATTERN);
                if (!isPreFilePatchMatches) {
                    LOGGER.error("路径{}必须以盘符开头，如C:\\。", windowsPath);
                    throw new BusinessException(PublicBusinessKey.RCDC_WINDOWS_PATH_DRIVE_LETTER_ERROR);
                }

                isFirstFlag = false;
                continue;
            }

            if (path.length() > WINDOWS_PATH_ARR_LENGTH) {
                LOGGER.error("文件或文件夹{}名称长度不能超过128个字符。", windowsPath);
                throw new BusinessException(PublicBusinessKey.RCDC_WINDOWS_PATH_CANNOT_EXCEED_CHARACTERS);
            }

            if (FILTER_PATH_NAME_KEYWORD_PATTERN.matcher(path.toUpperCase()).matches()) {
                LOGGER.error("文件或文件夹{}名称不能是以下保留字符：CON、PRN、AUX、NUL、以及COM0~COM9和LPT0~LPT9。", windowsPath);
                throw new BusinessException(PublicBusinessKey.RCDC_WINDOWS_PATH_CANNOT_FOLLOWING_RESERVED_CHARACTERS);
            }

            if (path.startsWith(" ") || path.endsWith(" ") || path.endsWith(".")) {
                LOGGER.error("文件或文件夹{}名称不能以空格开头或结尾，不能以点号结尾。", windowsPath);
                throw new BusinessException(PublicBusinessKey.RCDC_WINDOWS_PATH_CANNOT_STARTSWITH_OR_ENDSWITH_CHARACTERS);
            }

            boolean isMatches = path.matches(WIN_FILE_PATH_PATTERN);
            if (!isMatches || StringUtils.isEmpty(path)) {
                LOGGER.error("文件或文件夹{}名称中不能包含以下字符：/ \\ : * ? \" < > |。", windowsPath);
                throw new BusinessException(PublicBusinessKey.RCDC_WINDOWS_PATH_CANNOT_CONTAIN_FOLLOWING_CHARACTERS);
            }

        }

    }


}
