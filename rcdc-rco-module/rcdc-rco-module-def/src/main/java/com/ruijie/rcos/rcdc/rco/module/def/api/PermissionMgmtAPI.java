package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/30 15:10
 *
 * @author linrenjian
 */
public interface PermissionMgmtAPI {

    /**
     * 根据服务器模式查询全部权限
     *
     * @return 权限集合
     * @throws BusinessException 业务异常
     */
    List<IacPermissionDTO> listAllPermissionByServerModel() throws BusinessException;



    /**
     * 过滤些权限
     * 
     * @param list 权限集合
     * @param userName 管理员信息
     * @return list
     */
    List<IacPermissionDTO> listPermissionFilterByBaseAdminDTO(List<IacPermissionDTO> list, String userName);

    /**
     * 获取当前服务器模式不支持的菜单列表
     *
     * @return 响应
     */
    List<String> getCurrentServerModelUnsupportedMenuNameList();


    /**
     * 根据服务器模式 通过权限ID 查询权限
     * 
     * @param uuidArr 权限ID
     * @return 权限集合
     * @throws BusinessException 异常
     */
    List<IacPermissionDTO> listPermissionByIdArrAndServerModel(UUID[] uuidArr) throws BusinessException;
}
