package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.enums.SessionStatueEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.DesktopSessionBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: 桌面会话excel表头DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月20日
 *
 * @author wangjie9
 */
public class ExportDesktopSessionExcelDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportDesktopSessionExcelDTO.class);

    /**
     * 桌面名称
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_DESKTOP_NAME)
    private String desktopName;

    /**
     * 会话状态
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_SESSIONSTATUS)
    private String sessionStatus;

    /**
     * 所属桌面池名称
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_DESKTOPPOOLNAME)
    private String desktopPoolName;

    /**
     * 用户名
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_USERNAME)
    private String userName;

    /**
     * 姓名
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_REALNAME)
    private String realName;

    /**
     * 所属用户组名
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_GROUPNAME)
    private String groupName;

    /**
     * 会话建立时间
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_CREATETIME)
    private String lastCreateTime;

    /**
     * 会话建立时长
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_LASTCREATETIMESECOND)
    private String lastCreateTimeSecond;

    /**
     * 会话空闲时长
     */
    @ExcelHead(DesktopSessionBusinessKey.RCDC_RCO_EXPORT_LASTIDLETIMESECOND)
    private String lastIdleTimeSecond;

    public ExportDesktopSessionExcelDTO() {
    }

    public ExportDesktopSessionExcelDTO(ViewDesktopSessionEntity viewDesktopSessionDTO) {
        Assert.notNull(viewDesktopSessionDTO, "viewDesktopSessionDTO is not null");
        this.desktopName = viewDesktopSessionDTO.getDesktopName();
        this.desktopPoolName = viewDesktopSessionDTO.getDesktopPoolName();
        this.userName = viewDesktopSessionDTO.getUserName();
        this.realName = viewDesktopSessionDTO.getRealName();
        this.groupName = viewDesktopSessionDTO.getUserGroupName();
        this.lastCreateTime = Optional.ofNullable(viewDesktopSessionDTO.getLastCreateTime())
                .map(DateUtil::getDateMinuteFormat).orElse(StringUtils.EMPTY);
        this.lastCreateTimeSecond = Optional.ofNullable(viewDesktopSessionDTO.getLastCreateTimeSecond())
                .map(value -> DateUtil.millisecondsConvertToHMS(value * DateUtil.SECOND_TO_MILLISECOND, false)).orElse(DateUtil.ZERO_SECOND);
        this.lastIdleTimeSecond = Optional.ofNullable(viewDesktopSessionDTO.getLastIdleTimeSecond())
                .map(value -> DateUtil.millisecondsConvertToHMS(value * DateUtil.SECOND_TO_MILLISECOND, false)).orElse(DateUtil.ZERO_SECOND);
        this.sessionStatus = convert(viewDesktopSessionDTO.getSessionStatus());

        try {
            // 给属性为null的赋值为空字符串
            format();
        } catch (IllegalAccessException e) {
            LOGGER.error("格式化ExportDesktopSessionExcelDTO类失败，失败原因:", e);
        }
    }

    private String convert(SessionStatueEnum statueEnum) {
        switch (statueEnum) {
            case ONLINE:
                return Constants.ONLINE;
            case DESTROYING:
                return Constants.DESTROYING;
            default:
                return "";
        }
    }

    private void format() throws IllegalAccessException {
        Class<? extends ExportDesktopSessionExcelDTO> exportApplyClazz = this.getClass();
        Field[] fieldArr = exportApplyClazz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, "");
            }
        }
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(String lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    public String getLastCreateTimeSecond() {
        return lastCreateTimeSecond;
    }

    public void setLastCreateTimeSecond(String lastCreateTimeSecond) {
        this.lastCreateTimeSecond = lastCreateTimeSecond;
    }

    public String getLastIdleTimeSecond() {
        return lastIdleTimeSecond;
    }

    public void setLastIdleTimeSecond(String lastIdleTimeSecond) {
        this.lastIdleTimeSecond = lastIdleTimeSecond;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
}
