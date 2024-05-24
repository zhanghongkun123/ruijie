package com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/16 13:02
 *
 * @author conghaifeng
 */
public class GetPictureRequest implements Request {

    @NotNull
    private ThemePictureTypeEnum themePictureTypeEnum;

    public ThemePictureTypeEnum getThemePictureTypeEnum() {
        return themePictureTypeEnum;
    }

    public void setThemePictureTypeEnum(ThemePictureTypeEnum themePictureTypeEnum) {
        this.themePictureTypeEnum = themePictureTypeEnum;
    }

    public GetPictureRequest(ThemePictureTypeEnum themePictureTypeEnum) {
        this.themePictureTypeEnum = themePictureTypeEnum;
    }
}
