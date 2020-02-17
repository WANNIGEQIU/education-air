package com.air.server.sms.consumer;


import com.air.common.enums.ResultEnum;
import com.air.server.sms.exception.SmsException;
import com.air.server.sms.utils.SendSms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@Slf4j
public class SmsListener {



    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "smsQueue",durable = "true"),
           exchange = @Exchange(value = "heimaoSms",ignoreDeclarationExceptions = "true",
           type = ExchangeTypes.TOPIC),
            key = {"sms"}
    ))

    public void sendMessage(Map<String,String> map) {
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        String mobile = map.get("mobile");
        String verifyCode = map.get("verifyCode");
       log.info("短信服务 ---> 消费: {},{}",mobile,verifyCode);
       if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(verifyCode)) {
           throw new SmsException(ResultEnum.PARAM_ERROR);
       }
        SendSms.sendSms(mobile, verifyCode);


    }

}



