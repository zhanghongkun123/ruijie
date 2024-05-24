package com.ruijie.rcos.rcdc.rco.module.def.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Description: AES秘钥key工具类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/3
 *
 * @author xiao'yong'deng
 */
public class AesPasswordKeyUtils {

    public static final char[] CHAR_ARR = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
        't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
        'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 生成指定长度秘钥key
     * @param length 秘钥长度
     * @return 秘钥
     */
    public static String passwordKeyCreate(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(CHAR_ARR[ThreadLocalRandom.current().nextInt(CHAR_ARR.length)]);
        }
        return stringBuilder.toString();
    }
}
