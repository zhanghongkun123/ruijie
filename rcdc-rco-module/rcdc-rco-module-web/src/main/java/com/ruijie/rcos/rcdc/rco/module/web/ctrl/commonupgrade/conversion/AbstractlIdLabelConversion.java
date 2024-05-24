package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.conversion;

import com.google.common.collect.Lists;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;

import java.util.List;

/**
 * Description:  <br>
 * Copyright: Copyright (c) 2023 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2023/12/5 <br>
 * @param <K> 泛型
 * @author fang
 */

public abstract class AbstractlIdLabelConversion<K> implements IdLabelConversion<K> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractlIdLabelConversion.class);

    @Override
    public GenericIdLabelEntry<K>[] convert(K[] idArr) {

        // 至多返回20条label记录，剩余则为空
        int limitSize = MAX_LABEL_SHOW_NUM;
        List<GenericIdLabelEntry<K>> idLabelEntryList = Lists.newArrayList();
        for (K id : idArr) {
            GenericIdLabelEntry<K> idLabelEntry = new GenericIdLabelEntry<>();
            idLabelEntry.setId(id);
            if (limitSize > 0) {
                try {
                    String label = getLabel(id);
                    idLabelEntry.setLabel(label);
                    limitSize--;
                } catch (BusinessException e) {
                    LOGGER.error("查询ID为{}的基本信息发生异常{}", id, e.getI18nMessage());
                }
            }
            idLabelEntryList.add(idLabelEntry);
        }
        return idLabelEntryList.toArray(new GenericIdLabelEntry[0]);
    }

    protected abstract String getLabel(K id) throws BusinessException;
}
