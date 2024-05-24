package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.appcenter.module.def.constant.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppTerminalAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientCompressionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AppClientCompressionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.TerminalUpdateListCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AppUpdateListDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.FileNameExtension;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.filesystem.common.FileUtils;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.File;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/20
 *
 * @author hs
 */
public class AppTerminalAPIImpl implements AppTerminalAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppTerminalAPIImpl.class);

    @Autowired
    private ClientCompressionAPI compressionAPI;

    @Override
    public String getAppDownloadUrl(CbbTerminalTypeEnums terminalType) throws BusinessException {
        Assert.notNull(terminalType, "terminalType can not be null");
        AppUpdateListDTO listDTO = TerminalUpdateListCacheManager.get(terminalType);
        // 获取updatelist中完整组件的信息，从中获取全量包文件路径
        if (!TerminalUpdateListCacheManager.isCacheReady(terminalType)) {
            LOGGER.error("[{}]软终端updateList缓存未就绪", terminalType.name());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_APP_UPDATELIST_CACHE_NOT_READY, terminalType.getOsType());
        }

        if (listDTO == null) {
            LOGGER.error("[{}]软终端updateList信息异常", terminalType.name());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_COMPONENT_UPDATELIST_CACHE_INCORRECT, terminalType.getOsType());
        }

        // 旧的下载包名称
        String oldDownloadName = listDTO.getCompletePackageName();
        String completePackageUrl = null;
        AppClientCompressionDTO appClientCompressionConfig = compressionAPI.getAppClientCompressionConfig();
        String exeName;
        if (BooleanUtils.isTrue(appClientCompressionConfig.getOpenOneInstall())) {
            // 如果一键部署开启 则下载zip 先统一搜索转为ZIP ，有则转 没有就是ZIP 直接用
            exeName = oldDownloadName.contains(FileNameExtension.ZIP.getName()) ? oldDownloadName
                    : oldDownloadName.replace(terminalType.convertFileNameExtension().getName(), FileNameExtension.ZIP.getName());

        } else {
            // 没有开启 原先是什么就是什么 先统一搜索转为ZIP切回原来 ，有则转 没有就是原来 直接用
            exeName = oldDownloadName.contains(terminalType.convertFileNameExtension().getName()) ? oldDownloadName
                    : oldDownloadName.replace(FileNameExtension.ZIP.getName(), terminalType.convertFileNameExtension().getName());

        }
        completePackageUrl = terminalType.getCbbAppTerminalOsType().getComponentDir() + exeName;
        if (FileUtils.isValidPath(new File(completePackageUrl))) {
            return completePackageUrl;
        }
        // 无全量包信息
        LOGGER.error("[{}]软终端updatelist信息异常", terminalType.name());
        throw new BusinessException(BusinessKey.RCDC_TERMINAL_COMPONENT_UPDATELIST_CACHE_INCORRECT, terminalType.getOsType());
    }
}
