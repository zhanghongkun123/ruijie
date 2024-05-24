package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.SearchModuleEnum;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
public class SearchResultForWebDTO {

    @Enumerated(EnumType.STRING)
    private SearchModuleEnum module;

    private IdLabelStringEntry[] optionArr;

    public SearchResultForWebDTO(SearchModuleEnum module, IdLabelStringEntry[] optionArr) {
        this.module = module;
        this.optionArr = optionArr;
    }

    public SearchModuleEnum getModule() {
        return module;
    }

    public void setModule(SearchModuleEnum module) {
        this.module = module;
    }

    public IdLabelStringEntry[] getOptionArr() {
        return optionArr;
    }

    public void setOptionArr(IdLabelStringEntry[] optionArr) {
        this.optionArr = optionArr;
    }
}
