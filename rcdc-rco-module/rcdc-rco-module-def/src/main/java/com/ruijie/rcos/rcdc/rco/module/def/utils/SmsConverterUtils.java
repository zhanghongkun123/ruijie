package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.InputType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.HttpPairRequest;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants.PLACEHOLDER_RESOLVING;

/**
 * Description: SmsConverter
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/9
 *
 * @author TD
 */
public class SmsConverterUtils {

    /**
     * 短信网关占位符
     */
    private static final PropertyPlaceholderHelper SMS_GATEWAY_PLACEHOLDER =
            new PropertyPlaceholderHelper("$$", "$$", ":", true);

    /**
     * 短信模板占位符
     */
    private static final PropertyPlaceholderHelper SMS_TEMPLATE_PLACEHOLDER =
            new PropertyPlaceholderHelper("<", ">", ":", true);

    private SmsConverterUtils() {
    }

    /**
     * pair转成Map
     * @param pairList 待转换参数
     * @param <T> 泛型
     * @return 转换的MAP
     */
    public static <T> Map<String, T> buildMap(List<Pair<String, T>> pairList) {
        Assert.notNull(pairList, "buildMap pairList can be not null");
        
        Map<String, T> map = new HashMap<>();
        for (Pair<String, T> pair : pairList) {
            map.put(pair.getFirst(), pair.getSecond());
        }
        return map;
    }

    /**
     * pair转义后，再转成Map
     *
     * @param pairRequestList 待转换参数
     * @param paramsMap   替换的参数
     * @param <T>      泛型
     * @return 转换的MAP
     */
    public static <T> Map<String, T> buildPlaceholderMap(@Nullable List<HttpPairRequest> pairRequestList, Map<String, String> paramsMap) {
        Assert.notNull(paramsMap, "paramsMap can be not null");
        if (CollectionUtils.isEmpty(pairRequestList)) {
            return Maps.newHashMap();
        }
        Map<String, String> stringMap = pairRequestList.stream().collect(Collectors.toMap(HttpPairRequest::getKey, httpPairRequest -> {
            String value = httpPairRequest.getValue();
            if (httpPairRequest.getType() == InputType.PASSWORD) {
                return AesUtil.descrypt(value, RedLineUtil.getRealAdminRedLine());
            }
            return value;
        }, (pairRequest1, pairRequest2) -> pairRequest2));
        return JSON.parseObject(SMS_GATEWAY_PLACEHOLDER.replacePlaceholders(JSON.toJSONString(stringMap), paramsMap::get),
                new TypeReference<Map<String, T>>() {
                });
    }

    /**
     * 字符串转义
     * @param template 模板内容
     * @param paramsMap 参数
     * @return 转换后的内容
     */
    public static String buildPlaceholder(String template, Map<String, String> paramsMap) {
        Assert.hasText(template, "template can be not empty");
        Assert.notNull(paramsMap, "paramsMap can be not null");
        return SMS_GATEWAY_PLACEHOLDER.replacePlaceholders(template, paramsMap::get);
    }

    /**
     * 转义短信模板内容
     * @param template 模板内容
     * @param paramsMap 参数内容
     * @return 转义的短信内容
     */
    public static String placeholderTemplate(String template, Map<String, String> paramsMap) {
        Assert.hasText(template, "template can be not null");
        Assert.notNull(paramsMap, "paramsMap can be not null");
        return SMS_TEMPLATE_PLACEHOLDER.replacePlaceholders(template, paramsMap::get);
    }

    /**
     * 获取6位的短信验证码
     * @return 验证码内容
     */
    public static String getSmsVerifyCode() {
        return String.valueOf((int)((Math.random() * 9 + 1) * Math.pow(10, 5)));
    }

    /**
     * 获取解析模板出现占位符位置
     * @param template 模板
     * @return 下标集合
     */
    public static List<Integer> getResolvingTemplateIndexList(String template) {
        Assert.hasText(template, "template can be not empty");
        List<Integer> indexList = new LinkedList<>();
        int index = 0;
        while ((index = template.indexOf(PLACEHOLDER_RESOLVING, index)) != -1) {
            indexList.add(index);
            index += PLACEHOLDER_RESOLVING.length();
        }
        return indexList;
    }

    /**
     * 指定内容按照模板解析成map：
     * value = "/000/pass:123/ooo:231/"; 
     * template = "/$$code$$/pass:$$pass$$/ooo:$$kass$$/";
     * resultMap={"code":"000","pass":"123","kass":"231"}
     * @param value 内容
     * @param template 模板
     * @return 返回值
     */
    public static Map<String, String> resolvingDataToMap(String value, String template) {
        Assert.hasText(value, "value can be not empty");
        Assert.hasText(template, "template can be not empty");
        List<Integer> indexList = getResolvingTemplateIndexList(template);
        Map<String, String> resultMap = Maps.newHashMap();
        int startIndex = indexList.get(0);
        for (int i = 0;  i < indexList.size(); i = i + 2) {
            String key = template.substring(indexList.get(i) + PLACEHOLDER_RESOLVING.length(), indexList.get(i + 1));
            if (i + 2 >= indexList.size()) {
                String endStr = template.substring(indexList.get(i + 1) + PLACEHOLDER_RESOLVING.length());
                int endIndex;
                if (StringUtils.isNotBlank(endStr)) {
                    endIndex = value.indexOf(endStr, startIndex);
                } else {
                    endIndex = value.length();
                }
                resultMap.put(key, value.substring(startIndex, endIndex));
                break;
            }
            String endStr = template.substring(indexList.get(i + 1) + PLACEHOLDER_RESOLVING.length(), indexList.get(i + 2));
            int endIndex = value.indexOf(endStr);
            resultMap.put(key, value.substring(startIndex, endIndex));
            startIndex = endIndex + endStr.length();
        }
        return resultMap;
    }

    /**
     * 给字符串增加占位符
     * @param value 字符串
     * @return 字符串
     */
    public static String resolvingJoin(String value) {
        Assert.hasText(value, "value can be not empty");
        return PLACEHOLDER_RESOLVING + value + PLACEHOLDER_RESOLVING;
    }

    /**
     * 判断输入的字符串是否包含中文
     * @param value 字符串
     * @return 是否包含中文
     */
    public static boolean isChineseStr(String value) {
        Assert.hasText(value, "value can be not empty");
        return value.chars().anyMatch(ch -> isChinese((char) ch));
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }
}
