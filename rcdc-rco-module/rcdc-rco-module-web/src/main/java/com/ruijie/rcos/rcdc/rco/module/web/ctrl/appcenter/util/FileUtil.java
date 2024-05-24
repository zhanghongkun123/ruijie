package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.util;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月19日
 *
 * @author lcy
 */
public class FileUtil {

    public FileUtil() {
        throw new IllegalStateException("FileUtil Utility class");
    }

    private static final List<String> EXT_LIST = Arrays.asList("tar.gz", "tar.bz2", "tar.bz", "tar.Z", "tar.xz");

    private static final String DOT = ".";

    /**
     * 获取文件扩展名【包括一些特殊扩展名】
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getExtension(String fileName) {
        Assert.notNull(fileName, "fileName can not be null");
        String found = null;
        // 这里先匹配特殊的文件扩展名
        for (String ext : EXT_LIST) {
            if (fileName.endsWith(DOT + ext)) {
                if (found == null || found.length() < ext.length()) {
                    found = ext;
                }
            }
        }
        // 常规获取文件扩展
        if (found == null) {
            found = getExtensionCommon(fileName);
        }
        return found;
    }

    /**
     * 通用获取文件扩展名
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getExtensionCommon(String fileName) {
        Assert.notNull(fileName, "fileName can not be null");
        char ch;
        int len;
        if (fileName == null || (len = fileName.length()) == 0 || (ch = fileName.charAt(len - 1)) == '/' || ch == '\\' || ch == '.' ) {
            return "";
        }
        int dotInd = fileName.lastIndexOf('.');
        int sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if ( dotInd <= sepInd ) {
            return "";
        } else {
            return fileName.substring(dotInd + 1);
        }
    }
}
