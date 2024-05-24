package com.ruijie.rcos.rcdc.rco.module.def.annotation;

import java.lang.annotation.*;

/**
* @ClassName:  ExcelHead
* @Description: 用于生成excel头部信息
* @author: zhiweiHong
* @date:   2020/8/26
**/
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelHead {
    String value() default "";
}
