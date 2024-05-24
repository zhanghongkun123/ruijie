package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminFunctionListCustomAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FunctionListCustomRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FunctionListCustomResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AdminFunctionListRelationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminFunctionListCustomEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 功能列表自定义管理api实现
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月28日
 *
 * @author brq
 */
public class AdminFunctionListCustomAPIImpl implements AdminFunctionListCustomAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminFunctionListCustomAPIImpl.class);

    @Autowired
    private AdminFunctionListRelationDAO adminFunctionListRelationDAO;

    private static final String REGEX = ",";

    // 尝试限制规定时间内获取锁（秒）
    private static final Integer LIMIT = 10 * 6;

    @Override
    public FunctionListCustomResponse getFunctionListOfColumnMsg(FunctionListCustomRequest request) {
        Assert.notNull(request, "request must not be null");

        LOGGER.info("获取管理员在指定功能列表页面自定义列数据请求参数：request={}", JSONObject.toJSONString(request));

        AdminFunctionListCustomEntity entity =
            adminFunctionListRelationDAO.findByAdminIdAndFunctionType(request.getAdminId(), request.getFunctionType());
        FunctionListCustomResponse response = new FunctionListCustomResponse();
        if (null != entity) {
            LOGGER.info("获取管理员在指定功能列表页面自定义列数据返回结果：entity={}", JSONObject.toJSONString(entity));
            BeanUtils.copyProperties(entity, response);
            response.setColumnTextArr(StringUtils.isNotBlank(entity.getColumnText()) ? entity.getColumnText().split(REGEX) : new String[0]);
        }
        LOGGER.info("response ：response={}", JSONObject.toJSONString(response));
        return response;
    }

    @Override
    public void saveFunctionListOfColumnMsg(FunctionListCustomRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        LOGGER.info("保存管理员在指定功能列表页面自定义列数据请求参数：request={}", JSONObject.toJSONString(request));

        LockableExecutor.executeWithTryLock(String.valueOf(request.getAdminId()), () -> {

            Date currDate = new Date();
            AdminFunctionListCustomEntity entity =
                    adminFunctionListRelationDAO.findByAdminIdAndFunctionType(request.getAdminId(), request.getFunctionType());
            if (null == entity) {
                entity = new AdminFunctionListCustomEntity();
                entity.setId(UUID.randomUUID());
                entity.setAdminId(request.getAdminId());
                entity.setFunctionType(request.getFunctionType());
                entity.setCreateTime(currDate);
            }

            entity.setUpdateTime(currDate);
            entity.setColumnText(StringUtils.join(request.getColumnTextArr(), REGEX));
            LOGGER.info("after build entity message, entity={}", JSONObject.toJSONString(entity));

            adminFunctionListRelationDAO.save(entity);
        }, LIMIT);

        LOGGER.info("保存管理员在指定功能列表页面自定义列数据成功，adminId={}", request.getAdminId());
    }
}
