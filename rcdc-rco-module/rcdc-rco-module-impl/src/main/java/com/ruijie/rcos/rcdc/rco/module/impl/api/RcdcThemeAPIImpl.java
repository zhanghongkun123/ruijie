package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.RcdcThemeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.GetPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.RecoverPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.UploadPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetPictureResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.util.FileUtil;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.filesystem.SkyengineFile;
import com.ruijie.rcos.sk.base.io.IoUtil;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/11 16:18
 *
 * @author conghaifeng
 */
public class RcdcThemeAPIImpl implements RcdcThemeAPI {
    
    @Autowired
    private ConfigFacade configFacade;

    private static final String RCDC_THEME_PATH_CONFIG = "file.busiz.dir.rcdc.theme";

    /**
     * 图片上传
     *
     * @param request 图片上传请求
     * @return DefaultResponse
     * @throws BusinessException 业务异常
     */
    @Override
    public DefaultResponse uploadPicture(UploadPictureRequest request) throws BusinessException {
        Assert.notNull(request,"request can not be null");
        String pictureName = request.getThemePictureTypeEnum().getName();
        deletePicture(pictureName);
        savePicture(request.getPicturePath(), pictureName);
        return DefaultResponse.Builder.success();
    }

    /**
     * 登陆页主题初始化
     *
     * @param request 空白请求
     * @return DefaultResponse
     */
    @Override
    public DefaultResponse recoverThemeToDefault(RecoverPictureRequest request) {
        Assert.notNull(request,"request can not be null");
        List<ThemePictureTypeEnum> pictureTypeEnumList = request.getPictureTypeEnumList();
        pictureTypeEnumList.forEach(item -> deletePicture(item.getName()));
        return DefaultResponse.Builder.success();
    }

    /**
     * 获取图片路径进行预览
     *
     * @param request 图片请求
     * @return GetPicturePathResponse
     */
    @Override
    public GetPictureResponse getPicturePath(GetPictureRequest request) {
        Assert.notNull(request, "request can not be null");

        String saveDirectory = this.configFacade.read(RCDC_THEME_PATH_CONFIG);
        String pictureName = request.getThemePictureTypeEnum().getName();
        String savePicturePath = saveDirectory + pictureName;
        GetPictureResponse response = new GetPictureResponse();
        if (new File(savePicturePath).exists()) {
            response.setPicturePath(savePicturePath);
        } else {
            response.setPicturePath(null);
        }
        return response;
    }


    private void savePicture(String upLoadPath,String pictureName) throws BusinessException {
        File uploadFile = new File(upLoadPath);
        String saveDirectory = this.configFacade.read(RCDC_THEME_PATH_CONFIG);
        String savePicturePath = saveDirectory + pictureName;
        FileUtil.createDirectory(saveDirectory);
        File saveFile = new File(savePicturePath);
        try {
            IoUtil.copy(uploadFile, saveFile);
            Files.deleteIfExists(uploadFile.toPath());
        } catch (IOException e) {
            throw new BusinessException(BusinessKey.RCDC_RCO_UPLOAD_PICTURE_FAIL, e);
        }
    }

    private void deletePicture(String pictureName) {
        String saveDirectory = this.configFacade.read(RCDC_THEME_PATH_CONFIG);
        String savePicturePath = saveDirectory + pictureName;
        File saveFile = new File(savePicturePath);
        if (saveFile.exists()) {
            SkyengineFile skyengineFile = new SkyengineFile(saveFile);
            skyengineFile.delete(false);
        }
    }

}
