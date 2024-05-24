package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description: 桌面查询工具类
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/24
 *
 * @author linke
 */
public class DesktopQueryUtil {

    /**
     * 构建排序
     * @param sortArr sortArr
     * @return Sort[]
     */
    public static Sort[] generateDesktopSortArr(@Nullable Sort[] sortArr) {
        if (sortArr == null) {
            Sort sortFirst = new Sort();
            sortFirst.setSortField("faultState");
            sortFirst.setDirection(Sort.Direction.DESC);

            Sort sortSecond = new Sort();
            sortSecond.setSortField("latestLoginTime");
            sortSecond.setDirection(Sort.Direction.DESC);

            Sort sortThird = new Sort();
            sortThird.setSortField("createTime");
            sortThird.setDirection(Sort.Direction.DESC);

            return new Sort[] {sortFirst, sortSecond, sortThird};
        }
        List<Sort> sortList = new ArrayList<>();
        Sort sortFirst = new Sort();
        sortFirst.setSortField("faultState");
        sortFirst.setDirection(Sort.Direction.DESC);
        sortList.add(sortFirst);
        for (Sort sort : sortArr) {
            sortList.add(sort);
        }
        return sortList.toArray(new Sort[sortList.size()]);
    }

    /**
     * 云桌面列表信息修订
     *
     * @param response Web 封装的出参
     */
    public static void convertMoreAccurateCloudDesktopInfo(DefaultPageResponse<CloudDesktopDTO> response) {
        Assert.notNull(response, "response is null");

        if (ArrayUtils.isEmpty(response.getItemArr())) {
            return;
        }

        for (CloudDesktopDTO item : response.getItemArr()) {
            if (DesktopType.APP_LAYER.toString().equals(item.getDesktopType()) && Objects.nonNull(item.getSystemDisk())) {
                item.setSystemDisk(item.getSystemDisk() + Constants.SYSTEM_DISK_CAPACITY_INCREASE_SIZE);
            }
            if (CbbCloudDeskType.IDV.name().equals(item.getDesktopCategory())) {
                if (StringUtils.hasText(item.getImageName())) {
                    // 直接通过镜像获取
                    item.setDesktopCategory(item.getCbbImageType());
                }
            }
        }
    }
}
