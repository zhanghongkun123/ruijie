package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/3/7
 *
 * @author Jarman
 */
public interface UserService {


    /**
     * 统计获取用户绑定的云桌面个数,包括正在创建的云桌面个数（不包括回收站里的桌面和池桌面）
     * 
     * @param userId 用户id
     * @return 返回用户桌面个数
     */
    long countUserDesktopNumContainCreatingNum(UUID userId);

    /**
     * 获取创建中的云桌面个数
     * 
     * @param userId 用户id
     * @return 返回桌面个数
     */
    Integer getCreatingDesktopNum(UUID userId);

    /**
     * 获取用户所属分组，包括所有分组层级
     *
     * @param userId 用户id
     * @return 返回分组数组，0下标为根分组
     * @throws BusinessException 业务异常
     */
    String[] getUserGroupNameArr(UUID userId) throws BusinessException;

    /**
     * 删除云桌面
     * @param desktopId 桌面id
     * @param deskType 桌面类型
     * @throws BusinessException 业务异常
     */
    void deleteDesktop(UUID desktopId, CbbCloudDeskType deskType) throws BusinessException ;

    /**
     * 根据用户id获取用户基本信息
     *
     * @param userId 用户id
     * @Date 2022/1/14 10:56
     * @Author zjy
     * @return 返回值
     **/
    RcoViewUserEntity getUserInfoById(UUID userId);

    /**
     * 根据用户名获取用户基本信息
     *
     * @param userName 用户名
     * @Date 2022/1/14 10:56
     * @Author zjy
     * @return 返回值
     **/
    RcoViewUserEntity getUserInfoByName(String userName);

    /**
     * 根据用户id列表获取用户基本信息
     *
     * @param userIdList 用户id列表
     * @Date 2022/1/14 10:56
     * @Author zjy
     * @return 返回值
     **/
    List<RcoViewUserEntity> getUserInfoByIdList(List<UUID> userIdList);

    /**
     * 判断RcoViewUserEntity表是否为空
     *
     * @return 是否为空
     */
    boolean isRcoViewUserEntityEmpty();

    /**
     * 检查这些组ID是否都存在
     *
     * @param idList idList
     * @return true都存在，false有不存在的userId
     * @throws BusinessException ex
     */
    boolean checkAllUserGroupExistByIds(List<UUID> idList) throws BusinessException;

    /**
     * 查询用户组列表
     *
     * @param groupIdList groupIdList
     * @return List<CbbUserGroupDetailDTO>
     * @throws BusinessException BusinessException
     */
    List<IacUserGroupDetailDTO> listUserGroupByIds(List<UUID> groupIdList) throws BusinessException;

    /**
     * 查询用户列表
     *
     * @param idList idList
     * @return List<BaseUserDetailDTO>
     * @throws BusinessException BusinessException
     */
    List<IacUserDetailDTO> listUserByIds(List<UUID> idList) throws BusinessException;

    /**
     * 检查这些userId是否都存在
     *
     * @param idList idList
     * @return true都存在，false有不存在的userId
     */
    boolean checkAllUserExistByIds(List<UUID> idList);

    /**
     * 检查这些userId是否存在访客
     *
     * @param idList idList
     * @return true存在，false不存在
     */
    boolean checkAnyVisitor(List<UUID> idList);


    /**
     * 根据用户类型查询有使用云桌面的用户
     * @param desktopNum  云桌面数
     * @param userTypeList 用户类型
     * @return 用户数
     */
    Long findByDesktopNumAndUserType(int desktopNum, List<IacUserTypeEnum> userTypeList);

    /**
     * 分页查询用户组下的用户，一页查1000条
     * 
     * @param groupId 用户组id
     * @param page 页码，从0开始
     * @return 返回分页数据
     * @throws BusinessException 业务异常
     */
    IacPageResponse<IacUserDetailDTO> pageQueryByGroupId(UUID groupId, Integer page) throws BusinessException;

    /**
     * 根据用户名获取用户详细信息
     * 
     * @param userName 用户名
     * @return IacUserDetailDTO
     */
    IacUserDetailDTO getUserDetailByName(String userName);
}
