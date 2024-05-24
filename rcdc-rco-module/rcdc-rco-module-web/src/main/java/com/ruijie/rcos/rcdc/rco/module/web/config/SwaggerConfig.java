package com.ruijie.rcos.rcdc.rco.module.web.config;

import java.util.Date;

import com.ruijie.rcos.sk.base.util.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.ruijie.rcos.sk.base.springboot.SpringBootSource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description: Swagger 开关/配置
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.10
 *
 * @author linhj
 */
@Configuration
@EnableKnife4j
@EnableSwagger2
@SpringBootSource
public class SwaggerConfig {

    /**
     * 本次服务启动时间记录
     */
    public static final String COMPLIE_DATE = DateUtils.format(new Date(), DateUtils.NORMAL_DATE_FORMAT);

}
