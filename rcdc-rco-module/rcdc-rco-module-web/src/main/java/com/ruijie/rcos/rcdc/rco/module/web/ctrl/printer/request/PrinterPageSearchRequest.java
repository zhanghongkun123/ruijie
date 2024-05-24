package com.ruijie.rcos.rcdc.rco.module.web.ctrl.printer.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author chenjiehui
 */
public class PrinterPageSearchRequest extends PageSearchRequest {

    public static final Logger LOGGER = LoggerFactory.getLogger(PrinterPageSearchRequest.class);

    public PrinterPageSearchRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Override
    protected Sort[] sortConditionConvert(Sort sort) {
        Assert.notNull(sort, "sort can not be null");

        switch (sort.getSortField()) {
            case "printerConnectType":
                sort.setSortField("printerConnectType");
                break;
            case "createTime":
                sort.setSortField("createTime");
                break;
            default:
                LOGGER.info("打印机列表排序字段为：{}", sort.getSortField());
                break;
        }

        return super.sortConditionConvert(sort);
    }


}
