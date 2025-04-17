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
public class AppointmentScheduledEvent {
    private Long requestId;
    private Long appointmentId;
    private ZonedDateTime appointmentTime;
    private Long customerId;
    private Long technicianId;
    private ZonedDateTime timestamp;
}