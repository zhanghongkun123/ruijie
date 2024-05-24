package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamAppTestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月29日
 *
 * @author zhk
 */
public interface UamAppTestServiceTx {

    /**
     * 创建测试任务
     * 
     * @param dto 请求参数
     * @return 测试任务id
     * @throws BusinessException 业务异常
     */
    UUID createAppTest(UamAppTestDTO dto) throws BusinessException;


    /**
     * 增加测试桌面
     *
     * @param testId 测试id
     * @param deskIdList 桌面id
     * @throws BusinessException 业务异常
     */
    void addAppTestDesk(UUID testId, List<UUID> deskIdList) throws BusinessException;
}
