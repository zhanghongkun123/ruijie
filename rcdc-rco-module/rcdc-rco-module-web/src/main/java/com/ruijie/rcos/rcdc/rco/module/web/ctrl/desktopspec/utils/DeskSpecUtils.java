package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopspec.utils;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuModelType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.VgpuExtraInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.response.VgpuVO;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/23
 *
 * @author linke
 */
public class DeskSpecUtils {

    private static final String ITEM_ARR = "itemArr";

    private static final String VGPU_FORMAT = "%.2f%s";

    private static final String VGPU_GB = "GB";

    private static final double VGPU_SIZE = 1.0 * 1024 * 1024 * 1024;

    /**
     * 构建VGpuInfoDTO
     *
     * @param vgpuType vgpuType
     * @param vgpuExtraInfoDTO vgpuExtraInfoDTO
     * @return VgpuInfoDTO
     */
    public static VgpuInfoDTO buildVGpuInfoDTO(@Nullable String vgpuType, @Nullable VgpuExtraInfoDTO vgpuExtraInfoDTO) {
        if (StringUtils.isEmpty(vgpuType) || Objects.isNull(vgpuExtraInfoDTO)) {
            return new VgpuInfoDTO();
        }

        VgpuInfoDTO vgpuInfoDTO = new VgpuInfoDTO();
        Long vgpuSize = vgpuExtraInfoDTO.getGraphicsMemorySize();
        if (StringUtils.isEmpty(vgpuType) || VgpuType.QXL.name().equals(vgpuType) || Objects.isNull(vgpuSize)) {
            return vgpuInfoDTO;
        }
        vgpuInfoDTO.setVgpuType(VgpuType.valueOf(vgpuType));
        VgpuExtraInfo vgpuExtraInfo = new VgpuExtraInfo();
        vgpuExtraInfo.setGraphicsMemorySize(vgpuSize);
        vgpuExtraInfo.setVgpuModelType(Objects.isNull(vgpuExtraInfoDTO.getVgpuModelType()) ? VgpuModelType.G : vgpuExtraInfoDTO.getVgpuModelType());
        vgpuExtraInfo.setModel(vgpuExtraInfoDTO.getModel());
        vgpuExtraInfo.setParentGpuModel(vgpuExtraInfoDTO.getParentGpuModel());
        vgpuInfoDTO.setVgpuExtraInfo(vgpuExtraInfo);
        return vgpuInfoDTO;
    }

    /**
     * 转换GPU值
     *
     * @param graphicsMemorySize  显存大小（B）
     * @return GB大小展示值
     */
    public static String formatGpuSize(Long graphicsMemorySize) {
        Assert.notNull(graphicsMemorySize, "graphicsMemorySize must not be null");
        double gbSize = graphicsMemorySize / VGPU_SIZE;
        return String.format(VGPU_FORMAT, gbSize, VGPU_GB);
    }

    /**
     * 转换返回给页面的GPU选项
     *
     * @param vgpuVOList  List<VgpuDTO>
     * @return Map<String, List<VgpuDTO>>
     */
    public static Map<String, List<VgpuVO>> convert2VgpuItemResponse(List<VgpuVO> vgpuVOList) {
        Assert.notNull(vgpuVOList, "vgpuDTOList must not be null");
        return ImmutableMap.of(ITEM_ARR, vgpuVOList);
    }
}
