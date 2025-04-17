package dev.ikeepcalm.software.backend.eventdriven.consumers;

import dev.ikeepcalm.software.backend.eventdriven.RabbitMQConfig;
import dev.ikeepcalm.software.backend.eventdriven.events.AppointmentScheduledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerConsumer {

    @RabbitListener(queues = RabbitMQConfig.SCHEDULER_QUEUE)
    public void handleAppointmentEvent(AppointmentScheduledEvent event) {
        log.info("Received appointment event for request ID: {}", event.getRequestId());
        log.info("Updating appointment in scheduler service: Time={}, TechnicianID={}", 
                event.getAppointmentTime(), event.getTechnicianId());
    }
}

