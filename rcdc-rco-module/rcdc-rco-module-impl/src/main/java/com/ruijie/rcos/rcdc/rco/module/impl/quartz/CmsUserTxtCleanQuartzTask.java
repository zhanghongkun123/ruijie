package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description:CMS用户生成的TXT定时清理任务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/28 17:06
 *
 * @author linrenjian
 */
@Service
public class CmsUserTxtCleanQuartzTask implements SafetySingletonInitializer, Runnable {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUserTxtCleanQuartzTask.class);


    @Override
    public void safeInit() {
        // 每天 晚上3点 进行数据清理 清理昨天生成的文件
        String cronExpression = "0 0 3 * * ?";
        try {
            ThreadExecutors.scheduleWithCron(this.getClass().getSimpleName(), this, cronExpression);
        } catch (ParseException e) {
            throw new RuntimeException("定时任务[" + this.getClass() + "]cron表达式[" + cronExpression + "]解析异常", e);
        }
    }

    @Override
    public void run() {
        remove();
    }


    private void remove() {
        // 获取当前时间
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd")));
        // 创建文件类，指定要删除的文件夹路径
        File folder = new File(Constants.CMS_FTP_PATH);
        // 将file子目录及子文件放进文件数组
        File[] fileArr = folder.listFiles();
        // 如果包含文件进行删除操作
        if (fileArr != null) {
            for (int i = 0; i < fileArr.length; i++) {
                File file = fileArr[i];
                // 获取文件夹名称
                String name = file.getName();
                // 比当前时间小
                try {
                    if (dateCompare(name, timeNow)) {
                        // 删除文件
                        deleteFile(file);
                    }
                } catch (ParseException e) {
                    LOGGER.error("文件夹格式解析出错[" + name + "]出错，直接删除这个文件", e);
                    deleteFile(file);
                } catch (Exception e) {
                    LOGGER.error("删除文件夹[" + name + "]出错", e);
                }

            }
        }
    }

    private void deleteFile(File file) {
        // 删除文件夹
        if (file.isDirectory()) {
            delFolder(file.getAbsolutePath());
        } else {
            // 如果是文件 直接删除
            file.delete();
        }
    }



    /**
     * @param date1 日期1
     * @param date2 日期2
     * @return
     * @title: dateCompare
     * @description: 比较日期大小
     */
    private boolean dateCompare(String date1, String date2) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date bt = dateFormat.parse(date1);
        Date et = dateFormat.parse(date2);
        return bt.before(et);
    }


    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹完整绝对路径 ,"Z:/xuyun/save"
     */
    private void delFolder(String folderPath) {
        try {
            // 删除完里面所有内容
            delAllFile(folderPath);
            File myFilePath = new File(folderPath);
            // 删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            LOGGER.error("删除文件夹[" + folderPath + "]出错", e);
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径 ,"Z:/xuyun/save"
     */
    private boolean delAllFile(String path) {
        boolean enableFlag = false;
        File file = new File(path);
        if (!file.exists()) {
            return enableFlag;
        }
        if (!file.isDirectory()) {
            return enableFlag;
        }
        String[] tempArr = file.list();
        File temp = null;
        for (int i = 0; i < tempArr.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempArr[i]);
            } else {
                temp = new File(path + File.separator + tempArr[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
        }
        return enableFlag;
    }

}
