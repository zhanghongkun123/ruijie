package com.ruijie.rcos.rcdc.rco.module.impl.printer.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.PrinterConfigBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.enums.PrinterConfigMessageEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dto.PrinterConfigBaseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dao.PrinterConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.entity.PrinterConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dto.PrinterConfigGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: GT 下载打印机配置
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/23
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_DOWNLOAD_PRINTER_CONFIG)
public class GuestToolDownloadPrinterConfigSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GuestToolDownloadPrinterConfigSPIImpl.class);

    @Autowired
    private PrinterConfigDAO printerConfigDAO;

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");

        final CbbGuesttoolMessageDTO messageDTO = request.getDto();
        PrinterConfigGuestToolMsgDTO configGuestToolMsgDTO = parseGuestToolMsg(messageDTO.getBody(), PrinterConfigGuestToolMsgDTO.class);
        PrinterConfigGuestToolMsgDTO.BodyMessage content = configGuestToolMsgDTO.getContent();
        PrinterConfigEntity printerConfigEntity = printerConfigDAO.findByConfigName(content.getConfigName());

        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_CMD_ID_DOWNLOAD_PRINTER_CONFIG));
        dto.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_PRINTER_CONFIG);
        dto.setDeskId(messageDTO.getDeskId());

        PrinterConfigGuestToolMsgDTO guestToolMsgDTO = new PrinterConfigGuestToolMsgDTO();
        PrinterConfigGuestToolMsgDTO.BodyMessage bodyMessage = new PrinterConfigGuestToolMsgDTO.BodyMessage();
        bodyMessage.setConfigSerial(content.getConfigSerial());

        if (!Boolean.valueOf(printerManageServiceAPI.getPrinterConfigStatus())) {
            LOGGER.info("打印机配置未打开");
            guestToolMsgDTO.setCode(PrinterConfigMessageEnum.MESSAGE_MANAGER_CLOSE.getCode());
            guestToolMsgDTO.setMessage(PrinterConfigMessageEnum.MESSAGE_MANAGER_CLOSE.getMessage());
            guestToolMsgDTO.setContent(bodyMessage);
            dto.setBody(JSON.toJSONString(guestToolMsgDTO));
            return dto;
        }

        if (printerConfigEntity == null) {
            LOGGER.info("打印机配置<{0}>不存在", content.getConfigName());
            guestToolMsgDTO.setCode(PrinterConfigMessageEnum.MESSAGE_FAILURE.getCode());
            guestToolMsgDTO.setMessage(LocaleI18nResolver.resolve(PrinterConfigBusinessKey.RCDC_RCO_PRINTER_CONFIG_NOT_EXIST));
            guestToolMsgDTO.setContent(bodyMessage);
            dto.setBody(JSON.toJSONString(guestToolMsgDTO));
            return dto;
        }

        PrinterConfigBaseInfoDTO baseInfoDTO = new PrinterConfigBaseInfoDTO();
        BeanUtils.copyProperties(printerConfigEntity, baseInfoDTO);
        bodyMessage.setConfigDetail(printerConfigEntity.getConfigDetail());
        bodyMessage.setConfigMd5(printerConfigEntity.getConfigMd5());
        bodyMessage.setBaseInfoDTO(baseInfoDTO);
        guestToolMsgDTO.setCode(PrinterConfigMessageEnum.MESSAGE_SUCCESS.getCode());
        guestToolMsgDTO.setMessage(PrinterConfigMessageEnum.MESSAGE_SUCCESS.getMessage());
        guestToolMsgDTO.setContent(bodyMessage);
        dto.setBody(JSON.toJSONString(guestToolMsgDTO));
        return dto;
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
