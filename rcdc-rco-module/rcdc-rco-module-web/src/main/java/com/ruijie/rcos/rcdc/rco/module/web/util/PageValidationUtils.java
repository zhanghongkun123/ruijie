package com.ruijie.rcos.rcdc.rco.module.web.util;

import com.ruijie.rcos.rcdc.rco.module.web.PublicBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/2 14:32
 *
 * @author luojianmo
 */
public class PageValidationUtils {

    private PageValidationUtils() {
        throw new IllegalStateException("PageValidationUtils Utility class");
    }

    /**
     * 分页查询判断排序字段是否合法
     * 
     * @param supportSortFieldArr 支持排序字段数字
     * @param sortField 排序字段名称
     * @throws BusinessException 业务异常
     */
    public static void sortFieldValidation(String[] supportSortFieldArr, String sortField) throws BusinessException {
        Assert.notNull(supportSortFieldArr, "supportSortFieldArr must not be null");
        Assert.hasText(sortField, "sortField must not be null");
        for (String supportSortField : supportSortFieldArr) {
            if (sortField.equals(supportSortField)) {
                // 排序字段符合排序要求
                return;
            }
        }
        throw new BusinessException(PublicBusinessKey.RCDC_RCO_PUBLIC_PAGE_SORT_FIELD_VALIDATION_FAIL, sortField);
    }
}
