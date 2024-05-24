package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public final class DefaultExactMatch extends AbstractMatch implements ExactMatch {

    @NotBlank
    private String fieldName;

    @NotEmpty
    private Object[] valueArr;

    @NotNull
    private DefaultExactMatch.Rule matchRule;

    private DefaultExactMatch(String name, DefaultExactMatch.Rule matchRule, Object... valueArr) {
        this();
        Assert.hasText(name, "name is not blank");
        Assert.notEmpty(valueArr, "valueArr is not empty");
        Assert.notNull(matchRule, "matchRule is not null");
        this.fieldName = name;
        this.valueArr = valueArr;
        this.matchRule = matchRule;
    }

    private DefaultExactMatch() {
        super(Match.Type.EXACT);
        this.matchRule = DefaultExactMatch.Rule.EQ;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValueArr(Object[] valueArr) {
        this.valueArr = valueArr;
    }

    public DefaultExactMatch.Rule getMatchRule() {
        return this.matchRule;
    }

    public void setMatchRule(DefaultExactMatch.Rule matchRule) {
        this.matchRule = matchRule;
    }

    /**
     * 相等操作
     * @param name name
     * @param valueArr valueArr
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    public static DefaultExactMatch eq(String name, Object... valueArr) {
        Assert.hasText(name, "name is not blank");
        Assert.notEmpty(valueArr, "valueArr is not empty");
        Assert.noNullElements(valueArr, "valueArr no null elements");
        return new DefaultExactMatch(name, DefaultExactMatch.Rule.EQ, valueArr);
    }

    /**
     * 不相等操作
     * @param name name
     * @param valueArr valueArr
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    public static DefaultExactMatch neq(String name, Object... valueArr) {
        Assert.hasText(name, "name is not blank");
        Assert.notEmpty(valueArr, "valueArr is not empty");
        Assert.noNullElements(valueArr, "valueArr no null elements");
        return new DefaultExactMatch(name, DefaultExactMatch.Rule.NEQ, valueArr);
    }

    /**
     * in操作
     * @param fieldName name
     * @param valueArr valueArr
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    public static DefaultExactMatch in(String fieldName, Object... valueArr) {
        Assert.hasText(fieldName, "fieldName is not empty");
        Assert.notEmpty(valueArr, "valueArr is not null");
        return new DefaultExactMatch(fieldName, DefaultExactMatch.Rule.EQ, valueArr);
    }

    /**
     * nin操作
     * @param fieldName name
     * @param valueArr valueArr
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    public static DefaultExactMatch nin(String fieldName, Object... valueArr) {
        Assert.hasText(fieldName, "fieldName is not empty");
        Assert.notEmpty(valueArr, "valueArr is not null");
        return new DefaultExactMatch(fieldName, DefaultExactMatch.Rule.NEQ, valueArr);
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public Object[] getValueArr() {
        return ArrayUtils.clone(this.valueArr);
    }

    /**
     * 规则枚举类型
     */
    public enum Rule {
        EQ,
        NEQ;

        Rule() {
        }
    }
}
