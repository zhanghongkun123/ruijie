package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcdcThemeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.GetPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.RecoverPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetPictureResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.batchtask.UploadLargeScreenLogoBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.batchtask.UploadRcdcAdminLogoBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.batchtask.UploadRcdcBackgroundBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.batchtask.UploadRcdcLoginLogoBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.InitRcdcLogoWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.PreviewRcdcThemeWebRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;

/**
 * Description: 更改RCDC的logo
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/11 10:46
 *
 * @author conghaifeng
 */

@Controller
@RequestMapping("/rco/rcdcTheme")
public class RcdcThemeController {

    @Autowired
    RcdcThemeAPI rcdcThemeAPI;
    
    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 上传登陆页logo
     *
     * @param logo logo文件
     * @param builder builder
     * @return 文件上传结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/uploadLoginLogo")
    public DefaultWebResponse uploadLoginRcdcLogo(ChunkUploadFile logo, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(logo, "logo can not be null");
        Assert.notNull(builder, "builder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_LOGO_ITEM_NAME)).build();
        UploadRcdcLoginLogoBatchTaskHandler upgradePackageHandler =
                new UploadRcdcLoginLogoBatchTaskHandler(logo, taskItem, auditLogAPI, rcdcThemeAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_LOGO_TASK_NAME)
                .setTaskDesc(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_LOGO_TASK_DESC, logo.getFileName())
                .registerHandler(upgradePackageHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 预览登陆页logo
     *
     * @param webRequest 预览logo请求
     * @return DefaultWebResponse
     */
    @NoAuthUrl
    @RequestMapping(value = "/showLoginLogo")
    public DownloadWebResponse showLoginLogo(PreviewRcdcThemeWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");
        GetPictureRequest getPictureRequest = new GetPictureRequest(ThemePictureTypeEnum.RCDC_LOGIN_LOGO);
        GetPictureResponse response = rcdcThemeAPI.getPicturePath(getPictureRequest);
        return dealWithGetPictureResponse(response);
    }

    /**
     * 上传管理页logo
     *
     * @param logo logo文件
     * @param builder builder
     * @return 文件上传结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/uploadAdminLogo")
    public DefaultWebResponse uploadRcdcAdminLogo(ChunkUploadFile logo, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(logo, "logo can not be null");
        Assert.notNull(builder, "builder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_ITEM_NAME)).build();
        UploadRcdcAdminLogoBatchTaskHandler upgradePackageHandler =
                new UploadRcdcAdminLogoBatchTaskHandler(logo, taskItem, auditLogAPI, rcdcThemeAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_TASK_NAME)
                .setTaskDesc(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_ADMIN_LOGO_TASK_DESC, logo.getFileName())
                .registerHandler(upgradePackageHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 预览管理页logo
     *
     * @param webRequest 预览logo请求
     * @return DefaultWebResponse
     */
    @NoAuthUrl
    @RequestMapping(value = "/showAdminLogo")
    public DownloadWebResponse showAdminLogo(PreviewRcdcThemeWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");
        GetPictureRequest getPictureRequest = new GetPictureRequest(ThemePictureTypeEnum.RCDC_ADMIN_LOGO);
        GetPictureResponse response = rcdcThemeAPI.getPicturePath(getPictureRequest);
        return dealWithGetPictureResponse(response);
    }

    /**
     * 上传登陆页背景图片
     *
     * @param backGround 背景图片
     * @param builder builder
     * @return 文件上传结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/uploadLoginBackground")
    public DefaultWebResponse uploadRcdcBackground(ChunkUploadFile backGround, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(backGround, "backGround can not be null");
        Assert.notNull(builder, "builder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_BACKGROUND_ITEM_NAME)).build();
        UploadRcdcBackgroundBatchTaskHandler upgradePackageHandler =
                new UploadRcdcBackgroundBatchTaskHandler(backGround, taskItem, auditLogAPI, rcdcThemeAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_BACKGROUND_TASK_NAME)
                .setTaskDesc(RcoBusinessKey.RCDC_RCO_UPLOAD_RCDC_BACKGROUND_TASK_DESC, backGround.getFileName())
                .registerHandler(upgradePackageHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 预览登陆页背景图片
     *
     * @param webRequest 预览背景图片请求
     * @return DefaultWebResponse
     */
    @NoAuthUrl
    @RequestMapping(value = "/showLoginBackGround")
    public DownloadWebResponse showBackGround(PreviewRcdcThemeWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");
        GetPictureRequest request = new GetPictureRequest(ThemePictureTypeEnum.RCDC_BACKGROUND);
        GetPictureResponse response = rcdcThemeAPI.getPicturePath(request);
        return dealWithGetPictureResponse(response);
    }

    /**
     * 恢复登陆页默认主题
     *
     * @param webRequest 恢复默认主题请求
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "/recoverLoginThemeToDefault")
    public DefaultWebResponse recoverThemeToDefault(InitRcdcLogoWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");
        ArrayList<ThemePictureTypeEnum> themePictureTypeEnumsList =
                Lists.newArrayList(ThemePictureTypeEnum.RCDC_LOGIN_LOGO, ThemePictureTypeEnum.RCDC_BACKGROUND);
        rcdcThemeAPI.recoverThemeToDefault(new RecoverPictureRequest(themePictureTypeEnumsList));
        auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_INIT_RCDC_THEME_SUCCESS_LOG);
        return DefaultWebResponse.Builder.success(RcoBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    /**
     * 恢复管理页默认logo
     *
     * @param webRequest 恢复默认主题请求
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "/recoverAdminLogoToDefault")
    public DefaultWebResponse recoverLogoToDefault(InitRcdcLogoWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");
        ArrayList<ThemePictureTypeEnum> themePictureTypeEnumsList = Lists.newArrayList(ThemePictureTypeEnum.RCDC_ADMIN_LOGO);
        rcdcThemeAPI.recoverThemeToDefault(new RecoverPictureRequest(themePictureTypeEnumsList));
        auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_INIT_RCDC_LOGO_SUCCESS_LOG);
        return DefaultWebResponse.Builder.success(RcoBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    /**
     * 上传大屏页logo
     *
     * @param logo logo文件
     * @param builder builder
     * @return 文件上传结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/uploadLargeScreenLogo")
    public DefaultWebResponse uploadLargeScreenLogo(ChunkUploadFile logo, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(logo, "logo can not be null");
        Assert.notNull(builder, "builder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_ITEM_NAME)).build();
        UploadLargeScreenLogoBatchTaskHandler upgradePackageHandler =
                new UploadLargeScreenLogoBatchTaskHandler(taskItem, logo, auditLogAPI, rcdcThemeAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_TASK_NAME)
                .setTaskDesc(RcoBusinessKey.RCDC_RCO_UPLOAD_LARGE_SCREEN_LOGO_TASK_DESC, logo.getFileName())
                .registerHandler(upgradePackageHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 预览大屏LOGO图片
     *
     * @return DefaultWebResponse
     */
    @NoAuthUrl
    @RequestMapping(value = "/previewLargeScreenLogo")
    public DownloadWebResponse previewLargeScreenLogo() {
        GetPictureRequest request = new GetPictureRequest(ThemePictureTypeEnum.RCDC_LARGE_SCREEN_LOGO);
        GetPictureResponse response = rcdcThemeAPI.getPicturePath(request);
        return dealWithGetPictureResponse(response);
    }

    /**
     * 恢复大屏页面默认logo
     *
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "/recoverLargeScreenLogoToDefault")
    public DefaultWebResponse recoverLargeScreenLogoToDefault() {
        ArrayList<ThemePictureTypeEnum> themePictureTypeEnumsList = Lists.newArrayList(ThemePictureTypeEnum.RCDC_LARGE_SCREEN_LOGO);
        rcdcThemeAPI.recoverThemeToDefault(new RecoverPictureRequest(themePictureTypeEnumsList));
        auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_INIT_LARGE_SCREEN_LOGO_SUCCESS_LOG);
        return DefaultWebResponse.Builder.success(RcoBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    private DownloadWebResponse dealWithGetPictureResponse(GetPictureResponse response) {
        String picturePath = response.getPicturePath();
        if (picturePath == null) {
            return new DownloadWebResponse();
        } else {
            File picture = new File(picturePath);
            String pictureName = picture.getName();
            String pictureSuffix = pictureName.substring(pictureName.indexOf('.') + 1);
            return new DownloadWebResponse.Builder().setContentType("application/octet-stream")
                    .setName(pictureName, pictureSuffix).setFile(picture)
                    .build();
        }
    }

}
