package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.conversion;

import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;

/**
 * Description: IdLabelConversion <br>
 * Copyright: Copyright (c) 2023 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2023/12/5 <br>
 * @param <K> 泛型
 * @author fang
 */
public interface IdLabelConversion<K> {

    Integer MAX_LABEL_SHOW_NUM = 20;

    /**
     *
     * @param idArr 数组
     * @return 返回值
     */
    GenericIdLabelEntry<K>[] convert(K[] idArr);
}
