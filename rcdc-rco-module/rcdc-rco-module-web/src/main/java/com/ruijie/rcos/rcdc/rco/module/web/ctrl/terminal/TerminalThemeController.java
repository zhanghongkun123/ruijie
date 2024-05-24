package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import java.io.File;
import java.util.UUID;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.io.Files;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.UploadTerminalBackgroundBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.UploadTerminalLogoBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.UploadTerminalShortcutBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.PreviewTerminalLogoWebRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalBackgroundAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogoAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalShortcutAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBackgroundImageInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalLogoInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalShortcutImageInfoDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/9 16:21
 *
 * @author conghaifeng
 */
@Controller
@RequestMapping("/rco/terminal")
public class TerminalThemeController {

    @Autowired
    private CbbTerminalLogoAPI terminalLogoAPI;

    @Autowired
    CbbTerminalBackgroundAPI cbbTerminalBackgroundAPI;

    @Autowired
    CbbTerminalShortcutAPI cbbTerminalShortcutAPI;

    @Autowired
    private WebclientNotifyAPI webclientNotifyAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 上传Logo
     *
     * @param logo           logo
     * @param taskBuilder    批任务
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/logo/upload")
    public DefaultWebResponse uploadTerminalLogo(ChunkUploadFile logo, BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(logo, "logo can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_ITEM_NAME)).build();
        UploadTerminalLogoBatchTaskHandler upgradePackageHandler =
                new UploadTerminalLogoBatchTaskHandler(logo, taskItem, auditLogAPI, terminalLogoAPI, webclientNotifyAPI);
        BatchTaskSubmitResult result = taskBuilder.setTaskName(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_TASK_DESC, logo.getFileName())
                .registerHandler(upgradePackageHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 预览Logo
     *
     * @param request        request
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @RequestMapping("/logo/preview")
    public DownloadWebResponse previewTerminalLogo(PreviewTerminalLogoWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbTerminalLogoInfoDTO logoInfo = terminalLogoAPI.getLogoInfo();
        if (logoInfo != null && StringUtils.hasText(logoInfo.getLogoPath())) {
            File logo = new File(logoInfo.getLogoPath());
            String logoName = logo.getName();
            String logoSuffix = logoName.substring(logoName.indexOf('.') + 1);
            return new DownloadWebResponse.Builder()//
                    .setContentType("application/octet-stream")//
                    .setName(logoName, logoSuffix)//
                    .setFile(logo)//
                    .build();
        } else {
            return new DownloadWebResponse.Builder().build();
        }

    }

    /**
     * 背景图片的上传
     *
     * @param chunkUploadFile 上传的文件
     * @param taskBuilder     批任务
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/background/upload")
    public DefaultWebResponse uploadBackgroundImage(ChunkUploadFile chunkUploadFile, BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(chunkUploadFile, "chunkUploadFile must not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_BACKGROUND_ITEM_NAME))
                .build();

        UploadTerminalBackgroundBatchTaskHandler batchTaskHandler =
                new UploadTerminalBackgroundBatchTaskHandler(chunkUploadFile, taskItem, auditLogAPI, cbbTerminalBackgroundAPI, webclientNotifyAPI);

        BatchTaskSubmitResult result = taskBuilder.setTaskName(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_DESC, chunkUploadFile.getFileName())
                .registerHandler(batchTaskHandler)
                .start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 显示终端背景图片
     *
     * @param request 默认请求
     * @return DownloadWebResponse 下载响应
     * @throws BusinessException 业务请求
     */
    @RequestMapping("/background/preview")
    public DownloadWebResponse previewBackgroundImage(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CbbTerminalBackgroundImageInfoDTO dto = cbbTerminalBackgroundAPI.getBackgroundImageInfo();
        if (dto == null) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_PREVIEW_BACKGROUND_FILE_FAIL);
        }
        File background = new File(dto.getImagePath());
        String fileExtension = Files.getFileExtension(dto.getImagePath());
        return new DownloadWebResponse.Builder().setContentType("application/octet-stream").setName(dto.getImageName(), fileExtension)
                .setFile(background).build();
    }

    /**
     * 终端主题初始化
     *
     * @param request        默认请求
     * @return DefaultWebResponse 默认返回对象
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/init")
    public DefaultWebResponse init(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        try {
            cbbTerminalBackgroundAPI.initBackgroundImage();
            terminalLogoAPI.initLogo();
            cbbTerminalShortcutAPI.initShortcutImage();
            // 通知网页版客户端主题策略变更
            webclientNotifyAPI.notifyThemeChange();
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_THEME_SUCCESS);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_THEME_FAIL, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_THEME_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 快捷方式图的上传
     *
     * @param chunkUploadFile 上传的文件
     * @param taskBuilder     批任务
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/shortcut/upload")
    public DefaultWebResponse uploadShortcutImage(ChunkUploadFile chunkUploadFile, BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(chunkUploadFile, "chunkUploadFile must not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_SHORTCUT_ITEM_NAME))
                .build();

        UploadTerminalShortcutBatchTaskHandler batchTaskHandler =
                new UploadTerminalShortcutBatchTaskHandler(chunkUploadFile, taskItem, auditLogAPI, cbbTerminalShortcutAPI);

        BatchTaskSubmitResult result = taskBuilder.setTaskName(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_DESC, chunkUploadFile.getFileName())
                .registerHandler(batchTaskHandler)
                .start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 显示快捷方式图片
     *
     * @param request 默认请求
     * @return DownloadWebResponse 下载响应
     * @throws BusinessException 业务请求
     */
    @RequestMapping("/shortcut/preview")
    public DownloadWebResponse previewShortcutImage(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        CbbTerminalShortcutImageInfoDTO dto = cbbTerminalShortcutAPI.getShortcutImageInfo();
        if (dto != null && StringUtils.hasText(dto.getImagePath())) {
            File shortcut = new File(dto.getImagePath());
            String fileExtension = Files.getFileExtension(dto.getImagePath());
            return new DownloadWebResponse.Builder().setContentType("application/octet-stream").setName(dto.getImageName(), fileExtension)
                    .setFile(shortcut).build();
        } else {
            return new DownloadWebResponse.Builder().build();
        }

    }
}
