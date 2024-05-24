package com.ruijie.rcos.rcdc.rco.module.def.printer.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/13
 *
 * @author zhangsiming
 */
public class PrinterConfigMigrateDTO {

    @NotNull
    private Boolean enable;

    @Nullable
    private List<PrinterConfigMigrateItemDTO> printerList;


    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<PrinterConfigMigrateItemDTO> getPrinterList() {
        return printerList;
    }

    public void setPrinterList(List<PrinterConfigMigrateItemDTO> printerList) {
        this.printerList = printerList;
    }
}
