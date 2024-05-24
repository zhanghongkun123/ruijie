package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/11 13:33
 *
 * @author conghaifeng
 */
public class GetPictureResponse extends DefaultResponse {

    private String picturePath;

    public String getPicturePath() {
        return this.picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
}
