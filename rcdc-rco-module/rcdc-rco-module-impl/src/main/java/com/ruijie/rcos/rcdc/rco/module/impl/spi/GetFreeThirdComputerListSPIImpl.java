package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.spi.GetFreeThirdComputerListSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 获取离线+无工作模式的第三方主机
 * Copyright: Copyright (c) 2024
 * Company: RuiJie Co., Ltd.
 * Create Time: 2024年04月09日
 *
 * @author liuwc
 */
public class GetFreeThirdComputerListSPIImpl implements GetFreeThirdComputerListSPI {

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Override
    public List<UUID> getFreeThirdComputerList() {
        List<ComputerStateEnum> computerStateEnumList = new ArrayList<>();
        List<ComputerEntity> computerEntityList =
                computerBusinessDAO.findByWorkModelIsNullAndTypeAndState(ComputerTypeEnum.THIRD, ComputerStateEnum.ONLINE);

        return computerEntityList.stream().map(ComputerEntity::getId).collect(Collectors.toList());
    }
}
