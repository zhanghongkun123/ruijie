package com.ruijie.rcos.rcdc.rco.module.web.config.define;

/**
 * Description: HTML 标签<br/>
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.10
 *
 * @author linhj
 */
public enum HtmlTag {

    LOPENING("<"), // 左尖括号
    ROPENING(">"), // 右尖括号
    ONEBLANK(" "), // 空格
    LBRACE("{"), // 左大括号
    RBRACE("}"), // 右大括号
    ROPENING_WITH_LSPRIT("/>"), // 右大括号
    LOPENING_WITH_RSPRIT("</"), // 左大括号
    BR("br"), // 换行
    B("b"), // 加粗
    STYLE("style"), // 样式
    DIV("div"), // 容器
    DL("dl"), // 表单
    DT("dt"), // 表单
    DD("dd"), // 表单
    TH("th"), // 表单
    TR("tr"), // 表单
    TD("td"), // 表单
    SMALL("small"), // 字体
    H3("h3"), // 标题
    TABLE("table"), // 表单
    THEAD("thead"), // 表单
    TBODY("tbody"); // 表单

    private final String tagName;

    HtmlTag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 提供标签
     * 
     * @return 返回
     */
    public String show() {
        return tagName;
    }
}
