package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/27
 *
 * @author zqj
 */
public class PasswordUtil {

    /**
     * 检查新旧密码是否一致
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 是否一致
     */
    public static boolean checkNewOldPwdEquals(String oldPwd, String newPwd) {
        Assert.hasText(oldPwd, "oldPwd can be not null");
        Assert.hasText(newPwd, "newPwd can be not null");
        String oldPwdTxt = AesUtil.descrypt(oldPwd, RedLineUtil.getRealUserRedLine());
        String newPwdTxt = AesUtil.descrypt(newPwd, RedLineUtil.getRealUserRedLine());
        return Objects.equals(oldPwdTxt, newPwdTxt);
    }
}
