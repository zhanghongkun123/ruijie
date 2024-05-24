package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月27日
 * 
 * @author zhuangchenwu
 */
@Controller
@RequestMapping("/rco/systemLog")
public class SystemLogController {

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    /**
     * 获取系统日志列表请求
     *
     * @param pageQueryRequest 请求参数
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DefaultWebResponse getSystemLogPage(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "request is not null");

        PageQueryResponse<BaseSystemLogDTO> response = baseSystemLogMgmtAPI.pageQuery(pageQueryRequest);

        return DefaultWebResponse.Builder.success(response);
    }

}
