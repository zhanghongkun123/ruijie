package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: CMS登录响应码
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/22 20:05
 *
 * @author yxq
 */
public interface CmsLoginCode extends CommonMessageCode {

    /**
     * 管理员被锁定
     */
    Integer ADMIN_LOCKED = 12;

    /**
     * 提示管理员剩余错误次数
     */
    Integer REMIND_ADMIN_REMAINING_TIMES = 13;
}
