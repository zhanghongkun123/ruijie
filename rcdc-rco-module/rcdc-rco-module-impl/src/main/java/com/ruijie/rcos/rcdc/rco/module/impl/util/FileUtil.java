package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

/**
 * 文件相关工具类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年11月14日
 *
 * @author zhfw
 */

public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("FileUtil Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建目录
     * Description：如果目录存在则不创建，目录不存在则创建一个新目录
     *
     * @param path 目录路径
     */
    public static void createDirectory(String path) {
        Assert.notNull(path, "目录path不能为空");
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
            boolean canSetReadable = directory.setReadable(true, false);
            if (!canSetReadable) {
                LOGGER.warn("读取权限操作失败");
            }
            boolean canSetExecutable = directory.setExecutable(true, false);
            if (!canSetExecutable) {
                LOGGER.warn("执行权限操作失败");
            }
        }
    }

    /**
     * 创建文件
     *
     * @param path     文件路径
     * @param fileName 文件名
     */
    public static void createFile(String path, String fileName) {
        Assert.notNull(path, "path can not be null");
        Assert.notNull(fileName, "file name can not be null");
        File file = new File(path + fileName);
        try {
            boolean canCreateNewFile = file.createNewFile();
            if (!canCreateNewFile) {
                LOGGER.warn("指定文件已存在");
            }
        } catch (IOException e) {
            LOGGER.error("create file error", e);
        }
    }

    /**
     * 写文件
     *
     * @param fileName 要操作的文件
     * @param content  写入的内容
     */
    public static void writeFile(String fileName, String content) {
        Assert.notNull(fileName, "filename is not be null");
        Assert.notNull(content, "content is not be null");
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(content);
        } catch (IOException e) {
            LOGGER.error("writer file error", e);
        }
    }

    /**
     * 读取配置文件
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @return 结果
     */
    public static Properties fillProperties(String path, String fileName) {
        Assert.notNull(path, "path can not be null");
        Assert.notNull(fileName, "file name can not be null");

        Properties verIni = null;
        try (InputStream is = new FileInputStream((new File(path, fileName)))) {
            verIni = new Properties();
            verIni.load(is);
        } catch (Exception e) {
            LOGGER.error("error has happen,please check:", e);
        }
        return verIni;
    }

    /**
     * 根据文件获取文件创建时间
     *
     * @param file 文件对象
     * @return 文件时间
     */
    public static Date getFileDateTime(File file) {
        Assert.notNull(file, "file is null");
        Date creationTime = null;
        try {
            BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime localDateTime = Instant.ofEpochMilli(attributes.creationTime().toMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            creationTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            LOGGER.error("get file:[{}] create time error:", file.getPath(), e);
        }
        return creationTime;
    }

    /**
     * 文件复制
     * @param sourceFileStr 源文件路径
     * @param targetFileStr 目标文件路径
     * @param options 拷贝参数
     */
    public static void copyFile(String sourceFileStr, String targetFileStr, CopyOption... options) {
        Assert.hasText(sourceFileStr, "sourceFileStr can not be blank");
        Assert.notNull(targetFileStr, "targetFileStr can not be blank");
        Assert.notNull(options, "options is null");
        try {
            File sourceFile = new File(sourceFileStr);
            if (!sourceFile.exists()) {
                return;
            }

            // 复制文件
            Path targetFilePath = Paths.get(targetFileStr);
            Files.copy(sourceFile.toPath(), targetFilePath, options);
        } catch (Exception e) {
            LOGGER.error("复制文件[{}]异常", targetFileStr, e);
        }

    }

    /**
     * 通过文件元数据进行比对 (时间快)
     * @param sourceFileStr 源文件路径
     * @param targetFileStr 目标文件路径
     * @return 文件是否一直
     */
    public static boolean isEqualByMetaData(String sourceFileStr, String targetFileStr) {
        Assert.hasText(sourceFileStr, "sourceFileStr can not be blank");
        Assert.notNull(targetFileStr, "targetFileStr can not be blank");
        try {
            File sourceFile = new File(sourceFileStr);
            if (!sourceFile.exists()) {
                return false;
            }

            File targetFile = new File(targetFileStr);
            if (!targetFile.exists()) {
                return false;
            }

            BasicFileAttributes sourceAttr = Files.readAttributes(Paths.get(sourceFileStr), BasicFileAttributes.class);
            BasicFileAttributes targetAttr = Files.readAttributes(Paths.get(targetFileStr), BasicFileAttributes.class);

            return sourceAttr.size() == targetAttr.size() && sourceAttr.lastModifiedTime().equals(targetAttr.lastModifiedTime());
        } catch (Exception e) {
            LOGGER.error("复制文件[{}]异常", targetFileStr, e);
            return false;
        }

    }
}
