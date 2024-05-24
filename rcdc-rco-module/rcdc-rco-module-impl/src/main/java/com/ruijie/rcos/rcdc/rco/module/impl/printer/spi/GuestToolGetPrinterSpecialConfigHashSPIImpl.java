package com.ruijie.rcos.rcdc.rco.module.impl.printer.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.enums.PrinterConfigMessageEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.special.dao.PrinterSpecialConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.special.entity.PrinterSpecialConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dto.PrinterSpecialConfigBaseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.printer.dto.PrinterSpecialConfigGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: GT 获取打印机特殊配置的 hash
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/24
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_GET_PRINTER_SPECIAL_CONFIG_HASH)
public class GuestToolGetPrinterSpecialConfigHashSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GuestToolGetPrinterSpecialConfigHashSPIImpl.class);

    public static final String NOT_SPECIAL_CONFIG = "not_spec";

    @Autowired
    private PrinterSpecialConfigDAO printerSpecialConfigDAO;

    @Autowired
    private PrinterManageServiceAPI printerManageServiceAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");

        final CbbGuesttoolMessageDTO messageDTO = request.getDto();
        PrinterSpecialConfigGuestToolMsgDTO configGuestToolMsgDTO = parseGuestToolMsg(messageDTO.getBody(),
                PrinterSpecialConfigGuestToolMsgDTO.class);
        PrinterSpecialConfigGuestToolMsgDTO.BodyMessage content = configGuestToolMsgDTO.getContent();

        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_CMD_ID_GET_PRINTER_SPECIAL_CONFIG_HASH));
        dto.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_PRINTER_CONFIG);
        dto.setDeskId(messageDTO.getDeskId());

        PrinterSpecialConfigGuestToolMsgDTO guestToolMsgDTO = new PrinterSpecialConfigGuestToolMsgDTO();
        PrinterSpecialConfigGuestToolMsgDTO.BodyMessage bodyMessage = new PrinterSpecialConfigGuestToolMsgDTO.BodyMessage();
        bodyMessage.setConfigSerial(content.getConfigSerial());

        if (Boolean.valueOf(printerManageServiceAPI.getPrinterConfigStatus())) {

            List<PrinterSpecialConfigEntity> printerSpecialConfigList = printerSpecialConfigDAO.findAll();
            PrinterSpecialConfigEntity specialConfigEntity = null;
            if (printerSpecialConfigList.size() > 0) {
                specialConfigEntity = printerSpecialConfigList.get(0);
            }

            if (specialConfigEntity != null) {
                PrinterSpecialConfigBaseInfoDTO baseInfoDTO = new PrinterSpecialConfigBaseInfoDTO();
                baseInfoDTO.setConfigMd5(specialConfigEntity.getConfigMd5());
                bodyMessage.setBaseInfoDTO(baseInfoDTO);
                guestToolMsgDTO.setCode(PrinterConfigMessageEnum.MESSAGE_SUCCESS.getCode());
                guestToolMsgDTO.setMessage(PrinterConfigMessageEnum.MESSAGE_SUCCESS.getMessage());
            } else {
                guestToolMsgDTO.setCode(PrinterConfigMessageEnum.MESSAGE_FAILURE.getCode());
                guestToolMsgDTO.setMessage(NOT_SPECIAL_CONFIG);
            }

        } else {
            LOGGER.info("打印机配置未打开");
            guestToolMsgDTO.setCode(PrinterConfigMessageEnum.MESSAGE_MANAGER_CLOSE.getCode());
            guestToolMsgDTO.setMessage(PrinterConfigMessageEnum.MESSAGE_MANAGER_CLOSE.getMessage());
        }
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