package com.ruijie.rcos.rcdc.rco.module.web.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Description: 随机字符生成
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/09/08
 *
 * @author liyonghua1
 */
public class CharRandomUtils {

    /**
     * 生成指定长度随机密码
     * @param length 密码长度
     * @return 密码
     */
    public static String passwordCreate(int length) {
        char[] charArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '`',
            '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_'};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(charArr[ThreadLocalRandom.current().nextInt(charArr.length)]);
        }
        return stringBuilder.toString();
    }
}
