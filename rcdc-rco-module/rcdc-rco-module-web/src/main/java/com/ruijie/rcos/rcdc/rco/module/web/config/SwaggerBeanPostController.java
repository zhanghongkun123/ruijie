package com.ruijie.rcos.rcdc.rco.module.web.config;

import com.google.common.base.Predicate;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.config.ConfigFacadeHolder;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Description: Spring 初始化嵌入 Swagger 功能<br/>
 * \@Controller 注解 sk 框架不执行注入，仅做 Spring 标记使用<br/>
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.10
 *
 * @author linhj
 */
@Controller
public class SwaggerBeanPostController implements BeanFactoryPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerBeanPostController.class);

    // Swagger 接口页面标题
    private static final String SWAGGER_TITLE_NAME = "云办公管理平台";

    // Swagger 接口页面描述
    private static final String SWAGGER_TITLE_DESC = "云办公管理平台后台模块 API";

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        Assert.notNull(configurableListableBeanFactory, "configurableListableBeanFactory must be not null");

        LOGGER.info("初始化 Swagger 配置开始");
        final boolean enableSwagger = Boolean.parseBoolean(ConfigFacadeHolder.getFacade().read("rcdc.swagger.enable"));
        final String basePackageUrl = ConfigFacadeHolder.getFacade().read("rcdc.swagger.base.package.url");
        LOGGER.info("初始化 Swagger 获取开关配置：{}", enableSwagger);
        LOGGER.info("初始化 Swagger 扫码路径配置：{}", basePackageUrl);

        try {
            // 默认分组
            Docket defaultDocket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo(Version.V1_0_0.getMavenVersion())).select()
                    .apis(RequestHandlerSelectors.basePackage(basePackageUrl)).paths(PathSelectors.any()).build()
                    .ignoredParameterTypes(initIgnorableTypes().toArray(new Class[]{})).enable(enableSwagger);
            configurableListableBeanFactory.registerSingleton("defaultVersionDocket", defaultDocket);

            for (Version value : Version.values()) {

                if (value == Version.V1_0_0) {
                    // 默认分组无需创建
                    continue;
                }

                String newGroupName = String.format("%s 新增接口", value.getProductVersion());
                String editGroupName = String.format("%s 修订接口", value.getProductVersion());

                // 新增分组
                Docket newVersionDocket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo(value.getMavenVersion())).select()
                        .apis(RequestHandlerSelectors.basePackage(basePackageUrl)).apis(selector(value, false)).paths(PathSelectors.any()).build()
                        .enable(enableSwagger).ignoredParameterTypes(initIgnorableTypes().toArray(new Class[]{})).groupName(newGroupName);

                // 修订分组
                Docket editVersionDocket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo(value.getMavenVersion())).select()
                        .apis(RequestHandlerSelectors.basePackage(basePackageUrl)).apis(selector(value, true)).paths(PathSelectors.any()).build()
                        .enable(enableSwagger).ignoredParameterTypes(initIgnorableTypes().toArray(new Class[]{})).groupName(editGroupName);

                configurableListableBeanFactory.registerSingleton("newVersionDocket" + value.getMavenVersion(), newVersionDocket);
                configurableListableBeanFactory.registerSingleton("editVersionDocket" + value.getMavenVersion(), editVersionDocket);
            }

            LOGGER.info("初始化 Swagger 配置完成");

        } catch (Exception ex) {
            // 插件注册扫描不能影响业务初始化逻辑
            LOGGER.error("Swagger 注册分组失败", ex);
        }
    }

    /**
     * 限制 sk 部分类引入参数中
     *
     * @return return
     * @see SessionContext
     * @see ProgrammaticOptLogRecorder
     * @see BatchTaskBuilder
     */
    private HashSet<Class<?>> initIgnorableTypes() {
        HashSet<Class<?>> ignored = new HashSet<>();
        ignored.add(SessionContext.class);
        ignored.add(ProgrammaticOptLogRecorder.class);
        ignored.add(BatchTaskBuilder.class);
        return ignored;
    }

    /**
     * Swagger 分组选择器
     *
     * @param version    当前接口标记的版本
     * @param needExists 分组类型：新增/修订
     * @return 是否满足分组条件
     */
    @SuppressWarnings("Guava")
    private Predicate<RequestHandler> selector(Version version, boolean needExists) {
        return requestHandler -> {
            ApiVersions swaggerVersionGroup = requestHandler.getHandlerMethod().getMethodAnnotation(ApiVersions.class);
            if (swaggerVersionGroup == null) {
                return false;
            }

            ApiVersion[] apiVersionArr = swaggerVersionGroup.value();
            if (apiVersionArr.length == 0) {
                return false;
            }

            if (Arrays.stream(apiVersionArr).noneMatch(apiVersion -> apiVersion.value().getMavenVersion().equals(version.getMavenVersion()))) {
                return false;
            }

            if (needExists) {
                // 存在小于当前版本的记录
                return existsLessThanVersion(apiVersionArr, version);
            } else {
                return !existsLessThanVersion(apiVersionArr, version);
            }
        };
    }

    /**
     * Swagger 接口标记的所有版本中是否存在比当前版本小的版本
     *
     * @param apiVersions 接口标记的所有版本
     * @param version     当前版本
     * @return 是否满足条件
     */
    private boolean existsLessThanVersion(ApiVersion[] apiVersions, Version version) {

        try {
            String[] normalSplitArr = version.getMavenVersion().split("\\.");
            for (ApiVersion apiVersion : Arrays.stream(apiVersions).distinct().collect(Collectors.toList())) {

                // 不比较相同版本
                if (apiVersion.value() == version) {
                    continue;
                }

                // 顺序比较各个版本标识
                String[] intefaceSplitArr = apiVersion.value().getMavenVersion().split("\\.");
                int maxlength = Math.max(normalSplitArr.length, intefaceSplitArr.length);
                for (int i = 0; i < maxlength; i++) {
                    if (Integer.parseInt(normalSplitArr[i]) > Integer.parseInt(intefaceSplitArr[i])) {
                        return true;
                    } else if (Integer.parseInt(normalSplitArr[i]) == Integer.parseInt(intefaceSplitArr[i])) {
                        i++;
                    }
                }
            }

            return false;
        } catch (Exception ex) {
            LOGGER.error("swagger existsLessThanVersion error.", ex);
            return false;
        }
    }

    @SafeVarargs
    private final ApiInfo apiInfo(String version, VendorExtension<String>... extensions) {
        return new ApiInfoBuilder().title(SWAGGER_TITLE_NAME).description(SWAGGER_TITLE_DESC).version(version).extensions(Arrays.asList(extensions))
                .build();
    }
}
