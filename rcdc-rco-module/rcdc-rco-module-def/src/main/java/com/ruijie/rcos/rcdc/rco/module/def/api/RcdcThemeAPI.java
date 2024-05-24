package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.GetPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.RecoverPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.UploadPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetPictureResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: RCDC主题管理api
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/11 11:08
 *
 * @author conghaifeng
 */
public interface RcdcThemeAPI {

    /**
     * 图片上传
     * @param request 图片上传请求
     * @return DefaultResponse
     * @throws BusinessException 业务异常
     */
    DefaultResponse uploadPicture(UploadPictureRequest request) throws BusinessException;

    /**
     * 主题初始化
     * @param request 空白请求
     * @return DefaultResponse
     */
    DefaultResponse recoverThemeToDefault(RecoverPictureRequest request);

    /**
     * 获取主题图片进行预览
     * @param request 空白请求
     * @return GetPictureResponse
     */
    GetPictureResponse getPicturePath(GetPictureRequest request);


}
