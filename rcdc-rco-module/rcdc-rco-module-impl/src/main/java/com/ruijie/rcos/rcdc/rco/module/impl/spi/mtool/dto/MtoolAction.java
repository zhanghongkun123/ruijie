package com.ruijie.rcos.rcdc.rco.module.impl.spi.mtool.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.06
 *
 * @author linhj
 */
public interface MtoolAction {

    /**
     * get_key_information 4.x 升级 5.x 迁移获取信息接口
     */
    String GET_KEY_INFORMATION = "get_key_information";

    /**
     * sync_old_version_upgrade_result 4.x 升级 5.x 上报迁移结果信息接口
     */
    String SYNC_OLD_VERSION_UPGRADE_RESULT = "sync_old_version_upgrade_result";
}
