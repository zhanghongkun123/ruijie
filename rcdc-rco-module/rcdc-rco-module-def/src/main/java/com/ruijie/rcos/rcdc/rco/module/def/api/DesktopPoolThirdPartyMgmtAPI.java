package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolAssignResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolThirdPartyBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 第三方桌面池相关API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public interface DesktopPoolThirdPartyMgmtAPI {


    /**
     * 查询桌面池关联PC终端或PC终端组列表
     *
     * @param desktopPoolId 桌面池Id
     * @param relatedType   关联类型
     * @return List<DesktopPoolUserDTO>
     */
    List<DesktopPoolComputerDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable ComputerRelatedType relatedType);


    /**
     * 检查对象是否和桌面池有绑定关系
     *
     * @param desktopPoolId 分页参数
     * @param userId        用户ID
     * @return true已绑定池，false未绑定
     */
    boolean checkUserInDesktopPool(UUID desktopPoolId, UUID userId);


    /**
     * 获取用户组下已分配的用户数量
     *
     * @param groupId       用户组ID
     * @param desktopPoolId 桌面池ID
     * @return 用户组下已分配的用户数量
     */
    Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId);

    /**
     * 根据桌面池ID获取关联用户组下已分配的用户数量
     *
     * @param desktopPoolId 桌面池ID
     * @return List<UserGroupAssignedUserNumDTO> 用户组下已分配的用户数量
     */
    List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDesktopPoolId(UUID desktopPoolId);

    /**
     * 修改桌面池绑定对像关联关系
     *
     * @param updatePoolBindObjectDTO UpdatePoolBindObjectDTO
     * @throws BusinessException 业务异常
     */
    void updatePoolBindObject(UpdatePoolThirdPartyBindObjectDTO updatePoolBindObjectDTO) throws BusinessException;


    /**
     * 根据池ID获取有分配关系的终端组
     *
     * @param desktopPoolId 池ID
     * @return 用户组集合
     */
    Set<String> getDesktopPoolRelationComputerGroup(UUID desktopPoolId);


    /**
     * 移动桌面
     *
     * @param desktopDetailDTO     桌面信息
     * @param targetDesktopPoolDTO 目标桌面池
     * @throws BusinessException ex
     */
    void moveDesktop(CloudDesktopDetailDTO desktopDetailDTO, CbbDesktopPoolDTO targetDesktopPoolDTO) throws BusinessException;

    /**
     * 检查用户在桌面池中是否有绑定的非回收站的桌面
     *
     * @param desktopPoolId desktopPoolId
     * @param userId        userId
     * @return true存在；false不存在
     */
    boolean checkUserBindDesktopInPool(UUID desktopPoolId, UUID userId);



    /**
     * 统计桌面池的PC终端已存在数量
     * @param computerIdArr PC终端Id数组
     * @param desktopPoolId desktopPoolId
     * @return 数量
     */
    int countAssignedComputerNumInGroupByDesktopPoolId(UUID[] computerIdArr, UUID desktopPoolId);

    /**
     * 根据组id获取列表
     * @param groupIdArr 组id数组
     * @return list
     */
    List<DesktopPoolComputerDTO> getDesktopPoolRelationComputerGroupList(List<UUID> groupIdArr);

    /**
     * 根据终端id获取列表
     * @param computerIdArr  computerIdArr
     * @return list
     */
    List<DesktopPoolComputerDTO> getDesktopPoolRelationComputerList(UUID[] computerIdArr);
}
