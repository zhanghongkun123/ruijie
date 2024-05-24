package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQueryUserListPageDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SaveUserMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CreateUserMessageWebRequest;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Description: 用户组发送消息批任务处理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-04-23 14:51
 *
 * @author wanglianyun
 */


public class UserGroupSendMsgBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupSendMsgBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserMessageAPI userMessageAPI;

    private UserMessageDTO userMessageDTO;
    
    private IacUserMgmtAPI iacUserMgmtAPI;

    private IacUserGroupMgmtAPI iacUserGroupMgmtAPI;

    public void setUserMessageDTO(UserMessageDTO userMessageDTO) {
        this.userMessageDTO = userMessageDTO;
    }

    public void setIacUserGroupMgmtAPI(IacUserGroupMgmtAPI iacUserGroupMgmtAPI) {
        this.iacUserGroupMgmtAPI = iacUserGroupMgmtAPI;
    }

    public UserGroupSendMsgBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI, UserMessageAPI userMessageAPI,
                                            IacUserMgmtAPI iacUserMgmtAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.userMessageAPI = userMessageAPI;
        this.iacUserMgmtAPI = iacUserMgmtAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        UserGroupBatchTaskItem userGroupBatchTaskItem = (UserGroupBatchTaskItem) taskItem;
        final String messageTitle = userMessageDTO.getTitle();
        UUID groupId = userGroupBatchTaskItem.getGroupId();
        // 查组名
        IacUserGroupDetailDTO userGroupDetail = iacUserGroupMgmtAPI.getUserGroupDetail(groupId);
        try {
            // 查组下的用户
            List<IacUserDetailDTO> userList = getUserListByGroupId(groupId);
            // 记录成功数量
            AtomicInteger successNum = new AtomicInteger();
            // 创建用户消息
            saveAndSendUserMessage(userList, successNum);
            // 返回失败多少个，成功多少个
            return DefaultBatchTaskItemResult.builder()
                    .batchTaskItemStatus(successNum.get() == userList.size() ? BatchTaskItemStatus.SUCCESS : BatchTaskItemStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_CREATE_USER_GROUP_MESSAGE_RESULT)//
                    .msgArgs(userGroupDetail.getName(), messageTitle, String.valueOf(successNum.get()),
                            String.valueOf(userList.size() - successNum.get()))//
                    .build();
        } catch (BusinessException ex) {
            LOGGER.error("用户组发送用户消息失败", ex);
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(ex);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_CREATE_USER_MESSAGE_FAIL,
                    ex, messageTitle, exceptionMsg);
        }
    }

    private void saveAndSendUserMessage(List<IacUserDetailDTO> userList, AtomicInteger successNum) {
        for (IacUserDetailDTO iacUserDetailDTO : userList) {
            try {
                SaveUserMessageRequest saveUserMessageRequest = buildSaveUserMessageRequest(iacUserDetailDTO);
                userMessageAPI.saveAndSendUserMessage(saveUserMessageRequest);
                successNum.incrementAndGet();
            } catch (BusinessException e) {
                LOGGER.error("保存用户消息失败", e);
                // 记录审计日志
                String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_CREATE_USER_MESSAGE_FAIL, userMessageDTO.getTitle(), exceptionMsg);
            }
        }
    }
    
    private List<IacUserDetailDTO> getUserListByGroupId(UUID groupId) throws BusinessException {
        List<IacUserDetailDTO> iacUserDetailDTOList = new ArrayList<>();
        IacPageResponse<IacUserDetailDTO> pageResult = pageQueryByGroupId(groupId, 0);
        LOGGER.info("用户组{}下的用户总数量为：{}", groupId, pageResult.getTotal());
        // 总页数
        int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
        for (int page = 0; page < totalPage; page++) {
            pageResult = pageQueryByGroupId(groupId, page);
            IacUserDetailDTO[] itemArr = pageResult.getItemArr();
            List<IacUserDetailDTO> detailDTOList = Arrays.stream(itemArr).collect(Collectors.toList());
            iacUserDetailDTOList.addAll(detailDTOList);
        }

        return iacUserDetailDTOList;
    }
    
    private IacPageResponse<IacUserDetailDTO> pageQueryByGroupId(UUID groupId, int page) throws BusinessException {
        IacQueryUserListPageDTO pageDTO = new IacQueryUserListPageDTO();
        pageDTO.setGroupId(groupId);
        pageDTO.setPage(page);
        pageDTO.setLimit(Constants.MAX_QUERY_LIST_SIZE);
        return iacUserMgmtAPI.pageQueryUserListByGroupId(pageDTO);
    }

    private SaveUserMessageRequest buildSaveUserMessageRequest(IacUserDetailDTO userDetailDTO) {
        SaveUserMessageRequest saveUserMessageRequest = new SaveUserMessageRequest();
        saveUserMessageRequest.setMessageId(userMessageDTO.getId());
        saveUserMessageRequest.setUserId(userDetailDTO.getId());
        saveUserMessageRequest.setIacUserTypeEnum(userDetailDTO.getUserType());
        saveUserMessageRequest.setContent(userMessageDTO.getContent());
        saveUserMessageRequest.setTitle(userMessageDTO.getTitle());
        saveUserMessageRequest.setCreateTime(userMessageDTO.getCreateTime());
        return saveUserMessageRequest;
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_USER_GROUP_SEND_MESSAGE_RESULT);
    }
}
