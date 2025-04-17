package dev.ikeepcalm.software.backend.eventdriven;

import dev.ikeepcalm.software.backend.eventdriven.events.AppointmentScheduledEvent;
import dev.ikeepcalm.software.backend.eventdriven.events.NotificationEvent;
import dev.ikeepcalm.software.backend.eventdriven.events.RepairRequestStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishStatusChangeEvent(RepairRequestStatusChangedEvent event) {
        log.info("Publishing status change event for request ID: {}, new status: {}",
                event.getRequestId(), event.getNewStatus());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.REPAIR_EXCHANGE,
                RabbitMQConfig.STATUS_CHANGE_KEY,
                event
        );
    }

    public void publishNotificationEvent(NotificationEvent event) {
        log.info("Publishing notification event for recipient: {}, type: {}",
                event.getRecipientId(), event.getType());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.REPAIR_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_KEY,
                event
        );
    }

    public void publishAppointmentEvent(AppointmentScheduledEvent event) {
        log.info("Publishing appointment event for request ID: {}, appointment time: {}",
                event.getRequestId(), event.getAppointmentTime());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.REPAIR_EXCHANGE,
                RabbitMQConfig.SCHEDULER_KEY,
                event
        );
    }
}