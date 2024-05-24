package com.ruijie.rcos.rcdc.rco.module.impl.init;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.TerminalUpdateListCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AppUpdateListDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.filesystem.common.FileUtils;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 软终端缓存初始化
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/3 10:21
 *
 * @author conghaifeng
 */
@Service
public class AppDownloadCacheInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDownloadCacheInit.class);


    public AppDownloadCacheInit() {

    }

    /**
     * updatelist缓存初始化
     * @param terminalType  终端类型
     */
    public final void init(CbbTerminalTypeEnums terminalType) {
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(terminalType.getCbbAppTerminalOsType(), "cbbAppTerminalOsType can not be null");
        // 开始初始化，将缓存就绪状态设为未就绪
        LOGGER.info("terminal type: {}", terminalType.name());
        TerminalUpdateListCacheManager.setUpdatelistCacheNotReady(terminalType);
        String filePath = terminalType.getCbbAppTerminalOsType().getUpdateListPath();
        File updateListFile = new File(filePath);
        if (!updateListFile.isFile()) {
            LOGGER.error("updatelist file not exist or not a file, the file path is {}", filePath);
            return;
        }
        String updateListContent = readUpdateListContent(updateListFile);
        if (StringUtils.isBlank(updateListContent)) {
            LOGGER.error("获取updatelist信息失败，请检查updatelist文件是否正确，updatelist文件路径：{}", filePath);
            return;
        }

        AppUpdateListDTO updatelist = JSON.parseObject(updateListContent, AppUpdateListDTO.class);
        putInCache(terminalType, updatelist);
        // 完成初始化后将updatelist缓存状态更新为false
        TerminalUpdateListCacheManager.setUpdatelistCacheReady(terminalType);
        LOGGER.info("[{}]finish init updatelist...",terminalType.name());
    }

    /**
     * 读取updatelist的内容
     *
     * @param updateListFile updatelist文件
     * @return
     */
    private String readUpdateListContent(File updateListFile) {
        String updatelistStr = "";
        try {
            updatelistStr = FileUtils.readFileToString(updateListFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("[{}]read updatelist file error", updateListFile.getName(), e);
        }

        return updatelistStr;
    }

    /**
     * 将组件信息存入缓存
     *
     * @param updatelist 组件信息
     */
    protected void putInCache(CbbTerminalTypeEnums terminalType,AppUpdateListDTO updatelist) {
        if (updatelist == null) {
            LOGGER.error("[{}]updatelist is null, upgrade file is incorrect",terminalType.name());
            return;
        }
        // 将组件升级updatelist按终端类型，存入缓存中
        TerminalUpdateListCacheManager.add(terminalType, updatelist);
    }

}
