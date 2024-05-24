package com.ruijie.rcos.rcdc.rco.module.impl.sms.adaptor;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsPlatformType;

/**
 * Description: SmsPlatformTypeSupport
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/9
 *
 * @author TD
 */
public interface SmsPlatformTypeSupport {

    /**
     * 短信平台类型
     * @return SmsPlatformType 短信平台类型
     */
    SmsPlatformType getType();
    
}
