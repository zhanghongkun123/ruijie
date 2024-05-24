package com.ruijie.rcos.rcdc.rco.module.impl.thirdparty.utils;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description: 签名工具类
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2023/8/22 19:47
 *
 * @author zjy
 */
public class ThirdPartyShineAssertMgmtSignUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ThirdPartyShineAssertMgmtSignUtil.class);

    /**
     * 获取签名
     * @param params 信息
     * @return 签名
     */
    public static String getSign(JSONObject params) {
        Assert.notNull(params, "params can not be null");

        StringBuffer str = new StringBuffer();
        List<String> keyList = new ArrayList<>(params.keySet());
        Collections.sort(keyList);
        for (int i = 0; i < keyList.size(); i++) {
            str.append(keyList.get(i) + "=" + params.get(keyList.get(i)));
            if (i < keyList.size() - 1) {
                str.append("&");
            }
        }
        String sign = getSignNature(str.toString()).toLowerCase();
        return sign;
    }

    private static String getSignNature(String message) {

        String signNature = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(message.getBytes());
            signNature = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("不存在指定的算法", e);
        }
        return signNature;
    }
}
