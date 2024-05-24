package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDriverInstallMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageEditType;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.RemoteTerminalEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.ImageTemplateBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.util.StringUtils;


/**
 *
 * Description:远程编辑镜像日志枚举
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月10日
 *
 * @author ypp
 */
public enum RemoteTerminalEditImageLog {

    IDV_EDIT_LOG(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_TASK_NAME,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_DESC,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_TASK_SUCCESS,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_TASK_FAIL,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IDV_IMAGE_FAIL_LOG),
    
    VOI_EDIT_LOG(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_TASK_NAME,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_DESC,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_TASK_SUCCESS,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_TASK_FAIL,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_TCI_IMAGE_FAIL_LOG),
    
    RE_EDIT_LOG(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_NAME,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_DESC,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_SUCCESS,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_FAIL,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_FAIL_LOG),

    FINISH_EDIT_LOG(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_TASK_NAME,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_DESC,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_TASK_SUCCESS,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_TASK_FAIL,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_FINISH_EDIT_IMAGE_FAIL_LOG),

    UPLOAD_LOG(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_TASK_NAME,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_DESC,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_TASK_SUCCESS,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_TASK_FAIL,
            ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_UPLOAD_FAIL_LOG);

    RemoteTerminalEditImageLog(String taskName, String taskDesc, String itemTaskSucLog, String itemTaskFailLog, String itemTaskFailDesc) {

        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.itemTaskSucLog = itemTaskSucLog;
        this.itemTaskFailLog = itemTaskFailLog;
        this.itemTaskFailDesc = itemTaskFailDesc;
    }

    private String taskName;

    private String taskDesc;

    private String itemTaskSucLog;

    private String itemTaskFailLog;
    
    private String itemTaskFailDesc;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getItemTaskSucLog() {
        return itemTaskSucLog;
    }

    public void setItemTaskSucLog(String itemTaskSucLog) {
        this.itemTaskSucLog = itemTaskSucLog;
    }

    public String getItemTaskFailLog() {
        return itemTaskFailLog;
    }

    public void setItemTaskFailLog(String itemTaskFailLog) {
        this.itemTaskFailLog = itemTaskFailLog;
    }


    public String getItemTaskFailDesc() {
        return itemTaskFailDesc;
    }

    public void setItemTaskFailDesc(String itemTaskFailDesc) {
        this.itemTaskFailDesc = itemTaskFailDesc;
    }
    
    

    /**
     * 根据镜像类型获取日志businessKey
     * 
     * @param cbbImageType 镜像类型
     * @param editType 操作类型
     * @return RemoteTerminalEditImageLog
     * @throws BusinessException 异常
     */
    public static RemoteTerminalEditImageLog getKeyByCbbImageType(CbbImageType cbbImageType, ImageEditType editType) throws BusinessException {
        Assert.notNull(cbbImageType, "cbbImageType can not be null");
        Assert.notNull(editType, "editType can not be null");
        switch (editType) {
            case DRIVER_INSTALL:
            case REMOTE_EDIT:
                switch (cbbImageType) {
                    case IDV: {
                        return IDV_EDIT_LOG;
                    }
                    case VOI: {
                        return VOI_EDIT_LOG;
                    }
                    default:
                        throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCC_REMOTE_TERMINAL_EDIT_IMAGE_LOG_NO_FIND);
                }
            case REMOTE_REEDIT:
                return RE_EDIT_LOG;
            case FINISH_EDIT:
                return FINISH_EDIT_LOG;
            case RE_UPLOAD:
                return UPLOAD_LOG;
            default:
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCC_REMOTE_TERMINAL_EDIT_IMAGE_OPERATE_TYPE_NO_FIND);
        }
    }

    /**
     * 根据操作类型获取日志参数
     * 
     * @param request 请求
     * @return String[]
     */
    public static String[] getEditImageLogMsgArgsByEditType(RemoteTerminalEditImageTemplateDTO request) {
        Assert.notNull(request, "request can not be null");
        switch (request.getImageEditType()) {
            case REMOTE_EDIT:
                if (request.getCbbImageType() == CbbImageType.IDV) {
                    return new String[] {getOperateKeyByCbbDriverInstallMode(request.getMode()), request.getTerminalMac(),
                        request.getImageTemplateName()};
                }
                return new String[] {getOperateKeyByCbbDriverInstallMode(request.getMode()), request.getTerminalMac(), request.getImageTemplateName(),
                    request.getImageTemplateName()};
            case REMOTE_REEDIT:
            case FINISH_EDIT:
                return new String[] {getOperateKeyByCbbDriverInstallMode(request.getMode()), request.getTerminalMac(), request.getImageTemplateName(),
                    request.getImageTemplateName()};
            case RE_UPLOAD:
                return new String[] {getOperateKeyByCbbDriverInstallMode(request.getMode()), request.getTerminalMac(),
                    request.getImageTemplateName()};
            default:
                return new String[] {};
        }
    }

    /**
     * 获取操作描述
     *
     * @param mode 驱动安装类型
     * @return String
     */
    public static String getOperateKeyByCbbDriverInstallMode(CbbDriverInstallMode mode) {
        Assert.notNull(mode, "mode can not be null");
        switch (mode) {
            case NO_INSTALL:
                return LocaleI18nResolver.resolve(ImageTemplateBusinessKey.RCDC_RCC_REMOTE_TERMINAL_EDIT_IMAGE_OPERATE);
            case AUTO:
            case MANUAL:
                return LocaleI18nResolver.resolve(ImageTemplateBusinessKey.RCDC_RCC_REMOTE_TERMINAL_IMAGE_INSTALL_DRIVER_OPERATE);
            default:
                return StringUtils.EMPTY;
        }
    }

}
