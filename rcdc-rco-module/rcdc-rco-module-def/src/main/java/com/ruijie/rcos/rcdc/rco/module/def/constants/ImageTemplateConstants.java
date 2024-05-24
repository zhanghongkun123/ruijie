package com.ruijie.rcos.rcdc.rco.module.def.constants;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import org.apache.commons.compress.utils.Sets;

import java.util.Set;

/**
 * Description: 镜像模板常量
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-08-17
 *
 * @author linke
 */
public interface ImageTemplateConstants {

    /**
     * 可以进行镜像变更的镜像模板状态
     */
    Set<ImageTemplateState> IMAGE_CAN_UPDATE_SET =
            Sets.newHashSet(ImageTemplateState.AVAILABLE, ImageTemplateState.AVAILABLE_EXPORTING);
}
