package com.ruijie.rcos.rcdc.rco.module.def.imageexport.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsFileState;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/24 11:32
 *
 * @author liuwang1
 */
public class PageQueryExportImageRequest extends PageSearchRequest {


    public PageQueryExportImageRequest() {

    }

    public PageQueryExportImageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    private static final String IMAGE_DISK_TYPE = "imageDiskType";

    private static final String FILE_STATE = "fileState";

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    private static final String CPU_ARCH = "cpuArch";

    @Override
    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr, "exactMatchArr must not be null");
        MatchEqual[] matchEqualArr = new MatchEqual[exactMatchArr.length];
        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = null;
            switch (exactMatch.getName()) {
                case FILE_STATE:
                    OsFileState[] osFileStateArr = new OsFileState[exactMatch.getValueArr().length];
                    for (int j = 0; j < exactMatch.getValueArr().length; j++) {
                        osFileStateArr[j] = OsFileState.valueOf(exactMatch.getValueArr()[j]);
                    }
                    matchEqual = new MatchEqual(FILE_STATE, osFileStateArr);
                    break;
                case IMAGE_DISK_TYPE:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        CbbImageDiskType[] diskTypeArr = new CbbImageDiskType[exactMatch.getValueArr().length];
                        for (int j = 0; j < exactMatch.getValueArr().length; j++) {
                            diskTypeArr[j] = CbbImageDiskType.valueOf(exactMatch.getValueArr()[j]);
                        }
                        matchEqual = new MatchEqual(IMAGE_DISK_TYPE, diskTypeArr);
                    }
                    break;
                case CPU_ARCH:
                    if (ArrayUtils.isNotEmpty(exactMatch.getValueArr())) {
                        CbbCpuArchType[] cpuArchTypeArr = new CbbCpuArchType[exactMatch.getValueArr().length];
                        for (int j = 0; j < exactMatch.getValueArr().length; j++) {
                            cpuArchTypeArr[j] = CbbCpuArchType.valueOf(exactMatch.getValueArr()[j]);
                        }
                        matchEqual = new MatchEqual(CPU_ARCH, cpuArchTypeArr);
                    }
                    break;
                case IMAGE_TEMPLATE_ID:
                    matchEqual = new MatchEqual(exactMatch.getName(),
                            Arrays.stream(exactMatch.getValueArr()).map(UUID::fromString).toArray());
                    break;
                default:
                    matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
                    break;
            }
            matchEqualArr[i] = matchEqual;
        }

        return matchEqualArr;
    }
}
