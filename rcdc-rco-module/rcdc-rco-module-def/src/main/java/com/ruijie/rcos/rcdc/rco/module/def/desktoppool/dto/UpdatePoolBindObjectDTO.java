package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.google.common.collect.Lists;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 更新桌面池用户或用户组关联关系
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 14:39
 *
 * @author linke
 */
public class UpdatePoolBindObjectDTO {

    @NotNull
    private UUID poolId;

    @Nullable
    private List<UUID> addUserByIdList;

    @Nullable
    private List<UUID> deleteUserByIdList;

    @Nullable
    private List<UUID> deleteUserByGroupIdList;

    @Nullable
    private List<UUID> selectedGroupIdList;

    @Nullable
    private List<GroupExceptUserDTO> exceptList;

    @Nullable
    private List<UUID> selectedAdGroupIdList;

    /**
     * 获取涉及到的所有组ID列表
     *
     * @return List<UUID>
     */
    public List<UUID> getAllInvolvedGroupIdList() {
        List<UUID> groupIdList = this.getSelectedGroupIdAndDeleteUserByGroupId();
        groupIdList.addAll(this.getAllExceptGroupIdList());
        return groupIdList;
    }

    /**
     * 获取涉及到的所有用户ID列表
     *
     * @return List<UUID>
     */
    public List<UUID> getAllInvolvedUserIdList() {
        List<UUID> userIdList = new ArrayList<>();
        userIdList.addAll(Optional.ofNullable(addUserByIdList).orElse(Collections.emptyList()));
        userIdList.addAll(Optional.ofNullable(deleteUserByIdList).orElse(Collections.emptyList()));
        return userIdList;
    }

    /**
     * 汇总要删除的用户ID到deleteUserByIdList
     *
     * @param needDeleteByGroupUserIdList needDeleteByGroupUserIdList
     */
    public void mergeDeleteUserIdList(List<UUID> needDeleteByGroupUserIdList) {
        Assert.notNull(needDeleteByGroupUserIdList, "needDeleteByGroupUserIdList is null");

        List<UUID> exceptUserIdList = this.getAllExceptUserIdList();
        // 汇总要删除的用户到deleteUserByIdList字段中
        List<UUID> finalDeleteUserIdList = Optional.ofNullable(deleteUserByIdList).orElse(new ArrayList<>());
        finalDeleteUserIdList.addAll(needDeleteByGroupUserIdList);
        finalDeleteUserIdList.addAll(exceptUserIdList);
        // 去重
        deleteUserByIdList = finalDeleteUserIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获取选择的组列表和deleteUserByGroupIdList所有用户组列表的合并值
     *
     * @return List<UUID>
     */
    public List<UUID> getSelectedGroupIdAndDeleteUserByGroupId() {
        List<UUID> groupIdList = Lists.newArrayList();
        groupIdList.addAll(Optional.ofNullable(deleteUserByGroupIdList).orElse(Lists.newArrayList()));
        groupIdList.addAll(Optional.ofNullable(selectedGroupIdList).orElse(Lists.newArrayList()));
        return groupIdList;
    }

    /**
     * 合并需要添加的userId: addUserByIdList和exceptList反向的用户ID到addUserByIdList
     *
     * @param userIdOfExceptGroupList userIdOfExceptGroupList
     */
    public void mergeAllAddUserIdList(List<UUID> userIdOfExceptGroupList) {
        Assert.notNull(userIdOfExceptGroupList, "userIdOfExceptGroupList is null");

        // 整合需要新增的用户，处理exceptList，取出组下所有用户ID，再根据exceptUserIdList删除对应用户，再全部加入到addUserByIdList中
        if (CollectionUtils.isEmpty(exceptList)) {
            if (CollectionUtils.isNotEmpty(addUserByIdList)) {
                // 去重
                addUserByIdList = addUserByIdList.stream().distinct().collect(Collectors.toList());
            }
            return;
        }

        Set<UUID> exceptUserIdSet = new HashSet<>(this.getAllExceptUserIdList());
        // 获取组下所有用户ID，再根据exceptUserIdSet过滤掉不要的
        List<UUID> addByExceptIdList = userIdOfExceptGroupList.stream().filter(item -> !exceptUserIdSet.contains(item)).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(addUserByIdList)) {
            addUserByIdList = addByExceptIdList;
            return;
        }
        addUserByIdList.addAll(addByExceptIdList);
        addUserByIdList = addUserByIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 汇总exceptList里涉及的用户ID
     *
     * @return List<UUID>
     */
    public List<UUID> getAllExceptUserIdList() {
        if (CollectionUtils.isEmpty(exceptList)) {
            return Collections.emptyList();
        }
        List<UUID> exceptUserIdList = Lists.newArrayList();
        for (GroupExceptUserDTO groupExceptUserDTO : exceptList) {
            if (CollectionUtils.isNotEmpty(groupExceptUserDTO.getExceptUserIdList())) {
                exceptUserIdList.addAll(groupExceptUserDTO.getExceptUserIdList());
            }
        }
        return exceptUserIdList;
    }

    /**
     * 汇总exceptList里涉及的组ID
     *
     * @return List<UUID>
     */
    public List<UUID> getAllExceptGroupIdList() {
        if (CollectionUtils.isEmpty(exceptList)) {
            return Collections.emptyList();
        }
        List<UUID> exceptGroupIdList = Lists.newArrayList();
        for (GroupExceptUserDTO groupExceptUserDTO : exceptList) {
            if (Objects.nonNull(groupExceptUserDTO.getGroupId())) {
                exceptGroupIdList.add(groupExceptUserDTO.getGroupId());
            }
        }
        return exceptGroupIdList;
    }

    /**
     * 刷新数据
     */
    public void refresh() {
        if (CollectionUtils.isEmpty(addUserByIdList) || CollectionUtils.isEmpty(deleteUserByIdList)) {
            return;
        }
        Collection<UUID> intersection = CollectionUtils.intersection(addUserByIdList, deleteUserByIdList);
        if (CollectionUtils.isNotEmpty(intersection)) {
            addUserByIdList.removeAll(intersection);
            deleteUserByIdList.removeAll(intersection);
        }
    }

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    @Nullable
    public List<UUID> getAddUserByIdList() {
        return addUserByIdList;
    }

    public void setAddUserByIdList(@Nullable List<UUID> addUserByIdList) {
        this.addUserByIdList = addUserByIdList;
    }

    @Nullable
    public List<UUID> getDeleteUserByIdList() {
        return deleteUserByIdList;
    }

    public void setDeleteUserByIdList(@Nullable List<UUID> deleteUserByIdList) {
        this.deleteUserByIdList = deleteUserByIdList;
    }

    @Nullable
    public List<UUID> getDeleteUserByGroupIdList() {
        return deleteUserByGroupIdList;
    }

    public void setDeleteUserByGroupIdList(@Nullable List<UUID> deleteUserByGroupIdList) {
        this.deleteUserByGroupIdList = deleteUserByGroupIdList;
    }

    @Nullable
    public List<UUID> getSelectedGroupIdList() {
        return selectedGroupIdList;
    }

    public void setSelectedGroupIdList(@Nullable List<UUID> selectedGroupIdList) {
        this.selectedGroupIdList = selectedGroupIdList;
    }

    @Nullable
    public List<GroupExceptUserDTO> getExceptList() {
        return exceptList;
    }

    public void setExceptList(@Nullable List<GroupExceptUserDTO> exceptList) {
        this.exceptList = exceptList;
    }

    @Nullable
    public List<UUID> getSelectedAdGroupIdList() {
        return selectedAdGroupIdList;
    }

    public void setSelectedAdGroupIdList(@Nullable List<UUID> selectedAdGroupIdList) {
        this.selectedAdGroupIdList = selectedAdGroupIdList;
    }
}
