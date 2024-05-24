package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.util.Assert;
import com.ruijie.rcos.sk.base.filesystem.SkyengineFile;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月6日
 * 
 * @author zouqi
 */
public class DeleteFileUtil {

    private DeleteFileUtil() {
        throw new IllegalStateException("DeleteFileUtil Utility class");
    }

    /**
     * 删除文件夹及，文件夹内的文件
     * @param folderFile 文件夹
     * 
     * @throws IOException 业务异常
     * */
    public static void deleteFile(File folderFile) {
        Assert.notNull(folderFile, "File is null");
        //如果是文件夹，先删除文件夹内的文件
        if (folderFile.isDirectory()) {
            File[] fileArr = folderFile.listFiles();
            Arrays.stream(fileArr).forEach(file -> {
                //如果遇到文件夹则先删除里面的文件夹
                if (file.isDirectory()) {
                    deleteFile(file);
                    return;
                }
                //删除文件
                SkyengineFile skyengineFile = new SkyengineFile(file.getPath());
                skyengineFile.delete(false);
            });   
        }
        
        //删除文件后，删除文件夹
        SkyengineFile skyengineFile = new SkyengineFile(folderFile.getPath());
        skyengineFile.delete(false);
    }
}
