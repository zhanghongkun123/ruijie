package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskGetGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskSearchGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.GetGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UserGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupDesktopRelatedEntity;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/09 16:51
 *
 * @author coderLee23
 */
public interface ViewUserGroupDesktopRelatedService {

    /**
     *
     * 根据用户组id查询
     *
     * @param searchGroupDesktopRelatedDTO 查询对象
     * @param pageable 分页参数
     * @return Page<ViewUserGroupDesktopRelatedEntity>
     */
    Page<ViewUserGroupDesktopRelatedEntity> pageUserGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO, Pageable pageable);


    /**
     *
     * 根据用户组id列表获取所有可用云桌面
     *
     * @param searchGroupDesktopRelatedDTO 查询对象
     * @return List<ViewUserGroupDesktopRelatedEntity>
     */
    List<ViewUserGroupDesktopRelatedEntity> listUserGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO);


    /**
     *
     * 获取所有的用户组的可选桌面数量
     *
     * @param getGroupDesktopCountDTO 参数
     * @return List<UserGroupDesktopCountDTO>
     */
    List<UserGroupDesktopCountDTO> listUserGroupDesktopCount(GetGroupDesktopCountDTO getGroupDesktopCountDTO);


    /**
     *
     * 文件分发 -- 根据终端组id查询
     *
     * @param taskSearchGroupDesktopRelatedDTO 查询对象
     * @param pageable 分页参数
     * @return Page<ViewUserGroupDesktopRelatedEntity>
     */
    Page<ViewUserGroupDesktopRelatedEntity> pageUserGroupDesktopRelated(TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO,
            Pageable pageable);


    /**
     *
     * 文件分发 -- 获取所有的用户组的可选桌面数量
     *
     * @param taskGetGroupDesktopCountDTO 参数
     * @return List<UserGroupDesktopCountDTO>
     */
    List<UserGroupDesktopCountDTO> listUserGroupDesktopCount(TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO);

    /**
     *
     * 文件分发 -- 根据用户组id列表获取所有可用云桌面
     *
     * @param taskSearchGroupDesktopRelatedDTO 参数
     * @return List<UserGroupDesktopRelatedDTO>
     */
    List<ViewUserGroupDesktopRelatedEntity> listUserGroupDesktopRelated(TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO);

}
