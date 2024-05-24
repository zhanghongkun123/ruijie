package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 分发任务参数Service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 09:53
 *
 * @author zhangyichi
 */
public interface DistributeParameterService {

    /**
     * 创建新的参数记录
     * @param parameterDTO 参数
     * @return 新参数ID
     */
    UUID createNewParameter(DistributeParameterDTO parameterDTO);

    /**
     * 删除参数记录
     * @param parameterId 参数ID
     */
    void deleteParameter(UUID parameterId);

    /**
     * 查询参数
     * @param parameterId 参数ID
     * @return 参数内容
     * @throws BusinessException 业务异常
     */
    DistributeParameterDTO findById(UUID parameterId) throws BusinessException;

    /**
     * 查找包含指定内容的参数
     * @param content 指定内容
     * @return 参数ID列表
     */
    List<UUID> findByParameterContent(String content);
}
