package com.ruijie.rcos.rcdc.rco.module.web.config.plugin;

import com.google.common.base.Optional;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Swagger 入参注解扩展插件工厂<br/>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.05.20
 *
 * @author linhongjie
 */
@Controller
public class ExpandedParameterAnnotationFactory implements ApplicationContextAware, ExpandedParameterBuilderPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpandedParameterAnnotationFactory.class);

    private static Map<String, ExpandedParameterBuilderPluginAPI<Object>> beanMap;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Assert.notNull(applicationContext, "context must not be null");

        beanMap = new ConcurrentHashMap<>();
        applicationContext.getBeansOfType(ExpandedParameterBuilderPluginAPI.class).forEach((key, value) -> {
            try {
                Type[] typeArr = value.getClass().getGenericInterfaces();
                Type superType = ((ParameterizedType) typeArr[0]).getActualTypeArguments()[0];
                LOGGER.info("load swagger parameter plugin：{}", superType.getTypeName());
                beanMap.put(superType.getTypeName(), value);
            } catch (Exception ex) {
                LOGGER.warn("load swagger parameter plugin error", ex);
            }
        });
    }

    @SuppressWarnings({"unchecked", "Guava"})
    @Override
    public void apply(ParameterExpansionContext context) {

        Assert.notNull(context, "context must not be null");

        for (Map.Entry<String, ExpandedParameterBuilderPluginAPI<Object>> annotationEntry : beanMap.entrySet()) {
            try {
                Class<? extends Annotation> annotationType = (Class<? extends Annotation>) Class.forName(annotationEntry.getKey());
                Optional<? extends Annotation> annotation = context.findAnnotation(annotationType);
                if (!annotation.isPresent()) {
                    continue;
                }

                annotationEntry.getValue().invoke(context.getParameterBuilder(), annotation.get());
            } catch (Exception ex) {
                LOGGER.error("invoke swagger parameter plugin error", ex);
            }
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
