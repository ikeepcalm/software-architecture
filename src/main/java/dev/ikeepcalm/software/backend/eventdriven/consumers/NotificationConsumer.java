package dev.ikeepcalm.software.backend.eventdriven.consumers;


import dev.ikeepcalm.software.backend.eventdriven.RabbitMQConfig;
import dev.ikeepcalm.software.backend.eventdriven.events.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotificationEvent(NotificationEvent event) {
        log.info("Received notification event: {}", event);
        
        switch (event.getType()) {
            case STATUS_UPDATE -> sendStatusUpdateNotification(event);
            case APPOINTMENT_REMINDER -> sendAppointmentReminder(event);
            case PAYMENT_REQUIRED -> sendPaymentRequiredNotification(event);
            case REPAIR_COMPLETED -> sendRepairCompletedNotification(event);
        }
    }
    
    private void sendStatusUpdateNotification(NotificationEvent event) {
        log.info("Sending status update notification to: {}", event.getRecipientEmail());
    }
    
    private void sendAppointmentReminder(NotificationEvent event) {
        log.info("Sending appointment reminder to: {}", event.getRecipientEmail());
    }
    
    private void sendPaymentRequiredNotification(NotificationEvent event) {
        log.info("Sending payment required notification to: {}", event.getRecipientEmail());
    }
    
    private void sendRepairCompletedNotification(NotificationEvent event) {
        log.info("Sending repair completed notification to: {}", event.getRecipientEmail());
    }
}

