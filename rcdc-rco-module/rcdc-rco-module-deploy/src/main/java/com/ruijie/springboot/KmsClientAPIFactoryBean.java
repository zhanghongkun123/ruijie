package com.ruijie.springboot;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月10日
 *
 * @author 徐国祥
 */
public class KmsClientAPIFactoryBean implements InvocationHandler, FactoryBean<Object> {
    private static final String KMS_CLIENT_API_CLASS_NAME = //
            "com.ruijie.rcos.rcdc.clouddesktop.module.impl.connector.kms.KmsClientAPI";

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {getObjectType()}, this);
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(KMS_CLIENT_API_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //
        return null;
    }
}
