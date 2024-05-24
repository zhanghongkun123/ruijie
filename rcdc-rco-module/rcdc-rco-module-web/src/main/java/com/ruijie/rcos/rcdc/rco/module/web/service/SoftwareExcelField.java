package com.ruijie.rcos.rcdc.rco.module.web.service;

/**
 * Description: excel单元格数据列
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/20
 *
 * @author lihengjing
 */
public enum SoftwareExcelField implements ExcelField {

    SOFTWARE_GROUP_NAME("软件分组名称", 0), SOFTWARE_GROUP_TYPE("软件分组类型", 1),
    SOFTWARE_GROUP_DESC("软件分组描述", 2), SOFTWARE_NAME("软件名称",3), SOFTWARE_DESC("软件描述", 4)
    ,DIGITAL_SIGN("厂商数字签名", 5),DIGITAL_SIGN_FLAG("厂商数字签名标志", 6),
    PRODUCT_NAME("产品名称", 7),PRODUCT_NAME_FLAG("产品名称标志", 8),
    PROCESS_NAME("进程名", 9),PROCESS_NAME_FLAG("进程名标志", 10),
    ORIGINAL_FILE_NAME("原始文件名", 11),ORIGINAL_FILE_NAME_FLAG("原始文件名标志", 12),
    FILE_CUSTOM_MD5("文件特征码", 13),FILE_CUSTOM_MD5_FLAG("文件特征码标志", 14),
    FILE_IS_DIRECTORY("是否为文件夹", 15),FILE_PARENT_ID("所在文件夹标识", 16),
    FILE_ID("文件夹标识", 17),
    DIGITAL_SIGN_BLACK_FLAG("黑名单厂商数字签名标志", 18),
    PRODUCT_NAME_BLACK_FLAG("黑名单产品名称标志", 19),
    PROCESS_NAME_BLACK_FLAG("黑名单进程名标志", 20),
    ORIGINAL_FILE_NAME_BLACK_FLAG("黑名单原始文件名标志", 21),
    FILE_CUSTOM_MD5_BLACK_FLAG("黑名单文件特征码标志", 22);

    private String header;
    private Integer index;

    SoftwareExcelField(String header, Integer index) {
        this.header = header;
        this.index = index;
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public Integer getIndex() {
        return index;
    }
}
