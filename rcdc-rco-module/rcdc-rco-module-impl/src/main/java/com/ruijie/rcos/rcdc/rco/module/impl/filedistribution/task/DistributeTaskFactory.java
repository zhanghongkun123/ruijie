package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;

/**
 * Description: 文件分发任务工厂类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/24 10:16
 *
 * @author zhangyichi
 */
public interface DistributeTaskFactory {

    /**
     * 创建新分发任务
     * 
     * @param fileDistributionTargetType 类型
     * @return 任务执行类对象
     */
    DistributeTaskProcessor buildDistributeTaskProcessor(FileDistributionTargetType fileDistributionTargetType);

}
