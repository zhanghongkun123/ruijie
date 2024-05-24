package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskGetGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskSearchGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.TerminalGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.TerminalGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UserGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UserGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.distribution.dto.DistributeTaskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.distribution.request.SearchDistributeTaskDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 文件分发任务管理API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 14:33
 *
 * @author zhangyichi
 */
public interface FileDistributionTaskManageAPI {

    /**
     * 创建文件分发任务
     * 
     * @param request 创建请求
     * @return 新任务ID
     * @throws BusinessException 业务异常
     */
    UUID createTask(CreateDistributeTaskRequest request) throws BusinessException;

    /**
     * 根据任务名称（父任务）查找任务
     * 
     * @param taskName 任务名称
     * @return 任务列表
     */
    List<DistributeTaskDTO> findByTaskName(String taskName);

    /**
     * 获取分发任务（父任务）基本信息
     * 
     * @param request 父任务ID
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    DistributeTaskParameterDTO getBasicInfo(IdRequest request) throws BusinessException;

    /**
     * 查找参数包含指定内容的分发任务（父任务）列表
     * 
     * @param content 指定内容
     * @return 父任务列表
     */
    List<DistributeTaskDTO> findTaskByParameterContent(String content);

    /**
     * 分页查询文件分发任务（子任务）
     * 
     * @param request 分页请求
     * @param targetType 分发对象类型
     * @return 任务列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<DistributeSubTaskDTO> pageQuerySubTask(PageQueryRequest request, FileDistributionTargetType targetType)
            throws BusinessException;

    /**
     * 获取文件分发任务（子任务）信息
     * 
     * @param request 子任务ID
     * @return 子任务信息
     * @throws BusinessException 业务异常
     */
    DistributeSubTaskDTO getSubTaskInfo(IdRequest request) throws BusinessException;

    /**
     * 统计父任务下指定状态的子任务数
     * 
     * @param taskId 父任务ID
     * @param statusList 任务状态
     * @return 子任务数
     */
    Long countSubTaskWithState(UUID taskId, List<FileDistributionTaskStatus> statusList);

    /**
     * 执行文件分发任务（父任务）
     * 
     * @param request 父任务ID
     * @throws BusinessException 业务异常
     */
    void runTask(IdRequest request) throws BusinessException;

    /**
     * 取消文件分发任务（父任务）
     * 
     * @param request 父任务ID
     * @throws BusinessException 业务异常
     */
    void cancelTask(IdRequest request) throws BusinessException;

    /**
     * 处理子任务执行结果
     * 
     * @param request 执行结果
     */
    void processSubTaskResult(DistributeTaskResultDTO request);

    /**
     * 删除文件分发任务
     * 
     * @param request 父任务ID
     * @throws BusinessException 业务异常
     */
    void deleteTask(IdRequest request) throws BusinessException;

    /**
     * 取消文件分发任务（子任务）
     * 
     * @param request 子任务ID
     * @throws BusinessException 业务异常
     */
    void cancelSubTask(IdRequest request) throws BusinessException;

    /**
     * 重试文件分发任务（子任务）
     * 
     * @param request 子任务ID
     * @throws BusinessException 业务异常
     */
    void retrySubTask(IdRequest request) throws BusinessException;

    /**
     * 重建文件分发任务（子任务）
     * 
     * @param subTaskDTO 子任务信息
     */
    void rebuildSubTask(DistributeSubTaskDTO subTaskDTO);


    /**
     *
     * 根据终端组id查询
     *
     * @param taskSearchGroupDesktopRelatedDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<TerminalGroupDesktopRelatedDTO>
     */
    DefaultPageResponse<TerminalGroupDesktopRelatedDTO> pageTerminalGroupDesktopRelated(
            TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO, Pageable pageable);


    /**
     *
     * 获取所有的终端组的可选桌面数量
     *
     * @param taskGetGroupDesktopCountDTO 参数
     * @return List<TerminalGroupDesktopCountDTO>
     */
    List<TerminalGroupDesktopCountDTO> listTerminalGroupDesktopCount(TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO);

    /**
     *
     * 根据终端组id列表获取所有可用云桌面
     *
     * @param taskSearchGroupDesktopRelatedDTO 查询参数
     * @return List<TerminalGroupDesktopRelatedDTO>
     */
    List<TerminalGroupDesktopRelatedDTO> listTerminalGroupDesktopRelated(TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO);


    /**
     *
     * 根据终端组id查询
     *
     * @param taskSearchGroupDesktopRelatedDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<UserGroupDesktopRelatedDTO>
     */
    DefaultPageResponse<UserGroupDesktopRelatedDTO> pageUserGroupDesktopRelated(TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO,
            Pageable pageable);


    /**
     *
     * 获取所有的用户组的可选桌面数量
     *
     * @param taskGetGroupDesktopCountDTO 参数
     * @return List<UserGroupDesktopCountDTO>
     */
    List<UserGroupDesktopCountDTO> listUserGroupDesktopCount(TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO);

    /**
     *
     * 根据用户组id列表获取所有可用云桌面
     *
     * @param taskSearchGroupDesktopRelatedDTO 参数
     * @return List<UserGroupDesktopRelatedDTO>
     */
    List<UserGroupDesktopRelatedDTO> listUserGroupDesktopRelated(TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO);



    /**
     * 分页查找
     *
     * @param searchDistributeTaskDTO 查询参数
     * @param pageable 分页参数
     * @return 返回值
     */
    DefaultPageResponse<DistributeTaskDetailDTO> pageDistributeTask(SearchDistributeTaskDTO searchDistributeTaskDTO, Pageable pageable);

}
