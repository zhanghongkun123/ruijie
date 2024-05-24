package com.ruijie.rcos.rcdc.rco.module.impl.printer.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.enums.PrinterConfigMessageEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dao.PrinterConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.entity.PrinterConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dto.PrinterConfigBaseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dto.PrinterConfigGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;


/**
 * Description: 获取 GT 上传的打印机配置信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/21
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_UPLOAD_PRINTER_CONFIG)
public class GuestToolUploadPrinterManageConfigSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GuestToolUploadPrinterManageConfigSPIImpl.class);

    public static final Integer NOT_COVER = 0;

    @Autowired
    private PrinterConfigDAO printerConfigDAO;

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");

        final CbbGuesttoolMessageDTO messageDTO = request.getDto();
        PrinterConfigGuestToolMsgDTO guestToolMsgDTO = uploadPrinterConfig(messageDTO);

        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_CMD_ID_UPLOAD_PRINTER_CONFIG));
        dto.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_PRINTER_CONFIG);
        dto.setDeskId(messageDTO.getDeskId());
        dto.setBody(JSON.toJSONString(guestToolMsgDTO));

        return dto;
    }


    /**
     * 上传打印机配置
     * @param messageDTO
     * @return
     */
    private PrinterConfigGuestToolMsgDTO uploadPrinterConfig(CbbGuesttoolMessageDTO messageDTO) {

        PrinterConfigGuestToolMsgDTO configGuestToolMsgDTO = parseGuestToolMsg(messageDTO.getBody(), PrinterConfigGuestToolMsgDTO.class);
        PrinterConfigGuestToolMsgDTO.BodyMessage content = configGuestToolMsgDTO.getContent();
        PrinterConfigBaseInfoDTO baseInfoDTO = content.getBaseInfoDTO();

        PrinterConfigGuestToolMsgDTO.BodyMessage bodyMessage = new PrinterConfigGuestToolMsgDTO.BodyMessage();
        bodyMessage.setConfigSerial(content.getConfigSerial());

        if (!Boolean.valueOf(printerManageServiceAPI.getPrinterConfigStatus())) {
            LOGGER.info("打印机配置未打开");
            return buildGuestToolMsgDTO(PrinterConfigMessageEnum.MESSAGE_MANAGER_CLOSE.getCode(),
                    PrinterConfigMessageEnum.MESSAGE_MANAGER_CLOSE.getMessage(), bodyMessage);
        }

        if (!checkDetailMd5(content.getConfigDetail(), content.getConfigMd5())) {
            return buildGuestToolMsgDTO(PrinterConfigMessageEnum.MESSAGE_WRONG_DETAIL_MD5.getCode(),
                    PrinterConfigMessageEnum.MESSAGE_WRONG_DETAIL_MD5.getMessage(), bodyMessage);
        }

        PrinterConfigEntity printerConfig = printerConfigDAO.findByConfigName(baseInfoDTO.getConfigName());
        if (printerConfig == null) {
            PrinterConfigEntity printerConfigEntity = new PrinterConfigEntity();
            BeanUtils.copyProperties(baseInfoDTO, printerConfigEntity);
            printerConfigEntity.setConfigSerial(content.getConfigSerial());
            printerConfigEntity.setConfigDetail(content.getConfigDetail());
            printerConfigEntity.setConfigMd5(content.getConfigMd5());
            printerConfigEntity.setCreateTime(new Date());
            printerConfigDAO.save(printerConfigEntity);
        } else if (StringUtils.isEmpty(content.getCover()) || content.getCover().equals(NOT_COVER)) {
            LOGGER.info("有同名但不覆盖");
            return buildGuestToolMsgDTO(PrinterConfigMessageEnum.MESSAGE_SAME_NAME.getCode(),
                    PrinterConfigMessageEnum.MESSAGE_SAME_NAME.getMessage(), bodyMessage);
        } else {
            BeanUtils.copyProperties(baseInfoDTO, printerConfig);
            printerConfig.setConfigSerial(content.getConfigSerial());
            printerConfig.setConfigDetail(content.getConfigDetail());
            printerConfig.setConfigMd5(content.getConfigMd5());
            printerConfig.setCreateTime(new Date());
            printerConfig.setConfigEnableCovered(true);
            printerConfigDAO.save(printerConfig);
        }
        return buildGuestToolMsgDTO(PrinterConfigMessageEnum.MESSAGE_SUCCESS.getCode(),
                PrinterConfigMessageEnum.MESSAGE_SUCCESS.getMessage(), bodyMessage);
    }

    private PrinterConfigGuestToolMsgDTO buildGuestToolMsgDTO(Integer code, String message, PrinterConfigGuestToolMsgDTO.BodyMessage bodyMessage) {
        PrinterConfigGuestToolMsgDTO guestToolMsgDTO = new PrinterConfigGuestToolMsgDTO();
        guestToolMsgDTO.setCode(code);
        guestToolMsgDTO.setMessage(message);
        guestToolMsgDTO.setContent(bodyMessage);
        return guestToolMsgDTO;
    }

    private boolean checkDetailMd5(String detail, String md5) {
        if (StringUtils.isEmpty(detail) && StringUtils.isEmpty(md5)) {
            return true;
        }

        String detailMd5 = Md5Builder.computeTextMd5(detail);
        if (!detailMd5.equals(md5)) {
            LOGGER.error("md5 not matched, detail=[{}]  md5=[{}] md5sum=[{}]", detail, md5, detailMd5);
            return false;
        }

        return true;
    }


    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("guest tool报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }

}
