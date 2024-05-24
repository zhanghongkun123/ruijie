package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeBuilder;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo.PermissionTreeVO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/07/30
 *
 * @author linrenjian
 */
public class PermissionFilterDTO {

    /**
     * 权限树
     */
    private PermissionTreeDTO[] permissionTreeDTOArr;


    /***
     * 构造返回权限树的一维数组
     * 
     * @param basePermissionList basePermissionList
     * @return PermissionTreeVO[]
     */
    public PermissionTreeDTO[] buildPermissionTreeDTOArr(List<IacPermissionDTO> basePermissionList) {
        Assert.notEmpty(basePermissionList, "basePermissionList must not Empty");

        // 权限树集合
        List<PermissionTreeDTO> voList = new ArrayList<>();
        // 遍历权限
        for (IacPermissionDTO dto : basePermissionList) {
            // 构造单个权限树对象
            PermissionTreeDTO permissionTreeDTO = new PermissionTreeDTO();
            // 设置ID
            permissionTreeDTO.setId(dto.getId().toString());
            // 权限名称
            permissionTreeDTO.setLabel(dto.getName());
            // 权限标签
            permissionTreeDTO.setTags((JSONObject) dto.getTags());
            // 权限码
            permissionTreeDTO.setPermissionCode(dto.getPermissionCode());
            // 如果是空的 设置为ROOT_ID
            permissionTreeDTO.setParentId(Optional.ofNullable(dto.getParentId()).map(UUID::toString).orElse(PermissionConstants.ROOT_ID));
            // 添加树对象到集合
            voList.add(permissionTreeDTO);
        }
        // 最后 添加根对象总览树
        voList.add(addRootPermissionVO());

        // 根据tags进行排序
        List<PermissionTreeDTO> collectList = voList.stream().sorted(Comparator.comparing(PermissionTreeDTO::getOrder)).collect(Collectors.toList());
        return collectList.toArray(new PermissionTreeDTO[0]);
    }



    /**
     * 添加根对象总览树
     * 
     * @return PermissionTreeVO
     */
    public PermissionTreeDTO addRootPermissionVO() {
        // 构造总览树 对象
        PermissionTreeDTO vo = new PermissionTreeDTO();
        vo.setId(PermissionConstants.ROOT_ID);
        vo.setLabel(PermissionConstants.ROOT_NAME);
        vo.setPermissionCode(PermissionConstants.ROOT_ID);
        vo.setParentId(null);
        vo.setDisabled(true);
        vo.setAllowDelete(false);
        vo.setEnableDefault(false);
        return vo;
    }

    /**
     * 构建权限树形结构
     *
     * @param permissionFilterDTO permissionFilterDTO对象
     * @return 返回树结构
     */
    public List<PermissionTreeVO> buildPermissionTree(PermissionFilterDTO permissionFilterDTO) {
        Assert.notNull(permissionFilterDTO, "userGroupVOArr cannot null");
        // 进行权限过滤以后扩展
        List<PermissionTreeVO> permissionTreeVOList = filterPermission(permissionFilterDTO);
        // 树构造器 构造树形结构
        return new TreeBuilder<>(permissionTreeVOList).build();
    }

    /**
     * 过滤权限DTO
     *
     * @param permissionFilterDTO permissionFilterDTO
     * @return List<PermissionTreeVO> 树集合
     */
    public List<PermissionTreeVO> filterPermission(PermissionFilterDTO permissionFilterDTO) {
        List<PermissionTreeVO> permissionTreeVOList = new ArrayList<>();
        // 遍历添加
        for (PermissionTreeDTO permissionTreeVO1 : permissionFilterDTO.getPermissionTreeDTOArr()) {
            PermissionTreeVO permissionTreeVO = new PermissionTreeVO();
            BeanUtils.copyProperties(permissionTreeVO1, permissionTreeVO);
            permissionTreeVOList.add(permissionTreeVO);
        }
        return permissionTreeVOList;
    }


    public PermissionTreeDTO[] getPermissionTreeDTOArr() {
        return permissionTreeDTOArr;
    }

    public void setPermissionTreeDTOArr(PermissionTreeDTO[] permissionTreeDTOArr) {
        this.permissionTreeDTOArr = permissionTreeDTOArr;
    }

}
