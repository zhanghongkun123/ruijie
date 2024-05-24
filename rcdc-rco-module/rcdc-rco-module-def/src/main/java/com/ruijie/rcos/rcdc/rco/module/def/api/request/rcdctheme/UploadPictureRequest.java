package com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/11 11:27
 *
 * @author conghaifeng
 */
public class UploadPictureRequest implements Request {
    
    @NotBlank
    private String picturePath;

    @NotNull
    private ThemePictureTypeEnum themePictureTypeEnum;

    public UploadPictureRequest(String picturePath, ThemePictureTypeEnum themePictureTypeEnum) {
        this.picturePath = picturePath;
        this.themePictureTypeEnum = themePictureTypeEnum;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public ThemePictureTypeEnum getThemePictureTypeEnum() {
        return themePictureTypeEnum;
    }

    public void setThemePictureTypeEnum(ThemePictureTypeEnum themePictureTypeEnum) {
        this.themePictureTypeEnum = themePictureTypeEnum;
    }
}
