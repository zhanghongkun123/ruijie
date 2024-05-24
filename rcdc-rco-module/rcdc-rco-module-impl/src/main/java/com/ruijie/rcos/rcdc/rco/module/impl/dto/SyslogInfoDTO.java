package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.cloudbees.syslog.Severity;
import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDesktopOpLogDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.LogType;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Description: syslog发送的信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/30 15:50
 *
 * @author yxq
 */
public class SyslogInfoDTO {

    /**
     * 日志内容
     */
    @JSONField(name = "msg")
    private String msg;

    /**
     * 日志类别：系统日志、操作日志、云桌面日志
     */
    @JSONField(name = "category")
    private LogType category;

    /**
     * 日志等级
     */
    @JSONField(name = "log_severity")
    private Severity logSeverity;

    /**
     * 日志时间
     */
    @JSONField(name = "log_time")
    private String logTime;

    /**
     * 操作者
     */
    @JSONField(name = "operator")
    private String operator;

    /**
     * 操作类型
     */
    @JSONField(name = "operation")
    private String operation;

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static DateTimeFormatter DATE_FORMAT;

    {
        DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.US);
    }

    /**
     * 构建操作日志syslog信息
     *
     * @param itemArr 操作日志信息
     * @return List<SyslogInfoDTO>
     */
    public List<SyslogInfoDTO> buildSyslogInfoDTOList(BaseOperateLogDTO[] itemArr) {
        List<SyslogInfoDTO> syslogInfoDTOList = new LinkedList<>();
        for (BaseOperateLogDTO baseOperateLogDTO : itemArr) {
            SyslogInfoDTO syslogInfoDTO = new SyslogInfoDTO();

            syslogInfoDTO.setMsg(baseOperateLogDTO.getContent());
            syslogInfoDTO.setCategory(LogType.OPERATE_LOG);
            syslogInfoDTO.setLogSeverity(Severity.INFORMATIONAL);
            syslogInfoDTO.setOperation("admin_operation");
            syslogInfoDTO.setOperator(generateOperator(baseOperateLogDTO.getOperator(), baseOperateLogDTO.getLoginIp()));
            syslogInfoDTO.setLogTime(generateLogTime(baseOperateLogDTO.getOperationTime()));

            syslogInfoDTOList.add(syslogInfoDTO);
        }

        return syslogInfoDTOList;
    }

    /**
     * 构建系统日志syslog信息
     *
     * @param itemArr 系统日志信息
     * @return List<SyslogInfoDTO>
     */
    public List<SyslogInfoDTO> buildSyslogInfoDTOList(BaseSystemLogDTO[] itemArr) {
        List<SyslogInfoDTO> syslogInfoDTOList = new LinkedList<>();
        for (BaseSystemLogDTO baseSystemLogDTO : itemArr) {
            SyslogInfoDTO syslogInfoDTO = new SyslogInfoDTO();

            syslogInfoDTO.setMsg(baseSystemLogDTO.getContent());
            syslogInfoDTO.setCategory(LogType.SYSTEM_LOG);
            syslogInfoDTO.setLogSeverity(Severity.INFORMATIONAL);
            syslogInfoDTO.setOperation("system_operation");
            syslogInfoDTO.setOperator("system");
            syslogInfoDTO.setLogTime(generateLogTime(baseSystemLogDTO.getCreateTime()));

            syslogInfoDTOList.add(syslogInfoDTO);
        }

        return syslogInfoDTOList;
    }

    /**
     * 构建云桌面日志syslog信息
     *
     * @param itemArr 云桌面日志信息
     * @return List<SyslogInfoDTO>
     */
    public List<SyslogInfoDTO> buildSyslogInfoDTOList(CbbDesktopOpLogDetailDTO[] itemArr) {
        List<SyslogInfoDTO> syslogInfoDTOList = new LinkedList<>();
        for (CbbDesktopOpLogDetailDTO cbbDesktopOpLogDetailDTO : itemArr) {
            SyslogInfoDTO syslogInfoDTO = new SyslogInfoDTO();

            syslogInfoDTO.setMsg(cbbDesktopOpLogDetailDTO.getDetail());
            syslogInfoDTO.setCategory(LogType.CLOUD_DESKTOP_LOG);
            syslogInfoDTO.setLogSeverity(Severity.INFORMATIONAL);
            syslogInfoDTO.setOperation(cbbDesktopOpLogDetailDTO.getEventName());
            syslogInfoDTO.setOperator(generateOperator(cbbDesktopOpLogDetailDTO.getOperatorSource(), cbbDesktopOpLogDetailDTO.getSourceIp()));
            syslogInfoDTO.setLogTime(generateLogTime(new Date(cbbDesktopOpLogDetailDTO.getOperateTime())));

            syslogInfoDTOList.add(syslogInfoDTO);
        }

        return syslogInfoDTOList;
    }

    private String generateOperator(String operator, String loginIp) {
        return operator + " from IP " + loginIp;
    }

    private String generateLogTime(Date date) {
        return DATE_FORMAT.format(date.toInstant().atZone(ZoneId.of("GMT")).toLocalDateTime());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Severity getLogSeverity() {
        return logSeverity;
    }

    public void setLogSeverity(Severity logSeverity) {
        this.logSeverity = logSeverity;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public LogType getCategory() {
        return category;
    }

    public void setCategory(LogType category) {
        this.category = category;
    }
}
