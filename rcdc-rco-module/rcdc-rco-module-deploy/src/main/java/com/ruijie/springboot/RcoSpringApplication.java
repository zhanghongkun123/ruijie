package com.ruijie.springboot;

import java.sql.SQLException;
import java.util.List;

import javassist.*;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;

import com.google.common.collect.Lists;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.springboot.ContextSourceLoader;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月15日
 * @SpringBootApplication(//
 *         exclude = {DataSourceAutoConfiguration.class, H2ConsoleAutoConfiguration.class}, //
 *         excludeName = {"com.ruijie.rcos.sk.webmvc.all.context.SkyEngineWebMvcAutoConfiguration"} //
 * )
 * @author 徐国祥
 */

@Import(ServletWebServerFactoryAutoConfiguration.class)
public class RcoSpringApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcoSpringApplication.class);

    private static final String DEV_MODE_KEY = "devMode";

    private static final String DEV_MODE = "DEBUG";


    /**
     * springboot启动入口
     * 
     * @param args 参数
     */
    public static void main(@Nullable String[] args) {
        LOGGER.info("【通过main函数启动cdc】");

        modifyClass();
        System.setProperty(DEV_MODE_KEY, DEV_MODE);

        Class<?>[] classArr = ContextSourceLoader.loadContextSources();
        List<Class<?>> classesList = Lists.newArrayList(classArr);
        classesList.add(RcoSpringApplication.class);

        SpringApplication.run(classesList.toArray(new Class[0]), args);
    }

    @Bean
    DebugGlobalParameterAPIBeanPostProcessor getDebugBeanPostProcessor() {
        return new DebugGlobalParameterAPIBeanPostProcessor();
    }

    @Bean
    CdcPublicMemoryDataBaseInitBeanPostProcessor getMemoryDataBaseStarterBeanFactoryPostProcessor() {
        return new CdcPublicMemoryDataBaseInitBeanPostProcessor();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    Server getTcpServer() throws SQLException {
        return Server.createTcpServer();
    }


    /**
     * 关闭thrift后需要往上下文注册一个KmsClientAPI实现
     *
     * @return KmsClientAPI
     */
    @Bean
    KmsClientAPIFactoryBean getKmsClientAPI() {
        return new KmsClientAPIFactoryBean();
    }


    static void modifyClass() {
        closeThrift();
        forceDevelop();
        modifySambaMountEmpty();
        modifyRccpSubSystemAPIAdapterImpl();
        modifyFileUtil();
    }

    /**
     * 如果是非window系统sk认为是生成模式，此处本地启动时强制设置成开发模式，否则需要在环境中将有关配置拷贝到本地电脑
     */
    private static void forceDevelop() {

        ClassPool enviromentTypeClassPool = ClassPool.getDefault();
        enviromentTypeClassPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass enviromentTypeCtClass = null;
        try {
            enviromentTypeCtClass = enviromentTypeClassPool.get(Constants.ENVIROMENT_CLASS_NAME);
            enviromentTypeCtClass.getDeclaredMethod(Constants.ENVIROMENT_IS_DEVELOP_METHOD_NAME) //
                    .setBody(Constants.ENVIROMENT_IS_DEVELOP_METHOD_BODY);
            enviromentTypeCtClass.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != enviromentTypeCtClass) {
                // 释放对象
                enviromentTypeCtClass.detach();
            }
        }


    }

    private static void closeThrift() {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = null;
        try {
            ctClass = classPool.get(Constants.CONNECTORXMLCONFIGCACHE_CLS_NAME);
            ctClass.getDeclaredMethod(Constants.CONNECTORXMLCONFIGCACHE_ADDSERVICECONFIG_METHOD_NAME) //
                    .insertBefore(Constants.CONNECTORXMLCONFIGCACHE_COMMON_METHOD_BEFORE_BODY);
            ctClass.getDeclaredMethod(Constants.CONNECTORXMLCONFIGCACHE_ADDCLIENTCONFIG_METHOD_NAME) //
                    .insertBefore(Constants.CONNECTORXMLCONFIGCACHE_COMMON_METHOD_BEFORE_BODY);
            ctClass.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        } finally {
            if (ctClass != null) {
                // 释放对象
                ctClass.detach();
            }
        }
    }

    private static void modifySambaMountEmpty() {

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = null;
        try {
            ctClass = classPool.get(Constants.SAMBA_UTILS_CLASS);
            ctClass.getDeclaredMethod(Constants.SAMBA_UTILS_CLASS_MOUNT) //
                    .setBody(Constants.SAMBA_UTILS_CLASS_MOUNT_BODY);
            ctClass.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        } finally {
            if (ctClass != null) {
                // 释放对象
                ctClass.detach();
            }
        }
    }

    /**
     * 改为通过类路径读取
     */
    private static void modifyFileUtil() {

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = null;
        try {
            ctClass = classPool.get(Constants.FILE_UTIL_CLASS);
            ctClass.getDeclaredMethod(Constants.FILE_UTIL_FILL_PROPERTIES) //
                    .setBody(Constants.FILE_UTIL_FILL_PROPERTIES_BODY);
            ctClass.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        } finally {
            if (ctClass != null) {
                // 释放对象
                ctClass.detach();
            }
        }
    }

    /**
     * 改为通过类路径读取配置文件
     */
    private static void modifyRccpSubSystemAPIAdapterImpl() {

        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = null;
        try {
            ctClass = classPool.get(Constants.RCCP_SUBSYSTEMAPI_ADAPTER_CLASS);
            ctClass.getDeclaredMethod(Constants.RCCP_SUBSYSTEMAPI_ADAPTER_CLASS_OBTAINVERSION) //
                    .setBody(Constants.RCCP_SUBSYSTEMAPI_ADAPTER_CLASS_OBTAINVERSION_METHOD);
            ctClass.toClass();
        } catch (NotFoundException | CannotCompileException e) {
            throw new RuntimeException(e);
        } finally {
            if (ctClass != null) {
                // 释放对象
                ctClass.detach();
            }
        }
    }

}
