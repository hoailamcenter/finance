package com.finance.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.notification.form.BaseMsgForm;
import com.finance.notification.form.PostNotificationData;
import com.finance.service.RabbitMQService;
import com.finance.service.RabbitSender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class NotificationService {
    @Autowired
    RabbitMQService rabbitMQService;

    @Autowired
    RabbitSender rabbitSender;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${rabbitmq.notification.queue}")
    private String queueName;

    private <T> void handleSendMsg(T data, String cmd) {
        BaseMsgForm<T> form = new BaseMsgForm<>();
        form.setApp(NotificationConstant.BACKEND_APP);
        form.setCmd(cmd);
        form.setData(data);

        String msg;
        try {
            msg = objectMapper.writeValueAsString(form);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // create queue if existed
        rabbitMQService.createQueueIfNotExist(queueName);

        // push msg
        rabbitSender.send(msg, queueName);
    }

    public void sendNotification(String message, Long accountId) {
        PostNotificationData data = new PostNotificationData();
        data.setMessage(message);
        data.setAccountId(accountId);
        handleSendMsg(data, NotificationConstant.BACKEND_POST_NOTIFICATION_CMD);
    }
}