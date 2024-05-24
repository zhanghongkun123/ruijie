package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-22
 *
 * @author zqj
 */
public class InterfaceMethodUtil {


    private static final PropertyPlaceholderHelper PLACEHOLDER = new PropertyPlaceholderHelper("{", "}", ":", true);

    private InterfaceMethodUtil() {

    }

    /**
     * 从OpenAPI接口中获得当前请求的接口方法对象
     * @param interfaceClazz OpenAPI接口中声明方法对象
     * @param methodName OpenAPI接口中接口方法名
     * @param parameterTypes OpenAPI接口中接口方法参数列表
     * @return OpenAPI接口中声明方法对象
     */
    public static Method selectInterfaceMethod(Class<?> interfaceClazz, String methodName, Class<?>... parameterTypes) {
        Assert.notNull(interfaceClazz, "interfaceClazz is not null");
        Assert.notNull(methodName, "methodName is not null");
        Assert.notNull(parameterTypes, "parameterTypes is not null");
        Method[] methodArr = interfaceClazz.getMethods();

        if (methodArr.length > 0) {
            for (Method method : methodArr) {
                if (method.getName().equals(methodName) && Arrays.equals(parameterTypes, method.getParameterTypes())) {
                    return method;
                }
            }
        }
        //存在找不到方法
        return null;
    }

    /**
     * 获得OpenAPI声明方法注解中的完整URL信息
     * @param interfaceMethod OpenAPI接口中声明方法对象
     * @param pathParam URL中的参数Map
     * @return 当前请求完整URL信息
     */
    public static String getOpenAPIUrl(Method interfaceMethod, Map<String, String> pathParam) {
        Assert.notNull(interfaceMethod, "interfaceClazz is not null");
        Assert.notNull(pathParam, "methodName is not null");

        StringBuilder url = new StringBuilder();
        Class<?> clazzInterface = interfaceMethod.getDeclaringClass();

        Path startUrl = AnnotationUtils.findAnnotation(clazzInterface, Path.class);
        Path endUrl = AnnotationUtils.findAnnotation(interfaceMethod, Path.class);
        ApiAction apiAction = AnnotationUtils.findAnnotation(interfaceMethod, ApiAction.class);
        if (startUrl != null) {
            url.append(PLACEHOLDER.replacePlaceholders(startUrl.value(), pathParam::get));
        }
        if (endUrl != null) {
            url.append(PLACEHOLDER.replacePlaceholders(endUrl.value(), pathParam::get));
        }
        if (apiAction != null && StringUtils.hasText(apiAction.value())) {
            url.append(":").append(apiAction.value());
        }
        return url.toString();
    }

    /**
     * 获得调用该方法的方法名
     * @return 方法名
     */
    public static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }


    /**
     * 获取OpenAPI声明方法注解中 HttpMethod
     * @param interfaceMethod OpenAPI声明方法
     * @return HttpMethod
     */
    public static String getHttpMethod(Method interfaceMethod) {
        Assert.notNull(interfaceMethod, "interfaceClazz is not null");
        if (interfaceMethod.getAnnotation(POST.class) != null) {
            return "POST";
        } else if (interfaceMethod.getAnnotation(PUT.class) != null) {
            return "PUT";
        } else if (interfaceMethod.getAnnotation(GET.class) != null) {
            return "GET";
        } else if (interfaceMethod.getAnnotation(DELETE.class) != null) {
            return "DELETE";
        }
        //存在找不到方式
        return null;
    }
}
