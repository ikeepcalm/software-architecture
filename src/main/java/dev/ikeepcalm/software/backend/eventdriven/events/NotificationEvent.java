package dev.ikeepcalm.software.backend.eventdriven.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Long recipientId;
    private String recipientEmail;
    private String subject;
    private String content;
    private NotificationType type;
    private ZonedDateTime timestamp;
    
    public enum NotificationType {
        STATUS_UPDATE,
        APPOINTMENT_REMINDER,
        PAYMENT_REQUIRED,
        REPAIR_COMPLETED
    }
}