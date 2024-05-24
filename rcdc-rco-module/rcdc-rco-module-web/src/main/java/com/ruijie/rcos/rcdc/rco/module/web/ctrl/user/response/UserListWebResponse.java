package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 用户列表返回的结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/22 14:12
 *
 * @author yxq
 */
public class UserListWebResponse {

    @ApiModelProperty(value = "用户列表")
    private UserListDTO[] itemArr;

    @ApiModelProperty(value = "总数")
    private Long total;

    @ApiModelProperty(value = "是否开启动态口令")
    private Boolean openOtp;

    @ApiModelProperty(value = "是否开启CAS扫码认证")
    private Boolean openCas;

    @ApiModelProperty(value = "是否开启硬件特征码")
    private Boolean openHardware;
    
    @ApiModelProperty(value = "是否开启短信认证")
    private Boolean openSmsCertification;

    @ApiModelProperty(value = "是否开启Radius服务")
    private Boolean openRadiusCertification;

    @ApiModelProperty(value = "是否开启第三方认证")
    private Boolean openThirdPartyCertification;

    @ApiModelProperty(value = "开启企业微信")
    private Boolean openWorkWeixinCertification;

    @ApiModelProperty(value = "开启飞书")
    private Boolean openFeishuCertification;

    @ApiModelProperty(value = "开启钉钉")
    private Boolean openDingdingCertification;

    @ApiModelProperty(value = "开启Oauth2")
    private Boolean openOauth2Certification;

    @ApiModelProperty(value = "开启锐捷客户端扫码")
    private Boolean openRjclientCertification;

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    public UserListDTO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(UserListDTO[] itemArr) {
        this.itemArr = itemArr;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public Boolean getOpenCas() {
        return openCas;
    }

    public void setOpenCas(Boolean openCas) {
        this.openCas = openCas;
    }

    public Boolean getOpenHardware() {
        return openHardware;
    }

    public void setOpenHardware(Boolean openHardware) {
        this.openHardware = openHardware;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

    public Boolean getOpenWorkWeixinCertification() {
        return openWorkWeixinCertification;
    }

    public void setOpenWorkWeixinCertification(Boolean openWorkWeixinCertification) {
        this.openWorkWeixinCertification = openWorkWeixinCertification;
    }

    public Boolean getOpenFeishuCertification() {
        return openFeishuCertification;
    }

    public void setOpenFeishuCertification(Boolean openFeishuCertification) {
        this.openFeishuCertification = openFeishuCertification;
    }

    public Boolean getOpenDingdingCertification() {
        return openDingdingCertification;
    }

    public void setOpenDingdingCertification(Boolean openDingdingCertification) {
        this.openDingdingCertification = openDingdingCertification;
    }

    public Boolean getOpenOauth2Certification() {
        return openOauth2Certification;
    }

    public void setOpenOauth2Certification(Boolean openOauth2Certification) {
        this.openOauth2Certification = openOauth2Certification;
    }

    public Boolean getOpenRjclientCertification() {
        return openRjclientCertification;
    }

    public void setOpenRjclientCertification(Boolean openRjclientCertification) {
        this.openRjclientCertification = openRjclientCertification;
    }
}
