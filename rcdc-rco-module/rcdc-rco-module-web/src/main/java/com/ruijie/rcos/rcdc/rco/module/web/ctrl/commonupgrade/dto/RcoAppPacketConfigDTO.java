package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.dto;

import com.google.common.collect.Lists;
import com.ruijie.rcos.base.upgrade.module.def.dto.UpgradeTarget;
import com.ruijie.rcos.base.upgrade.module.def.enums.UpgradeMode;
import com.ruijie.rcos.base.upgrade.module.def.enums.UpgradeRange;
import com.ruijie.rcos.rcdc.rco.module.common.enums.CommonUpgradeTargetType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: ConfigPacketRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21
 *
 * @author chenl
 */
public class RcoAppPacketConfigDTO extends DefaultWebRequest {

    @NotNull
    private UUID id;

    @NotNull
    private UpgradeMode upgradeMode;

    @NotNull
    private UpgradeRange upgradeRange;

    @Nullable
    private GenericIdLabelEntry<UUID>[] deskArr;

    @Nullable
    private GenericIdLabelEntry<UUID>[] deskPoolArr;

    @Nullable
    private GenericIdLabelEntry<UUID>[] imageArr;

    @Nullable
    private GenericIdLabelEntry<String>[] terminalArr;

    @Nullable
    private GenericIdLabelEntry<UUID>[] terminalGroupArr;
    
    @Nullable
    private GenericIdLabelEntry<UUID>[] rcaPoolArr;

    @Nullable
    private Date upgradeBeginTime;

    @Nullable
    private Date upgradeEndTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UpgradeMode getUpgradeMode() {
        return upgradeMode;
    }

    public void setUpgradeMode(UpgradeMode upgradeMode) {
        this.upgradeMode = upgradeMode;
    }

    public UpgradeRange getUpgradeRange() {
        return upgradeRange;
    }

    public void setUpgradeRange(UpgradeRange upgradeRange) {
        this.upgradeRange = upgradeRange;
    }

    @Nullable
    public GenericIdLabelEntry<UUID>[] getDeskArr() {
        return deskArr;
    }

    public void setDeskArr(@Nullable GenericIdLabelEntry<UUID>[] deskArr) {
        this.deskArr = deskArr;
    }

    @Nullable
    public GenericIdLabelEntry<UUID>[] getDeskPoolArr() {
        return deskPoolArr;
    }

    public void setDeskPoolArr(@Nullable GenericIdLabelEntry<UUID>[] deskPoolArr) {
        this.deskPoolArr = deskPoolArr;
    }

    @Nullable
    public GenericIdLabelEntry<UUID>[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(@Nullable GenericIdLabelEntry<UUID>[] imageArr) {
        this.imageArr = imageArr;
    }

    @Nullable
    public GenericIdLabelEntry<String>[] getTerminalArr() {
        return terminalArr;
    }

    public void setTerminalArr(@Nullable GenericIdLabelEntry<String>[] terminalArr) {
        this.terminalArr = terminalArr;
    }

    @Nullable
    public GenericIdLabelEntry<UUID>[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(@Nullable GenericIdLabelEntry<UUID>[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    @Nullable
    public GenericIdLabelEntry<UUID>[] getRcaPoolArr() {
        return rcaPoolArr;
    }

    public void setRcaPoolArr(@Nullable GenericIdLabelEntry<UUID>[] rcaPoolArr) {
        this.rcaPoolArr = rcaPoolArr;
    }

    /**
     *
     * @return 返回数组
     */
    public UpgradeTarget[] toUpgradeTargets() {
        List<UpgradeTarget> upgradeTargetList = Lists.newArrayList();
        if (Objects.nonNull(getDeskArr())) {
            appendUpgradeTargets(upgradeTargetList, getDeskArr(), CommonUpgradeTargetType.DESKTOP.name());
        }
        if (Objects.nonNull(getDeskPoolArr())) {
            appendUpgradeTargets(upgradeTargetList, getDeskPoolArr(), CommonUpgradeTargetType.DESKTOP_POOL.name());
        }
        if (Objects.nonNull(getImageArr())) {
            appendUpgradeTargets(upgradeTargetList, getImageArr(), CommonUpgradeTargetType.IMAGE.name());
        }
        if (Objects.nonNull(getTerminalArr())) {
            appendUpgradeTargets(upgradeTargetList, getTerminalArr(), CommonUpgradeTargetType.TERMINAL.name());
        }

        if (Objects.nonNull(getTerminalGroupArr())) {
            appendUpgradeTargets(upgradeTargetList, getTerminalGroupArr(), CommonUpgradeTargetType.TERMINAL_GROUP.name());
        }

        if (Objects.nonNull(getRcaPoolArr())) {
            appendUpgradeTargets(upgradeTargetList, getRcaPoolArr(), CommonUpgradeTargetType.RCA_POOL.name());
        }

        return upgradeTargetList.toArray(new UpgradeTarget[0]);
    }

    @Nullable
    public Date getUpgradeBeginTime() {
        return upgradeBeginTime;
    }

    public void setUpgradeBeginTime(@Nullable Date upgradeBeginTime) {
        this.upgradeBeginTime = upgradeBeginTime;
    }

    @Nullable
    public Date getUpgradeEndTime() {
        return upgradeEndTime;
    }

    public void setUpgradeEndTime(@Nullable Date upgradeEndTime) {
        this.upgradeEndTime = upgradeEndTime;
    }

    private void appendUpgradeTargets(List<UpgradeTarget> upgradeTargetList, GenericIdLabelEntry<?>[] idArr, String type) {
        for (GenericIdLabelEntry<?> idLabelEntry : idArr) {
            UpgradeTarget upgradeTarget = new UpgradeTarget();
            upgradeTarget.setId(idLabelEntry.getId().toString());
            upgradeTarget.setType(type);
            upgradeTargetList.add(upgradeTarget);
        }
    }
}
