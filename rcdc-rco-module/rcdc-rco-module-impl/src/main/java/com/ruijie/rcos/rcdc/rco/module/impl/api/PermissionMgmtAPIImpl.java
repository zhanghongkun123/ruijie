package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.common.utils.SysInfoUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.MenuType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.global.GlobalParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/30 15:09
 *
 * @author linrenjian
 */
public class PermissionMgmtAPIImpl implements PermissionMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionMgmtAPIImpl.class);

    @Autowired
    private IacPermissionMgmtAPI basePermissionMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private IacPermissionMgmtAPI iacPermissionMgmtAPI;

    @Override
    public List<IacPermissionDTO> listAllPermissionByServerModel() throws BusinessException {
        // 查询所有权限
        List<IacPermissionDTO> iacPermissionList = iacPermissionMgmtAPI.listAllPermissionBySubSystem(SubSystem.CDC);
        // 先过滤下哪些权限的全局开关配置成关闭的
        iacPermissionList = filterPermissionListFromGlobalEnable(iacPermissionList);
        List<IacPermissionDTO> newBasePermissionList = new ArrayList<>();
        LOGGER.debug("获取不支持的服务器模式的菜单");

        // 获取不支持的服务器模式的菜单
        List<String> currentServerModelUnsupportedMenuNameList = getCurrentServerModelUnsupportedMenuNameList();
        for (IacPermissionDTO iacPermissionDTO : iacPermissionList) {
            // 全都不满足才会返回true 才能进行添加
            boolean enableAdd =
                    currentServerModelUnsupportedMenuNameList.stream().noneMatch(menu -> iacPermissionDTO.getPermissionCode().equals(menu));
            if (enableAdd) {
                newBasePermissionList.add(iacPermissionDTO);
            }
        }
        LOGGER.debug("处理完成服务器不支持菜单");
        return newBasePermissionList;
    }


    @Override
    public List<IacPermissionDTO> listPermissionFilterByBaseAdminDTO(List<IacPermissionDTO> list, String userName) {
        Assert.notNull(list, "list must not be null");
        Assert.notNull(userName, "userName is not null");

        // 如果是内置的管理员 cms后台可以访问 ，不是内置的管理员 需要过滤
        if (!DefaultAdmin.ADMIN.getName().equals(userName)) {
            List<String> adminMenuNameList = Arrays.asList(MenuType.CMS_MANAGE.getMenuName(), MenuType.UWS_MANAGE.getMenuName(),
                    MenuType.TERMINAL_CONFIG.getMenuName());
            return list.stream().filter(basePermissionDTO ->
                    !adminMenuNameList.contains(basePermissionDTO.getPermissionCode())
            ).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<String> getCurrentServerModelUnsupportedMenuNameList() {
        String serverModel = serverModelAPI.getServerModel();
        List<String> unsupportedMenuList = new ArrayList<>();
        // 如果是ARM架构
        if (SysInfoUtils.cpuArchTypeEqArm()) {
            // 不支持UWS
            unsupportedMenuList.add(MenuType.UWS_MANAGE.getMenuName());
            // 且非MINI 则添加不支持cms (由于X86 VDI IDV 都是支持CMS)
            if (!ServerModelEnum.MINI_SERVER_MODEL.getName().equals(serverModel)) {
                unsupportedMenuList.add(MenuType.CMS_MANAGE.getMenuName());

            }
        }
        // 正常 添加不支持类型
        unsupportedMenuList.addAll(Stream.of(MenuType.values())
                .filter(type -> type.getSupportServerModel().stream().map(ServerModelEnum::getName).noneMatch(name -> name.equals(serverModel)))
                .map(MenuType::getMenuName).collect(Collectors.toList()));
        return unsupportedMenuList;
    }

    @Override
    public List<IacPermissionDTO> listPermissionByIdArrAndServerModel(UUID[] uuidArr) throws BusinessException {
        Assert.notEmpty(uuidArr, "uuidArr is not empty");

        // 根据传入权限ID集合查询权限信息
        List<IacPermissionDTO> basePermissionList = basePermissionMgmtAPI.listPermissionByIdArr(uuidArr);
        List<IacPermissionDTO> newBasePermissionList = new ArrayList<>();
        LOGGER.debug("根据传入权限ID集合查询权限信息，获取不支持的服务器模式的菜单");
        // 获取不支持的服务器模式的菜单
        List<String> currentServerModelUnsupportedMenuNameList = getCurrentServerModelUnsupportedMenuNameList();
        for (IacPermissionDTO basePermissionDTO : basePermissionList) {
            // 全都不满足才会返回true 才能进行添加
            boolean enableAdd =
                    currentServerModelUnsupportedMenuNameList.stream().noneMatch(menu -> basePermissionDTO.getPermissionCode().equals(menu));
            if (enableAdd) {
                newBasePermissionList.add(basePermissionDTO);
            }
        }
        LOGGER.debug("根据传入权限ID集合查询权限信息，处理完成服务器不支持菜单");
        return newBasePermissionList;
    }


    /**
     *
     * 根据权限配置的全局开关做过滤
     * @param basePermissionList
     * @return
     */
    private List<IacPermissionDTO> filterPermissionListFromGlobalEnable(List<IacPermissionDTO> basePermissionList) {
        Set<String> globalEnableSearchKeys = new HashSet<>();
        basePermissionList.stream().forEach(dto -> {
            String globalEnableSearchKey = getGlobalEnableSearchKey(dto);
            if (globalEnableSearchKey != null) {
                globalEnableSearchKeys.add(globalEnableSearchKey);
            }
        });

        // 如果没有配置，直接返回
        if (globalEnableSearchKeys.isEmpty()) {
            return basePermissionList;
        }
        List<GlobalParameterDTO> globalParameterDTOList = globalParameterService.findParameters(globalEnableSearchKeys);
        Map<String, Boolean> globalEnableMap = globalParameterDTOList.stream().collect(Collectors.toMap(GlobalParameterDTO::getParamKey,
            dto -> (BooleanUtils.toBoolean(dto.getParamValue()))));


        //过滤权限列表
        List<IacPermissionDTO> newBasePermissionDTOList = new ArrayList<>();
        basePermissionList.stream().forEach(dto -> {
            String globalEnableSearchKey = getGlobalEnableSearchKey(dto);
            if (globalEnableSearchKey == null || globalEnableMap.get(globalEnableSearchKey)) {
                newBasePermissionDTOList.add(dto);
            }
        });
        return newBasePermissionDTOList;
    }


    /**
     * 获取配置了业务全局开关-对应的key
     *
     * @param dto
     * @return
     */
    private String getGlobalEnableSearchKey(IacPermissionDTO dto) {
        String globalEnableSearchKey = null;
        JSONObject jsonObject = (JSONObject) dto.getTags();
        if (jsonObject == null) {
            //没有设置自定义标签，直接返回空
            return null;
        }
        Object globalEnableSearch = jsonObject.get(FunTypes.GLOBAL_ENABLE_SEARCH);
        if (Objects.nonNull(globalEnableSearch)
            && FunTypes.YES.equals(String.valueOf(globalEnableSearch))
            && Objects.nonNull(jsonObject.get(FunTypes.GLOBAL_ENABLE_SEARCH_KEY))) {
            globalEnableSearchKey = String.valueOf(jsonObject.get(FunTypes.GLOBAL_ENABLE_SEARCH_KEY));

        }
        return globalEnableSearchKey;
    }
}
