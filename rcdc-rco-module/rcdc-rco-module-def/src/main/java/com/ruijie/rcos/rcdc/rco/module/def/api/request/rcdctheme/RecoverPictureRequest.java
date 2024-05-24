package com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/21 14:53
 *
 * @author conghaifeng
 */
public class RecoverPictureRequest implements Request {

    @NotNull
    private List<ThemePictureTypeEnum> pictureTypeEnumList;

    public RecoverPictureRequest(List<ThemePictureTypeEnum> pictureTypeEnumList) {
        this.pictureTypeEnumList = pictureTypeEnumList;
    }

    public List<ThemePictureTypeEnum> getPictureTypeEnumList() {
        return pictureTypeEnumList;
    }

    public void setPictureTypeEnumList(List<ThemePictureTypeEnum> pictureTypeEnumList) {
        this.pictureTypeEnumList = pictureTypeEnumList;
    }
}
