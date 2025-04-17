package dev.ikeepcalm.software.backend.eventdriven.consumers;

import dev.ikeepcalm.software.backend.eventdriven.RabbitMQConfig;
import dev.ikeepcalm.software.backend.eventdriven.events.RepairRequestStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StatusChangeConsumer {

    @RabbitListener(queues = RabbitMQConfig.STATUS_CHANGE_QUEUE)
    public void handleStatusChangeEvent(RepairRequestStatusChangedEvent event) {
        log.info("Received status change event for request ID: {}", event.getRequestId());
        log.info("Status changed from {} to {}", event.getOldStatus(), event.getNewStatus());
        
        switch (event.getNewStatus()) {
            case IN_PROGRESS -> recordRepairStarted(event);
            case RESOLVED -> recordRepairCompleted(event);
            case CANCELLED -> recordRepairCancelled(event);
        }
    }
    
    private void recordRepairStarted(RepairRequestStatusChangedEvent event) {
        log.info("Recording repair started for request ID: {}", event.getRequestId());
    }
    
    private void recordRepairCompleted(RepairRequestStatusChangedEvent event) {
        log.info("Recording repair completed for request ID: {}", event.getRequestId());
    }
    
    private void recordRepairCancelled(RepairRequestStatusChangedEvent event) {
        log.info("Recording repair cancelled for request ID: {}", event.getRequestId());
    }
}