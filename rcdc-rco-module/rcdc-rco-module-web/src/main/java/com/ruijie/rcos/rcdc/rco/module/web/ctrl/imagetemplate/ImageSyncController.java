package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ViewCbbImageSyncRecordDTO;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: 镜像版本同步WEB接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/29
 *
 * @author Joshua
 */
@Api(tags = "镜像版本同步管理")
@Controller
@RequestMapping("/rco/clouddesktop/imageTemplate/sync")
@PageQueryWebConfig(url = "/list", dtoType = ViewCbbImageSyncRecordDTO.class)
public class ImageSyncController {

    // 其他接口待定
}
