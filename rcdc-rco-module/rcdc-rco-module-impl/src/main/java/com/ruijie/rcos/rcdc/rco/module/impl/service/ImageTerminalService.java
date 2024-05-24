package com.ruijie.rcos.rcdc.rco.module.impl.service;


import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTemplateTerminalDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time:  2020/2/26
 *
 * @author songxiang
 */

public interface ImageTerminalService {
    /**
     * 查找已经在该镜像编辑过的终端列表
     * @param imageId  镜像ID
     * @return  已经在该镜像编辑过的终端列表
     */
    DtoResponse<ImageTemplateTerminalDTO[]> queryEditedTerminalListOfImage(UUID imageId);

    /**
     * 增加镜像在终端编辑过的信息
     * @param dto 实体
     * @return DefaultResponse
     */
    DefaultResponse addEditedTerminalInfoOfImage(ImageTemplateTerminalDTO dto);
}
