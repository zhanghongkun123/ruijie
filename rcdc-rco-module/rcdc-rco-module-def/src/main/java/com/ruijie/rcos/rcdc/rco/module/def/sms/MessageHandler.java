package com.ruijie.rcos.rcdc.rco.module.def.sms;

import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.HttpResultParserDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: MessageHandler
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/13
 *
 * @author TD
 */
public interface MessageHandler {

    /**
     * 解析器
     * @param parserDTO 解析DTO
     * @param result 待解析的参数
     * @return 是否成功
     * @throws BusinessException 业务异常
     */
    String resolverResponse(HttpResultParserDTO parserDTO, String result) throws BusinessException;
    
}
