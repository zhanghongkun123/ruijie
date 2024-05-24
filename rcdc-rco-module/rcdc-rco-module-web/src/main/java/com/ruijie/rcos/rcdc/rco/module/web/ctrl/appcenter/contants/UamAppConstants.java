package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.contants;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/31 11:44
 *
 * @author coderLee23
 */
public interface UamAppConstants {

    /**
     * 允许进行推送安装包编辑的状态
     */
    List<AppStatusEnum> ALLOW_EDIT_PUSH_INSTALL_PACKAGE_STATUS =
            Arrays.asList(AppStatusEnum.PRE_PUBLISH, AppStatusEnum.PUBLISHED, AppStatusEnum.FINISH_ERROR);
}
