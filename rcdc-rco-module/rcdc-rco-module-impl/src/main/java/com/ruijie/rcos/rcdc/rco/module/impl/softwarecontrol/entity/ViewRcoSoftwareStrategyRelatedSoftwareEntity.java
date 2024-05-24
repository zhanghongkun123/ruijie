package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@Entity
@Table(name = "v_rco_software_strategy_related_software")
public class ViewRcoSoftwareStrategyRelatedSoftwareEntity extends EqualsHashcodeSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 软件策略Id
     **/

    private UUID strategyId;

    /**
     * 软件策略名
     */
    private String strategyName;


    /**
     * 软件策略描述
     */
    private String strategyDescription;

    /**
     * 软件策略类型 内置、自定义
     */
    private Boolean isWhitelistMode;

    /**
     * 软件分组Id
     **/
    private UUID groupId;

    /**
     * 软件分组名称
     **/
    private String groupName;

    /**
     * 软件名称
     **/
    private String name;

    /**
     * 软件名称-用户模糊搜索且支持不区分大小写
     */
    private String nameSearch;


    /**
     * 软件描述
     **/
    private String description;

    /**
     * 厂商数字签名
     */
    private String digitalSign;

    /**
     * 厂商数字签名 
     */
    private Boolean digitalSignFlag;

    /**
     * 安装路径
     */
    private String installPath;

    /**
     * 安装路径 
     */
    private Boolean installPathFlag;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品名称 
     */
    private Boolean productNameFlag;

    /**
     * 进程名
     */
    private String processName;

    /**
     * 进程名 
     */
    private Boolean processNameFlag;

    /**
     * 原始文件名
     */
    private String originalFileName;

    /**
     * 原始文件名 
     */
    private Boolean originalFileNameFlag;

    /**
     * 自定义md5值
     */
    private String fileCustomMd5;

    /**
     * 自定义md5值 
     */
    private Boolean fileCustomMd5Flag;


    /**
     * 文件目录标识
     */
    private Boolean directoryFlag;

    /**
     * 厂商数字签名(黑名单) 
     */
    private Boolean digitalSignBlackFlag;

    /**
     * 产品名称(黑名单) 
     */
    private Boolean productNameBlackFlag;

    /**
     * 进程名(黑名单) 
     */
    private Boolean processNameBlackFlag;

    /**
     * 原始文件名(黑名单) 
     */
    private Boolean originalFileNameBlackFlag;

    /**
     * 自定义md5值(黑名单) 
     */
    private Boolean fileCustomMd5BlackFlag;


    @Version
    private int version;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDigitalSign() {
        return digitalSign;
    }

    public void setDigitalSign(String digitalSign) {
        this.digitalSign = digitalSign;
    }

    public Boolean getDigitalSignFlag() {
        return digitalSignFlag;
    }

    public void setDigitalSignFlag(Boolean digitalSignFlag) {
        this.digitalSignFlag = digitalSignFlag;
    }

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }

    public Boolean getInstallPathFlag() {
        return installPathFlag;
    }

    public void setInstallPathFlag(Boolean installPathFlag) {
        this.installPathFlag = installPathFlag;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Boolean getProductNameFlag() {
        return productNameFlag;
    }

    public void setProductNameFlag(Boolean productNameFlag) {
        this.productNameFlag = productNameFlag;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Boolean getProcessNameFlag() {
        return processNameFlag;
    }

    public void setProcessNameFlag(Boolean processNameFlag) {
        this.processNameFlag = processNameFlag;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Boolean getOriginalFileNameFlag() {
        return originalFileNameFlag;
    }

    public void setOriginalFileNameFlag(Boolean originalFileNameFlag) {
        this.originalFileNameFlag = originalFileNameFlag;
    }

    public String getFileCustomMd5() {
        return fileCustomMd5;
    }

    public void setFileCustomMd5(String fileCustomMd5) {
        this.fileCustomMd5 = fileCustomMd5;
    }

    public Boolean getFileCustomMd5Flag() {
        return fileCustomMd5Flag;
    }

    public void setFileCustomMd5Flag(Boolean fileCustomMd5Flag) {
        this.fileCustomMd5Flag = fileCustomMd5Flag;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Boolean getDirectoryFlag() {
        return directoryFlag;
    }

    public void setDirectoryFlag(Boolean directoryFlag) {
        this.directoryFlag = directoryFlag;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }


    public String getStrategyDescription() {
        return strategyDescription;
    }

    public void setStrategyDescription(String strategyDescription) {
        this.strategyDescription = strategyDescription;
    }

    public Boolean getIsWhitelistMode() {
        return isWhitelistMode;
    }

    public void setIsWhitelistMode(Boolean whitelistMode) {
        isWhitelistMode = whitelistMode;
    }

    public String getNameSearch() {
        return nameSearch;
    }

    public void setNameSearch(String nameSearch) {
        this.nameSearch = nameSearch;
    }

    public Boolean getDigitalSignBlackFlag() {
        return digitalSignBlackFlag;
    }

    public void setDigitalSignBlackFlag(Boolean digitalSignBlackFlag) {
        this.digitalSignBlackFlag = digitalSignBlackFlag;
    }

    public Boolean getProductNameBlackFlag() {
        return productNameBlackFlag;
    }

    public void setProductNameBlackFlag(Boolean productNameBlackFlag) {
        this.productNameBlackFlag = productNameBlackFlag;
    }

    public Boolean getProcessNameBlackFlag() {
        return processNameBlackFlag;
    }

    public void setProcessNameBlackFlag(Boolean processNameBlackFlag) {
        this.processNameBlackFlag = processNameBlackFlag;
    }

    public Boolean getOriginalFileNameBlackFlag() {
        return originalFileNameBlackFlag;
    }

    public void setOriginalFileNameBlackFlag(Boolean originalFileNameBlackFlag) {
        this.originalFileNameBlackFlag = originalFileNameBlackFlag;
    }

    public Boolean getFileCustomMd5BlackFlag() {
        return fileCustomMd5BlackFlag;
    }

    public void setFileCustomMd5BlackFlag(Boolean fileCustomMd5BlackFlag) {
        this.fileCustomMd5BlackFlag = fileCustomMd5BlackFlag;
    }
}
