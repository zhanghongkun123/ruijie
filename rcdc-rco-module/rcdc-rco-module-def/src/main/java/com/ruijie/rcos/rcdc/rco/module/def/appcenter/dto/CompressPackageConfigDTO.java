package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.rco.module.def.enums.CompressReplaceStrategy;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月04日
 *
 * @author chenl
 */
public class CompressPackageConfigDTO {

    @Nullable
    private Boolean enableAutoDecompress;

    @Nullable
    private String decompressPath;

    @Nullable
    private CompressReplaceStrategy replaceStrategy;

    /**
     * 是否删除源文件
     */
    @Nullable
    private Boolean enableDeleteOriginalFile;

    public Boolean getEnableAutoDecompress() {
        return enableAutoDecompress;
    }

    public void setEnableAutoDecompress(Boolean enableAutoDecompress) {
        this.enableAutoDecompress = enableAutoDecompress;
    }

    public String getDecompressPath() {
        return decompressPath;
    }

    public void setDecompressPath(String decompressPath) {
        this.decompressPath = decompressPath;
    }

    public CompressReplaceStrategy getReplaceStrategy() {
        return replaceStrategy;
    }

    public void setReplaceStrategy(CompressReplaceStrategy replaceStrategy) {
        this.replaceStrategy = replaceStrategy;
    }

    @Nullable
    public Boolean getEnableDeleteOriginalFile() {
        return enableDeleteOriginalFile;
    }

    public void setEnableDeleteOriginalFile(@Nullable Boolean enableDeleteOriginalFile) {
        this.enableDeleteOriginalFile = enableDeleteOriginalFile;
    }
}
