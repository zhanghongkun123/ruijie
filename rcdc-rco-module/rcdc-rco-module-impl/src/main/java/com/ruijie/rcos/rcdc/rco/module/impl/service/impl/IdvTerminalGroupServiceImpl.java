package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request.SaveWifiWhitelistRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IdvTerminalGroupDeskConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IdvTerminalGroupDeskConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.IdvTerminalGroupService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/12 19:28
 *
 * @author conghaifeng
 */
@Service
public class IdvTerminalGroupServiceImpl implements IdvTerminalGroupService {

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private IdvTerminalGroupDeskConfigDAO idvTerminalGroupDeskConfigDAO;

    @Autowired
    private WifiWhitelistAPI wifiWhitelistAPI;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    /**
     * 创建idv终端组
     *
     * @param request 创建idv终端组请求
     */
    @Override
    public UUID saveIdvTerminalGroup(IdvCreateTerminalGroupRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        String groupName = request.getGroupName();
        Assert.hasText(groupName, "idv terminalGroup name can not be null");
        //保存终端组
        CbbTerminalGroupDetailDTO terminalGroupDTO = cbbTerminalGroupMgmtAPI.createTerminalGroup(request);
        //获取终端组id
        UUID cbbTerminalGroupId = terminalGroupDTO.getId();
        //保存IDV终端组配置
        saveIdvDeskConfig(request, cbbTerminalGroupId);
        //保存VOI终端组配置
        saveVoiDeskConfig(request, cbbTerminalGroupId);
        //无线白名单保存
        SaveWifiWhitelistRequest saveWifiWhitelistRequest = new SaveWifiWhitelistRequest(request.getWifiWhitelistDTOList(), cbbTerminalGroupId);
        wifiWhitelistAPI.createWifiWhitelist(saveWifiWhitelistRequest);
        return cbbTerminalGroupId;
    }

    /**
     * 保存IDV 配置信息
     *
     * @param request
     * @param cbbTerminalGroupId
     */
    private void saveIdvDeskConfig(IdvCreateTerminalGroupRequest request, UUID cbbTerminalGroupId) {
        //IDV云桌面关联镜像模板id
        UUID cbbIdvDesktopImageId = request.getCbbIdvDesktopImageId();
        //IDV云桌面关联策略模板id
        UUID cbbIdvDesktopStrategyId = request.getCbbIdvDesktopStrategyId();
        //当IDV 信息都不为空 进行保存
        if (cbbIdvDesktopImageId != null && cbbIdvDesktopStrategyId != null) {
            // 保存IDV的 配置信息
            IdvTerminalGroupDeskConfigEntity entity = new IdvTerminalGroupDeskConfigEntity();
            // IDV云桌面关联镜像模板id
            entity.setCbbIdvDesktopImageId(cbbIdvDesktopImageId);
            // IDV云桌面关联策略模板id
            entity.setCbbIdvDesktopStrategyId(cbbIdvDesktopStrategyId);
            // 终端组ID
            entity.setCbbTerminalGroupId(cbbTerminalGroupId);
            //终端组类型
            entity.setDeskType(CbbTerminalPlatformEnums.IDV);
            entity.setUserProfileStrategyId(request.getCbbIdvUserProfileStrategyId());
            entity.setSoftwareStrategyId(request.getIdvSoftwareStrategyId());
            idvTerminalGroupDeskConfigDAO.save(entity);
        }
    }

    /**
     * 保存 VOI配置信息
     *
     * @param request
     * @param cbbTerminalGroupId
     */
    private void saveVoiDeskConfig(IdvCreateTerminalGroupRequest request, UUID cbbTerminalGroupId) {
        //VOI云桌面关联镜像模板id
        UUID cbbVoiDesktopImageId = request.getCbbVoiDesktopImageId();
        //VOI云桌面关联策略模板id
        UUID cbbVoiDesktopStrategyId = request.getCbbVoiDesktopStrategyId();
        //当VOI 信息都不为空 进行保存
        if (cbbVoiDesktopImageId != null && cbbVoiDesktopStrategyId != null) {
            //保存VOI的配置信息
            IdvTerminalGroupDeskConfigEntity voidEntity = new IdvTerminalGroupDeskConfigEntity();
            //VOI云桌面关联镜像模板id
            voidEntity.setCbbIdvDesktopImageId(cbbVoiDesktopImageId);
            //VOI云桌面关联策略模板id
            voidEntity.setCbbIdvDesktopStrategyId(cbbVoiDesktopStrategyId);
            //终端组ID
            voidEntity.setCbbTerminalGroupId(cbbTerminalGroupId);
            //终端组类型
            voidEntity.setDeskType(CbbTerminalPlatformEnums.VOI);
            voidEntity.setSoftwareStrategyId(request.getVoiSoftwareStrategyId());
            voidEntity.setUserProfileStrategyId(request.getCbbVoiUserProfileStrategyId());
            idvTerminalGroupDeskConfigDAO.save(voidEntity);
        }

    }

    @Override
    public void editIdvTerminalGroup(IdvEditTerminalGroupRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        // 编辑idv终端组
        cbbTerminalGroupMgmtAPI.editTerminalGroup(request);
        // 编辑idv终端组配置
        editGroupConfig(request);
        // 更新白名单相关配置
        SaveWifiWhitelistRequest saveWifiWhitelistRequest = new SaveWifiWhitelistRequest(request);
        wifiWhitelistAPI.updateWifiWhitelist(saveWifiWhitelistRequest);
    }

    private void editGroupConfig(IdvEditTerminalGroupRequest request) {
        Boolean isGlobalSoftwareStrategy = softwareControlMgmtAPI.getGlobalSoftwareStrategy();
        //查询IDV 终端配置
        IdvTerminalGroupDeskConfigEntity entity = checkConfigExist(CbbTerminalPlatformEnums.IDV, request.getId());
        entity.setCbbIdvDesktopStrategyId(request.getCbbIdvDesktopStrategyId());
        entity.setCbbIdvDesktopImageId(request.getCbbIdvDesktopImageId());
        entity.setUserProfileStrategyId(request.getCbbIdvUserProfileStrategyId());
        entity.setDeskType(CbbTerminalPlatformEnums.IDV);
        if (isGlobalSoftwareStrategy) {
            entity.setSoftwareStrategyId(request.getIdvSoftwareStrategyId());
        }
        //并新增OR修改
        idvTerminalGroupDeskConfigDAO.save(entity);

        //查询VOI终端配置
        IdvTerminalGroupDeskConfigEntity voiEntity = checkConfigExist(CbbTerminalPlatformEnums.VOI, request.getId());
        voiEntity.setCbbIdvDesktopStrategyId(request.getCbbVoiDesktopStrategyId());
        voiEntity.setCbbIdvDesktopImageId(request.getCbbVoiDesktopImageId());
        voiEntity.setUserProfileStrategyId(request.getCbbVoiUserProfileStrategyId());
        voiEntity.setDeskType(CbbTerminalPlatformEnums.VOI);
        if (isGlobalSoftwareStrategy) {
            voiEntity.setSoftwareStrategyId(request.getVoiSoftwareStrategyId());
        }
        //并新增OR修改
        idvTerminalGroupDeskConfigDAO.save(voiEntity);
    }

    private IdvTerminalGroupDeskConfigEntity checkConfigExist(CbbTerminalPlatformEnums type, UUID groupId) {
        List<IdvTerminalGroupDeskConfigEntity> configList =
                idvTerminalGroupDeskConfigDAO.findTerminalGroupDeskConfigEntityByCbbTerminalGroupId(groupId);
        // 集合为空 则创建一个新对象 用于保存
        if (CollectionUtils.isEmpty(configList)) {
            IdvTerminalGroupDeskConfigEntity newEntity = new IdvTerminalGroupDeskConfigEntity();
            newEntity.setCbbTerminalGroupId(groupId);
            return newEntity;
        }
        // 终端配置实体
        IdvTerminalGroupDeskConfigEntity newEntity = null;
        // 遍历集合 是否有符合的
        for (int i = 0; i < configList.size(); i++) {
            // 当桌面类型一样 赋值
            if (type == configList.get(i).getDeskType()) {
                newEntity = configList.get(i);
            }
        }
        // 如果从集合查询出来的 对象不存在 则创建
        if (newEntity == null) {
            newEntity = new IdvTerminalGroupDeskConfigEntity();
            newEntity.setCbbTerminalGroupId(groupId);
        }
        return newEntity;
    }

    /**
     * 删除idv终端组
     *
     * @param id 请求参数
     * @throws BusinessException 业务异常
     */
    @Override
    public void deleteTerminalGroupDesktopConfig(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        // 检测终端组配置是否存在
        if (idvTerminalGroupDeskConfigDAO.findTerminalGroupDeskConfigEntityByCbbTerminalGroupId(id) != null) {
            // 删除终端组配置
            idvTerminalGroupDeskConfigDAO.deleteByCbbTerminalGroupId(id);
        }
    }
}
