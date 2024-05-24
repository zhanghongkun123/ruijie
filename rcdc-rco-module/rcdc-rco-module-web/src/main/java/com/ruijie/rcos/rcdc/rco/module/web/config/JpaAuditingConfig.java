package com.ruijie.rcos.rcdc.rco.module.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ruijie.rcos.sk.base.springboot.SpringBootSource;

/**
 * Description: 使用Spring Data JPA完成审计功能
 * @EnableJpaAuditing注解：启用JPA审计功能开关。
 * @CreatedBy注解：创建人，当实体被insert的时候，会设置值。
 * 实现AuditorAware接口，来自定义获取用户的信息；在实际项目中需要从用户权限模块中获取到当前登录用户的实际信息；
 * @LastModifiedBy注解：最后一次修改者，当实体每次被update的时候，会设置值。
 * @CreatedDate注解：创建日期，当实体被insert的时候，会设置值。
 * @LastModifiedDate注解：最后一次修改日期，当实体每次被update的时候，会设置值。
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/15 22:20
 *
 * @author coderLee23
 */
@Configuration
@SpringBootSource
@EnableJpaAuditing
public class JpaAuditingConfig {

}
