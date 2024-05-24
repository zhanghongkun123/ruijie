package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月09日
 * 
 * @param <R> 返回结果对象类型
 * @author xgx
 */
public abstract class AbstractPageQueryAllHandle<R> {
    private static final int SIZE = 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPageQueryAllHandle.class);

    /**
     * 通过分页方式查询所有结果
     * @return 所有记录
     * @throws BusinessException 业务异常
     */
    public Set<R> queryAll() throws BusinessException {
        Set<R> resultSet = new HashSet<>();
        int currentPage = 0;
        while (true) {
            Pageable pageable = PageRequest.of(currentPage, SIZE);
            List<R> pageResultList = queryByPage(pageable);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("分页获取结果集[{}], 页码[{}]", JSON.toJSONString(pageResultList), currentPage);
            }

            if (ObjectUtils.isEmpty(pageResultList)) {
                LOGGER.debug("分页获取第[{}]页结果集为空", currentPage);
                break;
            }
            resultSet.addAll(pageResultList);

            if (pageResultList.size() < SIZE) {
                LOGGER.debug("分页获取第[{}]页结果集[{}]少于[{}]个", currentPage, pageResultList.size(), SIZE);
                break;
            }
            currentPage++;
        }
        return resultSet;
    }

    protected abstract List<R> queryByPage(Pageable pageable) throws BusinessException;
}
