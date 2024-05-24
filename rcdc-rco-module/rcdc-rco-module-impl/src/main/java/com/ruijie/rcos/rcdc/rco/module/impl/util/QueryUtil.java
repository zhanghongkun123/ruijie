package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.Assert;

/**
 * Description: 查询相关工具类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/18 0:17
 *
 * @param <T> 请求参数
 * @param <R> 返回参数
 * @author zhangyichi
 */
public class QueryUtil<T, R extends DefaultPageRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryUtil.class);

    /**
     * 使用分页接口，循环获取所有符合条件的数据
     * @param pageRequest 目标API所需的分页request
     * @param pageQuery 目标API的分页查询方法
     * @param filterCondition 分页返回结果的过滤条件
     * @param loopCondition 是否继续获取下一页的判断条件
     * @return 所有符合条件的数据
     * @throws BusinessException 业务异常
     */
    public List<T> findAllByPageQuery(R pageRequest, ThrowingFunction<R, ? extends DefaultPageResponse<T>> pageQuery,
        Predicate<T> filterCondition, Predicate<List<T>> loopCondition) throws BusinessException {
        Assert.notNull(pageRequest, "pageRequest cannot be null!");
        Assert.notNull(pageQuery, "pageQuery cannot be null!");
        Assert.notNull(filterCondition, "filterCondition cannot be null!");
        Assert.notNull(loopCondition, "loopCondition cannot be null!");

        int page = 0;
        List<T> dtoList = Lists.newArrayList();
        boolean shouldContinue = true;

        while (shouldContinue) {
            pageRequest.setPage(page);
            DefaultPageResponse<T> apiResponse = pageQuery.apply(pageRequest);
            if (apiResponse == null || null == apiResponse.getItemArr()) {
                LOGGER.error("分页获取信息失败, response = {}", JSONObject.toJSONString(apiResponse));
                throw new BusinessException(BusinessKey.RCDC_RCO_BIGSCREEN_GET_FROM_API_PAGE_QUERY_FAIL,
                    JSONObject.toJSONString(apiResponse));
            }
            T[] dtoArr = apiResponse.getItemArr();
            if (dtoArr.length == 0) {
                break;
            }
            List<T> filteredList = Stream.of(dtoArr).filter(filterCondition).collect(Collectors.toList());
            dtoList.addAll(filteredList);
            shouldContinue = loopCondition.test(dtoList);
            page++;
        }
        return dtoList;
    }
}
