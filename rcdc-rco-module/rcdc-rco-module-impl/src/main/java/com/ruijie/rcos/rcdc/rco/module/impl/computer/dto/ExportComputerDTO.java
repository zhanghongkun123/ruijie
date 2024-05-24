package com.ruijie.rcos.rcdc.rco.module.impl.computer.dto;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.DateUtils;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Description: 导出实体类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/20
 *
 * @author zqj
 */
public class ExportComputerDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportComputerDTO.class);

    /**
     * 下载状态国际化的前缀，使用这个前缀加上下载状态匹配对应国际化状态
     */
    private static final String DOWNLOAD_STATE_PREFIX = "rcdc_rco_download_state_";

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_RECORD_COMPUTER_NAME)
    private String name;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_TERMINAL_GROUP_NAME)
    private String terminalGroupName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_COMPUTER_IP_ADDRESS)
    private String ip;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_MAC)
    private String mac;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_STATE)
    private String state;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_COMPUTER_TYPE)
    private String type;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_COMPUTER_OS)
    private String os;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_COMPUTER_AGENT_VERSION)
    private String agentVersion;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CPU)
    private String cpu;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_MEMORY)
    private String memory;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_SYSTEM_DISK)
    private String systemDisk;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_PERSONAL_DISK)
    private String personDisk;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_WORKMODEL)
    private String workModel;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CREATE_TIME)
    private String createTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_COMPUTER_REMARK)
    private String alias;


    public ExportComputerDTO() {

    }

    // 从ComputerDTO中获得数据生成实体
    public ExportComputerDTO(ComputerDTO computerDTO) {
        Assert.notNull(computerDTO, "ComputerDTO is not null");

        BeanUtils.copyProperties(computerDTO, this);
        this.createTime = DateUtils.format(computerDTO.getCreateTime(), DateUtils.NORMAL_DATE_FORMAT);
        this.cpu = formatObject(computerDTO.getCpu(), BusinessKey.RCDC_RCO_FORMAT_CPU);
        this.memory = formatObject(computerDTO.getMemory(), BusinessKey.RCDC_RCO_FORMAT_MEMORY);
        this.systemDisk = formatObject(computerDTO.getSystemDisk(), BusinessKey.RCDC_RCO_FORMAT_SYSTEMDISK);
        this.personDisk = formatObject(computerDTO.getPersonDisk(), BusinessKey.RCDC_RCO_FORMAT_PERSIONDISK);
        this.workModel = parseWorkModel(computerDTO.getWorkModel());
        this.type = parseType(computerDTO.getType());
        this.state = parseState(computerDTO.getState());


        try {
            format();
        } catch (IllegalAccessException e) {
            LOGGER.info("格式化ExportComputerDTO类失败", e);
        }
    }

    private String formatObject(Object obj, String key) {
        if (Objects.isNull(obj) || (obj instanceof String && StringUtils.isEmpty((String) obj))) {
            return "--";
        }

        return LocaleI18nResolver.resolve(key, String.valueOf(obj));
    }

    private void format() throws IllegalAccessException {
        Class<? extends ExportComputerDTO> clzz = this.getClass();
        Field[] fieldArr = clzz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, "");
            }
        }
    }


    private String parseState(ComputerStateEnum state) {
        if (state == null) {
            return "";
        }
        String businessKey;
        switch (state) {
            case ONLINE:
                businessKey = BusinessKey.RCDC_RCO_COMPUTER_STATE_ONLINE;
                break;
            case OFFLINE:
                businessKey = BusinessKey.RCDC_RCO_COMPUTER_STATE_OFFLINE;
                break;
            case WAIT_TUBE:
                businessKey = BusinessKey.RCDC_RCO_COMPUTER_STATE_WAIT_TUBE;
                break;
            default:
                LOGGER.error("can not find state in cbbCloudDeskState, now is {}", state);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }


    private String parseType(ComputerTypeEnum type) {
        if (type == null) {
            return "";
        }
        String businessKey;
        switch (type) {
            case PC:
                businessKey = BusinessKey.RCDC_RCO_COMPUTER_TYPE_PC;
                break;
            case THIRD:
                businessKey = BusinessKey.RCDC_RCO_COMPUTER_TYPE_THIRD_PARTY;
                break;
            default:
                LOGGER.error("can not find userTypeEnum in CbbUserTypeEnum, now is {}", type);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseWorkModel(ComputerWorkModelEnum workModel) {
        if (workModel == null) {
            return "";
        }
        String businessKey;
        switch (workModel) {
            case APP_HOST:
                businessKey = BusinessKey.RCDC_RCO_WORK_MODEL_APP_HOST;
                break;
            case CLOUD_DESK:
                businessKey = BusinessKey.RCDC_RCO_WORK_MODEL_CLOUD_DESK;
                break;
            default:
                LOGGER.error("can not find workModel in ComputerWorkModelEnum, now is {}", workModel);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getAgentVersion() {
        return agentVersion;
    }

    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }


    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(String systemDisk) {
        this.systemDisk = systemDisk;
    }

    public String getPersonDisk() {
        return personDisk;
    }

    public void setPersonDisk(String personDisk) {
        this.personDisk = personDisk;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWorkModel() {
        return workModel;
    }

    public void setWorkModel(String workModel) {
        this.workModel = workModel;
    }
}
